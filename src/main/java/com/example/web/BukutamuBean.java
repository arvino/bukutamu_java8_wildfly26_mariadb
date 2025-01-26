package com.example.web;

import com.example.ejb.BukutamuEJB;
import com.example.entity.Bukutamu;
import com.example.entity.Member;
import com.example.util.FileUtil;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class BukutamuBean implements Serializable {
    
    @EJB
    private BukutamuEJB bukutamuEJB;
    
    @Inject
    private LoginBean loginBean;
    
    private Bukutamu bukutamu = new Bukutamu();
    private Part uploadedFile;
    private List<Bukutamu> daftarBukutamu;
    
    public String submit() {
        try {
            // Validasi max 1x sehari
            if (bukutamuEJB.hasSubmittedToday(loginBean.getLoggedInMember().getId())) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Anda sudah submit buku tamu hari ini", null));
                return null;
            }
            
            // Set member
            bukutamu.setMember(loginBean.getLoggedInMember());
            
            // Upload file jika ada
            if (uploadedFile != null) {
                String realPath = FacesContext.getCurrentInstance()
                        .getExternalContext().getRealPath("/");
                String fileName = FileUtil.saveFile(uploadedFile, realPath);
                bukutamu.setGambar(fileName);
            }
            
            // Simpan ke database
            bukutamuEJB.create(bukutamu);
            
            // Reset form
            bukutamu = new Bukutamu();
            uploadedFile = null;
            
            // Tampilkan pesan sukses
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Buku tamu berhasil disimpan", null));
                
            return null;
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                e.getMessage(), null));
            return null;
        }
    }
    
    public String delete(Long id) {
        try {
            Bukutamu existing = bukutamuEJB.findById(id);
            if (existing != null) {
                // Hapus file jika ada
                if (existing.getGambar() != null) {
                    String realPath = FacesContext.getCurrentInstance()
                            .getExternalContext().getRealPath("/");
                    FileUtil.deleteFile(existing.getGambar(), realPath);
                }
                
                bukutamuEJB.delete(id);
                
                // Reset cache
                daftarBukutamu = null;
                
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, 
                    "Buku tamu berhasil dihapus", null));
            }
            return null;
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                e.getMessage(), null));
            return null;
        }
    }
    
    public List<Bukutamu> getDaftarBukutamu() {
        if (daftarBukutamu == null) {
            if (loginBean.isAdmin()) {
                daftarBukutamu = bukutamuEJB.findAll();
            } else {
                daftarBukutamu = bukutamuEJB.findByMemberId(loginBean.getLoggedInMember().getId());
            }
        }
        return daftarBukutamu;
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