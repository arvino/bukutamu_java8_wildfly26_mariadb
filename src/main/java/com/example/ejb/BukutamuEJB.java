package com.example.ejb;

import com.example.entity.Bukutamu;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Stateless
public class BukutamuEJB {
    
    @PersistenceContext
    private EntityManager em;
    
    public Bukutamu findById(Long id) {
        return em.find(Bukutamu.class, id);
    }
    
    public List<Bukutamu> findAll() {
        return em.createQuery("SELECT b FROM Bukutamu b ORDER BY b.createdAt DESC", Bukutamu.class)
                .getResultList();
    }
    
    public List<Bukutamu> findByMemberId(Long memberId) {
        return em.createQuery("SELECT b FROM Bukutamu b WHERE b.member.id = :memberId ORDER BY b.createdAt DESC", Bukutamu.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }
    
    public boolean hasSubmittedToday(Long memberId) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date startDate = cal.getTime();
        
        cal.add(Calendar.DATE, 1);
        Date endDate = cal.getTime();
        
        Long count = em.createQuery("SELECT COUNT(b) FROM Bukutamu b WHERE b.member.id = :memberId AND b.createdAt BETWEEN :startDate AND :endDate", Long.class)
                .setParameter("memberId", memberId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getSingleResult();
                
        return count > 0;
    }
    
    public long countAll() {
        return em.createQuery("SELECT COUNT(b) FROM Bukutamu b", Long.class)
                .getSingleResult();
    }
    
    public long countToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date startDate = cal.getTime();
        
        cal.add(Calendar.DATE, 1);
        Date endDate = cal.getTime();
        
        return em.createQuery("SELECT COUNT(b) FROM Bukutamu b WHERE b.createdAt BETWEEN :startDate AND :endDate", Long.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getSingleResult();
    }
    
    public void create(Bukutamu bukutamu) {
        em.persist(bukutamu);
    }
    
    public Bukutamu update(Bukutamu bukutamu) {
        return em.merge(bukutamu);
    }
    
    public void delete(Long id) {
        Bukutamu bukutamu = findById(id);
        if (bukutamu != null) {
            em.remove(bukutamu);
        }
    }
} 