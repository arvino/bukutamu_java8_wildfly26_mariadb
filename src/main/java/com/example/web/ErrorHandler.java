package com.example.web;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import com.example.exception.BusinessException;

public class ErrorHandler {
    
    public static void handleError(Throwable e) {
        FacesMessage.Severity severity = FacesMessage.SEVERITY_ERROR;
        String message = "Terjadi kesalahan sistem";
        
        if (e instanceof BusinessException) {
            message = e.getMessage();
        } else if (e instanceof IllegalArgumentException) {
            message = e.getMessage();
        }
        
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(severity, message, null));
    }
    
    public static void showInfo(String message) {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }
    
    public static void showWarning(String message) {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, message, null));
    }
    
    public static void showError(String message) {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }
} 