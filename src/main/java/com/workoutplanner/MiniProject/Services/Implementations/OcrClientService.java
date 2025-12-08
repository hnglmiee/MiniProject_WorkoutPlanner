package com.workoutplanner.MiniProject.Services.Implementations;

import com.workoutplanner.MiniProject.Helpers.MultipartInputStreamFileResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
// dùng để gọi API OCR bên Python
public class OcrClientService {

    // Tạo 1 instance RestTemplate để gọi HTTP request
    // Là cách Spring dùng để call API
    private final RestTemplate restTemplate = new RestTemplate();

    public String doOcr(MultipartFile file) throws IOException {
        // URL của OCR server viết bằng FastAPI/PaddleOCR.
        String url = "http://127.0.0.1:8000/ocr";

        // Tạo header HTTP -> Cho server biết bạn đang gửi loại dữ liệu gì
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Gửi file dạng multipart, data thật bạn muốn gửi lên server.
        // Body là dạng map key–value nhiều phần (multi-part).
        // LinkedMultiValueMap cho phép 1 key có nhiều value.
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        // Gán trường "file" đúng như FastAPI yêu cầu.
        // Spring không gửi được trực tiếp InputStream trong multipart, nên bạn bọc thành 1 resource custom có filename.
        body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));

        // Tạo request entity
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        // Gửi request sang OCR server
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        // Lấy list “texts” từ API OCR Python
        List<Map<String, Object>> texts = (List<Map<String, Object>>) response.getBody().get("texts");

        // Gộp text lại thành 1 đoạn RAW TEXT
        StringBuilder sb = new StringBuilder();
        for (Map<String, Object> item : texts) {
            sb.append(item.get("text")).append("\n");
        }

        return sb.toString();
    }
}
