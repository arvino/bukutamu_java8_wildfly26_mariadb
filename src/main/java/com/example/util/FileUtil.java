package com.example.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import javax.servlet.http.Part;

public class FileUtil {
    
    private static final String UPLOAD_DIR = "/uploads/bukutamu/";
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB
    private static final String[] ALLOWED_TYPES = {".jpg", ".jpeg", ".png"};
    
    public static String saveFile(Part file, String realPath) throws IOException {
        validateFile(file);
        
        String fileName = generateFileName(file);
        String uploadPath = realPath + UPLOAD_DIR;
        
        // Buat direktori jika belum ada
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        // Simpan file
        Path path = Paths.get(uploadPath + fileName);
        try (InputStream input = file.getInputStream()) {
            Files.copy(input, path, StandardCopyOption.REPLACE_EXISTING);
        }
        
        return fileName;
    }
    
    public static void deleteFile(String fileName, String realPath) {
        if (fileName != null && !fileName.isEmpty()) {
            try {
                Path path = Paths.get(realPath + UPLOAD_DIR + fileName);
                Files.deleteIfExists(path);
            } catch (IOException e) {
                // Log error
                e.printStackTrace();
            }
        }
    }
    
    private static void validateFile(Part file) throws IOException {
        // Cek ukuran file
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IOException("Ukuran file maksimal 2MB");
        }
        
        // Cek tipe file
        String fileName = file.getSubmittedFileName().toLowerCase();
        boolean validType = false;
        for (String type : ALLOWED_TYPES) {
            if (fileName.endsWith(type)) {
                validType = true;
                break;
            }
        }
        if (!validType) {
            throw new IOException("Tipe file tidak diizinkan. Gunakan: " + String.join(", ", ALLOWED_TYPES));
        }
    }
    
    private static String generateFileName(Part file) {
        String originalFileName = file.getSubmittedFileName();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return UUID.randomUUID().toString() + extension;
    }
} 