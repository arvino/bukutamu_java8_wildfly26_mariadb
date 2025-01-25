package com.example.ejb;

import com.example.entity.Tamu;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class TamuEJB {
    
    @PersistenceContext
    private EntityManager em;

    public void simpanTamu(Tamu tamu) {
        em.persist(tamu);
    }

    public List<Tamu> semuaTamu() {
        return em.createQuery("SELECT t FROM Tamu t ORDER BY t.tanggalKunjungan DESC", Tamu.class)
                .getResultList();
    }

    public Tamu getTamu(Long id) {
        return em.find(Tamu.class, id);
    }

    public void updateTamu(Tamu tamu) {
        em.merge(tamu);
    }

    public void hapusTamu(Long id) {
        Tamu tamu = getTamu(id);
        if (tamu != null) {
            em.remove(tamu);
        }
    }
} 