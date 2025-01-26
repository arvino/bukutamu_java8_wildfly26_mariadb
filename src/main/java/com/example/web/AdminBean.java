package com.example.web;

import com.example.ejb.MemberEJB;
import com.example.ejb.BukutamuEJB;
import com.example.entity.Member;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Named
@RequestScoped
public class AdminBean {
    
    @EJB
    private MemberEJB memberEJB;
    
    @EJB
    private BukutamuEJB bukutamuEJB;
    
    @javax.inject.Inject
    private LoginBean loginBean;
    
    private List<Member> daftarMember;
    private Map<String, Long> statistik;
    
    @PostConstruct
    public void init() {
        if (!loginBean.isAdmin()) {
            return;
        }
        
        loadStatistik();
        loadDaftarMember();
    }
    
    private void loadStatistik() {
        statistik = new HashMap<>();
        statistik.put("totalMember", memberEJB.countMembers());
        statistik.put("totalBukutamu", bukutamuEJB.countAll());
        statistik.put("bukutamuHariIni", bukutamuEJB.countToday());
    }
    
    private void loadDaftarMember() {
        daftarMember = memberEJB.findAllMembers();
    }
    
    public String deleteMember(Long id) {
        try {
            if (!loginBean.isAdmin()) {
                throw new SecurityException("Akses ditolak");
            }
            
            memberEJB.deleteMember(id);
            loadDaftarMember(); // Refresh data
            loadStatistik(); // Update statistik
            
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sukses", "Member berhasil dihapus"));
                
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
        return null;
    }
    
    // Getters
    public List<Member> getDaftarMember() {
        return daftarMember;
    }
    
    public long getTotalMember() {
        return statistik != null ? statistik.get("totalMember") : 0;
    }
    
    public long getTotalBukutamu() {
        return statistik != null ? statistik.get("totalBukutamu") : 0;
    }
    
    public long getBukutamuHariIni() {
        return statistik != null ? statistik.get("bukutamuHariIni") : 0;
    }
} 