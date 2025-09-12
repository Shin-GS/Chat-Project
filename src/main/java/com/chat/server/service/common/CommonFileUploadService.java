package com.chat.server.service.common;

import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface CommonFileUploadService {
    String uploadFile(String subDir,
                      MultipartFile file,
                      String fileName,
                      Set<String> allowedExtensions,
                      long maxBytes);
}
