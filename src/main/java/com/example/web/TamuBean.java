package com.example.web;

import com.example.ejb.TamuEJB;
import com.example.entity.Tamu;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.Date;
import java.util.List;

@Named
@RequestScoped
public class TamuBean {
    
    @EJB
    private TamuEJB tamuEJB;
    
    private Tamu tamu = new Tamu();
    private List<Tamu> daftarTamu;

    public String simpan() {
        tamu.setTanggalKunjungan(new Date());
        tamuEJB.simpanTamu(tamu);
        tamu = new Tamu();
        return "index?faces-redirect=true";
    }

    public List<Tamu> getDaftarTamu() {
        if (daftarTamu == null) {
            daftarTamu = tamuEJB.semuaTamu();
        }
        return daftarTamu;
    }

    public String hapus(Long id) {
        tamuEJB.hapusTamu(id);
        daftarTamu = null;
        return "index?faces-redirect=true";
    }

    public Tamu getTamu() {
        return tamu;
    }

    public void setTamu(Tamu tamu) {
        this.tamu = tamu;
    }
} 