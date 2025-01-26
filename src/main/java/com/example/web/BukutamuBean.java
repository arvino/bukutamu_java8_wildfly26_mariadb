package com.example.web;

import com.example.ejb.BukutamuEJB;
import com.example.entity.Bukutamu;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.UUID;

@Named
@SessionScoped
public class BukutamuBean implements Serializable {
    
    @EJB
    private BukutamuEJB bukutamuEJB;
    
    @Inject
    private LoginBean loginBean;
    
    private Bukutamu bukutamu = new Bukutamu();
    private Part uploadedFile;
    private List<Bukutamu> daftarBukutamu;
    
    private static final String UPLOAD_DIR = "/uploads/bukutamu/";

    public String submit() {
        try {
            // Set member dari user yang login
            bukutamu.setMember(loginBean.getLoggedInMember());
            
            // Handle file upload
            if (uploadedFile != null) {
                String fileName = UUID.randomUUID().toString() + getFileExtension(uploadedFile);
                String filePath = FacesContext.getCurrentInstance().getExternalContext()
                    .getRealPath(UPLOAD_DIR + fileName);
                
                // Buat direktori jika belum ada
                new File(filePath).getParentFile().mkdirs();
                
                // Save file
                try (InputStream input = uploadedFile.getInputStream()) {
                    Files.copy(input, new File(filePath).toPath());
                }
                
                bukutamu.setGambar(fileName);
            }
            
            bukutamuEJB.create(bukutamu);
            bukutamu = new Bukutamu();
            daftarBukutamu = null; // Reset cache
            
            FacesContext.getCurrentInstance().addMessage(
                null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Buku tamu berhasil disimpan")
            );
            
            return loginBean.isAdmin() ? 
                   "/admin/bukutamu?faces-redirect=true" : 
                   "/member/bukutamu?faces-redirect=true";
                   
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(
                null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage())
            );
            return null;
        }
    }

    public List<Bukutamu> getDaftarBukutamu() {
        if (daftarBukutamu == null) {
            if (loginBean.isAdmin()) {
                daftarBukutamu = bukutamuEJB.findAll();
            } else if (loginBean.isLoggedIn()) {
                daftarBukutamu = bukutamuEJB.findByMember(loginBean.getLoggedInMember().getId());
            } else {
                daftarBukutamu = bukutamuEJB.findAll(); // Untuk public view
            }
        }
        return daftarBukutamu;
    }

    public String delete(Long id) {
        if (loginBean.isAdmin()) {
            bukutamuEJB.delete(id);
            daftarBukutamu = null;
            return "/admin/bukutamu?faces-redirect=true";
        }
        return null;
    }

    private String getFileExtension(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                String fileName = token.substring(token.indexOf("=") + 2, token.length() - 1);
                return fileName.substring(fileName.lastIndexOf("."));
            }
        }
        return "";
    }

    // Getters and Setters
    public Bukutamu getBukutamu() {
        return bukutamu;
    }

    public void setBukutamu(Bukutamu bukutamu) {
        this.bukutamu = bukutamu;
    }

    public Part getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(Part uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
} 