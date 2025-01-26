package com.example.web;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter({"/member/*", "/admin/*"})
public class MemberFilter implements Filter {
    
    @Inject
    private LoginBean loginBean;
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        String path = req.getRequestURI();
        
        // Cek apakah user sudah login
        if (!loginBean.isLoggedIn()) {
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
            return;
        }
        
        // Cek akses ke halaman admin
        if (path.contains("/admin/") && !loginBean.isAdmin()) {
            res.sendRedirect(req.getContextPath() + "/error/403.xhtml");
            return;
        }
        
        chain.doFilter(request, response);
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
    @Override
    public void destroy() {
    }
} 