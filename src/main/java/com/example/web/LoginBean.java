package com.example.web;

import com.example.ejb.MemberEJB;
import com.example.entity.Member;
import com.example.util.ValidationUtil;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginBean implements Serializable {
    
    @EJB
    private MemberEJB memberEJB;
    
    private String email;
    private String password;
    private Member member = new Member();
    private Member loggedInMember;
    
    public String login() {
        try {
            // Validasi input
            ValidationUtil.validateEmail(email);
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Password wajib diisi");
            }
            
            // Cek kredensial
            Member member = memberEJB.findByEmail(email);
            if (member == null || !member.getPassword().equals(password)) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Email atau password salah", null));
                return null;
            }
            
            // Set session
            loggedInMember = member;
            
            // Clear form
            email = null;
            password = null;
            
            // Redirect sesuai role
            return isAdmin() ? 
                "/admin/dashboard?faces-redirect=true" : 
                "/member/dashboard?faces-redirect=true";
                
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                e.getMessage(), null));
            return null;
        }
    }
    
    public String register() {
        try {
            // Validasi input
            ValidationUtil.validateName(member.getNama());
            ValidationUtil.validateEmail(member.getEmail());
            ValidationUtil.validatePhone(member.getPhone());
            ValidationUtil.validatePassword(member.getPassword());
            
            // Cek email sudah terdaftar
            if (memberEJB.isEmailExists(member.getEmail())) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Email sudah terdaftar", null));
                return null;
            }
            
            // Set role default
            member.setRole("member");
            
            // Simpan ke database
            memberEJB.create(member);
            
            // Set session
            loggedInMember = member;
            
            // Reset form
            member = new Member();
            
            return "/member/dashboard?faces-redirect=true";
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                e.getMessage(), null));
            return null;
        }
    }
    
    public String updateProfile() {
        try {
            // Validasi input
            ValidationUtil.validateName(loggedInMember.getNama());
            ValidationUtil.validatePhone(loggedInMember.getPhone());
            
            // Update password jika diisi
            if (password != null && !password.isEmpty()) {
                ValidationUtil.validatePassword(password);
                loggedInMember.setPassword(password);
            }
            
            // Update ke database
            memberEJB.update(loggedInMember);
            
            // Clear password
            password = null;
            
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Profil berhasil diupdate", null));
                
            return null;
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                e.getMessage(), null));
            return null;
        }
    }
    
    public String logout() {
        // Invalidate session
        FacesContext.getCurrentInstance()
                .getExternalContext()
                .invalidateSession();
        return "/index?faces-redirect=true";
    }
    
    public boolean isLoggedIn() {
        return loggedInMember != null;
    }
    
    public boolean isAdmin() {
        return isLoggedIn() && loggedInMember.getRole().equals("admin");
    }
    
    // Getters and Setters
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Member getMember() {
        return member;
    }
    
    public void setMember(Member member) {
        this.member = member;
    }
    
    public Member getLoggedInMember() {
        return loggedInMember;
    }
} 