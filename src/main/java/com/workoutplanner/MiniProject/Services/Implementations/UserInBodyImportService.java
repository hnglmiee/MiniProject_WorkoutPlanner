package com.workoutplanner.MiniProject.Services.Implementations;

import com.workoutplanner.MiniProject.Exception.AppException;
import com.workoutplanner.MiniProject.Exception.ErrorCode;
import com.workoutplanner.MiniProject.Models.User;
import com.workoutplanner.MiniProject.Models.UserInbody;
import com.workoutplanner.MiniProject.Payload.Response.InBodyExtractResponse;
import com.workoutplanner.MiniProject.Repositories.UserInbodyRepository;
import com.workoutplanner.MiniProject.Repositories.UserRepository;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class UserInBodyImportService {
    @Autowired
    // Convert PDF → nhiều ảnh.
    private PDFToImageService pdfToImageService;

    @Autowired
    // OCR từng ảnh.
    // Lấy text từ ảnh
    private OcrService ocrService;

    @Autowired
    // Parse text → số đo.
    private ParserService parserService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInbodyRepository userInbodyRepository;

    @Autowired
    private OcrClientService ocrClientService;

    // Nhận file PDF upload từ client.
    // Trả về DTO chứa dữ liệu đã parse.
    public InBodyExtractResponse importFromPdf(MultipartFile file) throws IOException, TesseractException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 1) Convert PDF -> images
        List<BufferedImage> pages = pdfToImageService.convertPdfToImages(file, 300);

        // 2) OCR từng trang → ghép thành 1 chuỗi text
        StringBuilder sb = new StringBuilder();
        for (BufferedImage img : pages) {
            // doOcr(img) chạy Tesseract trên từng trang.
            String pageText = ocrService.doOcr(img);
            // Append text lại thành 1 chuỗi lớn để dễ parse.
            sb.append(pageText).append("\n");
        }
        String combinedText = sb.toString();

        // 3) Dùng regex parser để trích xuất số đo
        InBodyExtractResponse parsed = new InBodyExtractResponse();

        // Tìm số trước "Female/Male", ví dụ: 23 Male.
        Integer age = parserService.extractIntFirstMatch(combinedText, "(\\d{1,3})\\s+(Female|Male)");
        Double heightVal = parserService.extractDoubleFirstMatch(combinedText, "(\\d{2,3}\\.\\d)\\s*cm");
        Double weightVal = parserService.extractDoubleFirstMatch(combinedText, "Weight\\s*\\(kg\\)\\s*([0-9]{1,3}(?:\\.[0-9]+)?)");
        Double bodyFatMassVal = parserService.extractDoubleFirstMatch(combinedText, "Body\\s*Fat\\s*Mass\\s*\\(kg\\)\\s*([0-9]{1,3}(?:\\.[0-9]+)?)");
        Double bodyFatVal = parserService.extractDoubleFirstMatch(combinedText, "Percent\\s*Body\\s*Fat[^0-9]*([0-9]{1,3}(?:\\.[0-9]+)?)");
        Double muscleVal = parserService.extractDoubleFirstMatch(combinedText, "Skeletal\\s+Muscle\\s+Mass\\s*\\(kg\\)\\s*[^0-9\\n]*([0-9]{1,3}(?:\\.[0-9]+)?)");

        // Set vào Response DTO
        // Convert sang BigDecimal
        parsed.setAge(age);
        parsed.setHeight(heightVal == null ? null : new BigDecimal(heightVal.toString()));
        parsed.setWeight(weightVal == null ? null : new BigDecimal(weightVal.toString()));
        parsed.setBodyFatPercentage(bodyFatVal == null ? null : new BigDecimal(bodyFatVal.toString()));
        parsed.setMuscleMass(muscleVal == null ? null : new BigDecimal(muscleVal.toString()));
        // Ghi measuredAt là thời điểm import.
        parsed.setMeasuredAt(Instant.now());

        // 4) Map DTO → entity.
        UserInbody inbody = new UserInbody();
        inbody.setUser(user);
        inbody.setAge(parsed.getAge());
        inbody.setMeasuredAt(parsed.getMeasuredAt());
        inbody.setHeight(parsed.getHeight());
        inbody.setWeight(parsed.getWeight());
        inbody.setBodyFatPercentage(parsed.getBodyFatPercentage());
        inbody.setMuscleMass(parsed.getMuscleMass());
        inbody.setNotes("Imported from PDF; rawText length=" + (combinedText == null ? 0 : combinedText.length()));

        UserInbody saved = userInbodyRepository.save(inbody);

        // 5) Save
        parsed.setId(saved.getId());

        // 6) Return
        return parsed;
    }

    public InBodyExtractResponse importFromImage(MultipartFile file) throws IOException {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 1) Gọi OCR server (paddleocr)
        String rawText = ocrClientService.doOcr(file);

        // 2) Parse bằng regex như cũ
        InBodyExtractResponse parsed = new InBodyExtractResponse();
        Integer age = parserService.extractIntFirstMatch(
                rawText,
                "\\b(1[0-9]|[2-9][0-9])\\b(?=\\s*Female|\\s*Male)"
        );

        Double heightVal = parserService.extractDoubleFirstMatch(
                rawText,
                "([0-9]{2,3}\\.[0-9]{1,2})\\s*cm"
        );

        Double weightVal = parserService.extractDoubleFirstMatch(
                rawText,
                "Weight[\\s\\S]{0,20}?([0-9]{1,3}\\.?[0-9]*)"
        );

        Double bodyFatVal = parserService.extractDoubleFirstMatch(
                rawText,
                "Percent Body Fat[\\s\\S]{0,40}?([0-9]{1,3}\\.?[0-9]*)"
        );

        Double muscleVal = parserService.extractDoubleFirstMatch(
                rawText,
                "SMM[\\s\\S]{0,20}?([0-9]{1,3}\\.?[0-9]*)"
        );


        parsed.setAge(age);
        parsed.setHeight(heightVal == null ? null : new BigDecimal(heightVal));
        parsed.setWeight(weightVal == null ? null : new BigDecimal(weightVal));
        parsed.setBodyFatPercentage(bodyFatVal == null ? null : new BigDecimal(bodyFatVal));
        parsed.setMuscleMass(muscleVal == null ? null : new BigDecimal(muscleVal));
        parsed.setMeasuredAt(Instant.now());

        System.out.println("=== RAW TEXT (from PaddleOCR) ===");
        System.out.println(rawText);

        // 3) Map vào entity
        UserInbody inbody = new UserInbody();
        // Không set inbody.setUser(user) = KHÔNG THỂ biết record thuộc về user nào.
        inbody.setUser(user);
        inbody.setAge(parsed.getAge());
        inbody.setMeasuredAt(parsed.getMeasuredAt());
        inbody.setHeight(parsed.getHeight());
        inbody.setWeight(parsed.getWeight());
        inbody.setBodyFatPercentage(parsed.getBodyFatPercentage());
        inbody.setMuscleMass(parsed.getMuscleMass());
        inbody.setNotes("Imported from Image; length=" + rawText.length());

        UserInbody saved = userInbodyRepository.save(inbody);
        parsed.setId(saved.getId());
        return parsed;
    }
}
