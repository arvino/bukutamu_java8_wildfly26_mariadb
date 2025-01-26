package com.example.rest;

import com.example.ejb.MemberEJB;
import com.example.entity.Member;
import com.example.util.JwtUtil;
import com.example.util.ValidationUtil;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {
    
    @EJB
    private MemberEJB memberEJB;
    
    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        try {
            // Validate input
            ValidationUtil.validateEmail(request.getEmail());
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("Password wajib diisi");
            }
            
            // Check credentials
            Member member = memberEJB.findByEmail(request.getEmail());
            if (member == null || !member.getPassword().equals(request.getPassword())) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(ApiResponse.error("Email atau password salah"))
                        .build();
            }
            
            // Generate JWT token
            String token = JwtUtil.generateToken(member);
            
            // Create response data
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("member", member);
            
            return Response.ok(ApiResponse.success("Login berhasil", data)).build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage()))
                    .build();
        }
    }
    
    @POST
    @Path("/register")
    public Response register(Member member) {
        try {
            // Validate input
            ValidationUtil.validateName(member.getNama());
            ValidationUtil.validateEmail(member.getEmail());
            ValidationUtil.validatePhone(member.getPhone());
            ValidationUtil.validatePassword(member.getPassword());
            
            // Check if email exists
            if (memberEJB.isEmailExists(member.getEmail())) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Email sudah terdaftar"))
                        .build();
            }
            
            // Set default role
            member.setRole("member");
            
            // Save to database
            memberEJB.create(member);
            
            // Generate JWT token
            String token = JwtUtil.generateToken(member);
            
            // Create response data
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("member", member);
            
            return Response.status(Response.Status.CREATED)
                    .entity(ApiResponse.success("Registrasi berhasil", data))
                    .build();
                    
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage()))
                    .build();
        }
    }
    
    public static class LoginRequest {
        private String email;
        private String password;
        
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
    }
} 