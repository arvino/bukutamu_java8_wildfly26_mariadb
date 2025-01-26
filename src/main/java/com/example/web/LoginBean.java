package com.example.web;

import com.example.ejb.MemberEJB;
import com.example.entity.Member;
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
    
    private Member member = new Member();
    private String email;
    private String password;
    private Member loggedInMember;

    public String login() {
        loggedInMember = memberEJB.login(email, password);
        if (loggedInMember != null) {
            if ("admin".equals(loggedInMember.getRole())) {
                return "/admin/dashboard?faces-redirect=true";
            } else {
                return "/member/dashboard?faces-redirect=true";
            }
        }
        FacesContext.getCurrentInstance().addMessage(
            null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login gagal", "Email atau password salah")
        );
        return null;
    }

    public String register() {
        try {
            memberEJB.register(member);
            FacesContext.getCurrentInstance().addMessage(
                null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Registrasi berhasil", "Silakan login")
            );
            return "login?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(
                null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registrasi gagal", e.getMessage())
            );
            return null;
        }
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index?faces-redirect=true";
    }

    public boolean isLoggedIn() {
        return loggedInMember != null;
    }

    public boolean isAdmin() {
        return isLoggedIn() && "admin".equals(loggedInMember.getRole());
    }

    // Getters and Setters
    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

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

    public Member getLoggedInMember() {
        return loggedInMember;
    }
} 