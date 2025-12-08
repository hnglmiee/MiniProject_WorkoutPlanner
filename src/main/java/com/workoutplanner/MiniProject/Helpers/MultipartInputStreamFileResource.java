package com.workoutplanner.MiniProject.Helpers;

import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.io.InputStream;

// Spring không hỗ trợ gửi InputStream như multipart, phải tự bọc lại.
public class MultipartInputStreamFileResource extends InputStreamResource {
    private final String fileName;
    public MultipartInputStreamFileResource(final InputStream inputStream, final String fileName) {
        super(inputStream);
        this.fileName = fileName;
    }

    @Override
    public String getFilename() {
        return this.fileName;
    }

    @Override
    public long contentLength() throws IOException {
        return -1;
    }
}
