package com.workoutplanner.MiniProject.Services.Implementations;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

// Nhận List<BufferedImage>
// Chạy OCR bằng Tesseract
// Trả về text thô (raw text)

@Service
public class OcrService {
    // PDF
    @Autowired
    private Tesseract tesseract;

    // Convert BufferedImage → file tạm
    // Nhận 1 ảnh BufferedImage.
    public String doOcr(BufferedImage image) throws TesseractException {
        // Gọi Tesseract để OCR → trả về text thô.
        // Ghép text toàn bộ PDF thành 1 chuỗi
        return tesseract.doOCR(image);
    }
}
