package com.example.util;

import java.util.regex.Pattern;

public class ValidationUtil {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^[0-9]{10,13}$");
    
    public static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email wajib diisi");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Format email tidak valid");
        }
    }
    
    public static void validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Nomor HP wajib diisi");
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new IllegalArgumentException("Format nomor HP tidak valid (10-13 digit)");
        }
    }
    
    public static void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password wajib diisi");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password minimal 6 karakter");
        }
    }
    
    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama wajib diisi");
        }
        if (name.length() < 3) {
            throw new IllegalArgumentException("Nama minimal 3 karakter");
        }
    }
} 