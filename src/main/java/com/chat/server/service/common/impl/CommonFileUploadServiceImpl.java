package com.chat.server.service.common.impl;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.constant.Constants;
import com.chat.server.common.exception.CustomException;
import com.chat.server.service.common.CommonFileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CommonFileUploadServiceImpl implements CommonFileUploadService {
    @Value("${app.upload.base-url:http://localhost:8080}")
    private String publicBaseUrl;

    @Override
    public String uploadFile(String subDir,
                             MultipartFile file,
                             String fileName,
                             Set<String> allowedExtensions,
                             long maxBytes) {
        // Validate Basic
        if (file == null || file.isEmpty()) {
            throw new CustomException(ErrorCode.FILE_EMPTY);
        }

        if (maxBytes > 0 && file.getSize() > maxBytes) {
            throw new CustomException(ErrorCode.FILE_TOO_LARGE);
        }

        // Validate Extension
        String ext = getExtension(file.getOriginalFilename());
        if (!StringUtils.hasText(ext)) {
            throw new CustomException(ErrorCode.FILE_EXTENSION_MISSING);
        }

        String lowerExtension = ext.toLowerCase();
        if (allowedExtensions != null && !allowedExtensions.isEmpty()
                && allowedExtensions.stream().map(String::toLowerCase).noneMatch(lowerExtension::equals)) {
            throw new CustomException(ErrorCode.FILE_EXTENSION_NOT_ALLOWED);
        }

        // Validate fileName (prevent path injection)
        String storedFileName = validateFileName(fileName) + "." + lowerExtension;

        // Ensure directory exists under base
        Path base = Paths.get(Constants.UPLOAD_BASE_DIR).toAbsolutePath().normalize();
        Path targetDir = StringUtils.hasText(subDir) ? Paths.get(base.toString(), subDir) : base;
        try {
            Files.createDirectories(targetDir);

        } catch (IOException e) {
            throw new CustomException(ErrorCode.UPLOAD_DIR_CREATE_FAILED);
        }

        // Store file
        Path targetFile = targetDir.resolve(storedFileName);
        try {
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_STORE_FAILED);
        }

        // Build FULL absolute URL safely: {publicBaseUrl}{UPLOAD_BASE_PATH}/{subDir?}/{storedFileName}
        return createFileUrl(subDir, storedFileName);
    }

    private String getExtension(String originFilename) {
        if (!StringUtils.hasText(originFilename)) {
            return "";
        }

        String filename = Paths.get(originFilename).getFileName().toString();
        int dot = filename.lastIndexOf('.');
        return (dot < 0 || dot == filename.length() - 1) ? "" : filename.substring(dot + 1);
    }

    private String validateFileName(String originFilename) {
        if (!StringUtils.hasText(originFilename)) {
            throw new CustomException(ErrorCode.FILE_STORE_FAILED); // or a specific code if you prefer
        }

        String trimmedFilename = originFilename.trim();
        if (trimmedFilename.contains("/") || trimmedFilename.contains("\\")) {
            throw new CustomException(ErrorCode.FILE_STORE_FAILED);
        }

        if (!trimmedFilename.matches("[A-Za-z0-9._-]{1,100}")) {
            throw new CustomException(ErrorCode.FILE_STORE_FAILED);
        }

        return trimmedFilename;
    }

    private String createFileUrl(String subDir,
                                 String fileName) {
        String encodedFile = urlEncode(fileName);
        if (StringUtils.hasText(subDir)) {
            return publicBaseUrl + subDir + "/" + encodedFile;
        }

        return publicBaseUrl + "/" + encodedFile;
    }

    private String urlEncode(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8).replace("+", "%20");
    }
}
