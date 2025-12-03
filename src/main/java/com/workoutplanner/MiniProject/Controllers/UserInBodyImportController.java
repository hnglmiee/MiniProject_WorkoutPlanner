package com.workoutplanner.MiniProject.Controllers;

import com.workoutplanner.MiniProject.Constants.ApiPaths;
import com.workoutplanner.MiniProject.Payload.Response.ApiResponse;
import com.workoutplanner.MiniProject.Payload.Response.InBodyExtractResponse;
import com.workoutplanner.MiniProject.Services.Implementations.UserInBodyImportService;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(ApiPaths.UserInBody.IMPORT)
public class UserInBodyImportController {

    private final UserInBodyImportService importService;

    public UserInBodyImportController(UserInBodyImportService importService) {
        this.importService = importService;
    }

    @PostMapping(
            value = "/upload",
            // endpoint bắt buộc nhận multipart file upload.
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<InBodyExtractResponse>> uploadInbodyPdf(
            @RequestParam("file") MultipartFile file
    ) {
        ApiResponse<InBodyExtractResponse> apiResponse = new ApiResponse<>();

        // Kiểm tra nếu người dùng không chọn file hoặc file rỗng.
        if (file == null || file.isEmpty()) {
            apiResponse.setMessage("File is required!");
            apiResponse.setResult(null);
            apiResponse.setCode(400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }

        try {
            // Gọi service chính để xử lý OCR
            InBodyExtractResponse result = importService.importFromPdf(file);
            apiResponse.setResult(result);
            apiResponse.setMessage("Upload & OCR successfully!");
            apiResponse.setCode(200);
            return ResponseEntity.ok(apiResponse);

            // Catch lỗi OCR / PDF
        } catch (IOException | TesseractException e) {
            apiResponse.setMessage("OCR failed: " + e.getMessage());
            apiResponse.setResult(null);
            apiResponse.setCode(500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);

            // Catch lỗi không mong muốn
        } catch (Exception e) {
            apiResponse.setMessage("Unexpected error: " + e.getMessage());
            apiResponse.setResult(null);
            apiResponse.setCode(500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<InBodyExtractResponse>> uploadInbodyImage(@RequestPart("file") MultipartFile file) {
        ApiResponse<InBodyExtractResponse> apiResponse = new ApiResponse<>();
        if (file == null || file.isEmpty()) {
            apiResponse.setMessage("File is required!");
            apiResponse.setResult(null);
            apiResponse.setCode(400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }

        String contentType = file.getContentType();
        if(contentType == null || contentType.equals("/image/png") || contentType.equals("/image/jpeg") || contentType.equals("/image/jpg")) {
            apiResponse.setMessage("Only PNG/JPG/JPEG images are allowed");
        }

        try {
            InBodyExtractResponse response = importService.importFromImage(file);
            apiResponse.setResult(response);
            apiResponse.setMessage("Upload & OCR successfully!");
            apiResponse.setCode(200);
            return ResponseEntity.ok(apiResponse);
        } catch (TesseractException e) {
            apiResponse.setMessage("Unexpected error: " + e.getMessage());
            apiResponse.setResult(null);
            apiResponse.setCode(500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        } catch (IOException e) {
            apiResponse.setMessage("Unexpected error: " + e.getMessage());
            apiResponse.setResult(null);
            apiResponse.setCode(500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }
}
