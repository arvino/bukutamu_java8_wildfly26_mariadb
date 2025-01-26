package com.example.rest;

import com.example.ejb.MemberEJB;
import com.example.entity.Member;
import com.example.util.ValidationUtil;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/members")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MemberResource {

    @EJB
    private MemberEJB memberEJB;
    
    @Context
    private HttpServletRequest request;
    
    @GET
    @Path("/profile")
    public Response getProfile() {
        try {
            // Get logged in member from JWT
            Member member = (Member) request.getAttribute("member");
            return Response.ok(ApiResponse.success("Sukses", member)).build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage()))
                    .build();
        }
    }
    
    @PUT
    @Path("/profile")
    public Response updateProfile(Member member) {
        try {
            // Get logged in member from JWT
            Member loggedInMember = (Member) request.getAttribute("member");
            
            // Validate input
            ValidationUtil.validateName(member.getNama());
            ValidationUtil.validatePhone(member.getPhone());
            
            // Update data
            loggedInMember.setNama(member.getNama());
            loggedInMember.setPhone(member.getPhone());
            
            // Update password if provided
            if (member.getPassword() != null && !member.getPassword().isEmpty()) {
                ValidationUtil.validatePassword(member.getPassword());
                loggedInMember.setPassword(member.getPassword());
            }
            
            memberEJB.update(loggedInMember);
            return Response.ok(ApiResponse.success("Profil berhasil diupdate", loggedInMember))
                    .build();
                    
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage()))
                    .build();
        }
    }
    
    @GET
    @Path("/check-email")
    public Response checkEmail(@QueryParam("email") String email) {
        try {
            ValidationUtil.validateEmail(email);
            boolean exists = memberEJB.isEmailExists(email);
            return Response.ok(ApiResponse.success("Sukses", exists)).build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage()))
                    .build();
        }
    }
} 