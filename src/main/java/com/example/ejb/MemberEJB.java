package com.example.ejb;

import com.example.entity.Member;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.security.MessageDigest;
import java.util.Base64;

@Stateless
public class MemberEJB {
    
    @PersistenceContext
    private EntityManager em;

    public Member register(Member member) {
        // Validasi email unik
        if (findByEmail(member.getEmail()) != null) {
            throw new RuntimeException("Email sudah terdaftar");
        }
        
        // Hash password
        member.setPassword(hashPassword(member.getPassword()));
        
        // Set role default
        member.setRole("member");
        
        em.persist(member);
        return member;
    }

    public Member login(String email, String password) {
        try {
            return em.createQuery(
                    "SELECT m FROM Member m WHERE m.email = :email AND m.password = :password", 
                    Member.class)
                .setParameter("email", email)
                .setParameter("password", hashPassword(password))
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Member findByEmail(String email) {
        try {
            return em.createQuery(
                    "SELECT m FROM Member m WHERE m.email = :email", 
                    Member.class)
                .setParameter("email", email)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Member> findAllMembers() {
        return em.createQuery("SELECT m FROM Member m ORDER BY m.createdAt DESC", Member.class)
                .getResultList();
    }

    public Member updateProfile(Long id, String nama, String phone, String password) {
        Member member = em.find(Member.class, id);
        if (member != null) {
            member.setNama(nama);
            member.setPhone(phone);
            if (password != null && !password.isEmpty()) {
                member.setPassword(hashPassword(password));
            }
            em.merge(member);
        }
        return member;
    }

    public void deleteMember(Long id) {
        Member member = em.find(Member.class, id);
        if (member != null) {
            em.remove(member);
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public long countMembers() {
        return em.createQuery("SELECT COUNT(m) FROM Member m", Long.class)
                 .getSingleResult();
    }

    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    public boolean isEmailExists(String email) {
        return findByEmail(email) != null;
    }

    public void create(Member member) {
        em.persist(member);
    }

    public Member update(Member member) {
        return em.merge(member);
    }

    public void delete(Long id) {
        Member member = findById(id);
        if (member != null && !member.getRole().equals("admin")) {
            em.remove(member);
        }
    }
} 