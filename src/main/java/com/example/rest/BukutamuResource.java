package com.example.rest;

import com.example.ejb.BukutamuEJB;
import com.example.entity.Bukutamu;
import com.example.entity.Member;
import com.example.util.FileUtil;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/bukutamu")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BukutamuResource {

    @EJB
    private BukutamuEJB bukutamuEJB;
    
    @Context
    private HttpServletRequest request;
    
    @GET
    public Response getAll() {
        List<Bukutamu> list = bukutamuEJB.findAll();
        return Response.ok(ApiResponse.success("Sukses", list)).build();
    }
    
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Bukutamu bukutamu = bukutamuEJB.findById(id);
        if (bukutamu == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Data tidak ditemukan"))
                    .build();
        }
        return Response.ok(ApiResponse.success("Sukses", bukutamu)).build();
    }
    
    @POST
    public Response create(Bukutamu bukutamu) {
        try {
            // Get logged in member from JWT
            Member member = (Member) request.getAttribute("member");
            bukutamu.setMember(member);
            
            // Validate max 1 entry per day
            if (bukutamuEJB.hasSubmittedToday(member.getId())) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Anda sudah submit buku tamu hari ini"))
                        .build();
            }
            
            bukutamuEJB.create(bukutamu);
            return Response.status(Response.Status.CREATED)
                    .entity(ApiResponse.success("Buku tamu berhasil disimpan", bukutamu))
                    .build();
                    
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage()))
                    .build();
        }
    }
    
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Bukutamu bukutamu) {
        try {
            // Get logged in member from JWT
            Member member = (Member) request.getAttribute("member");
            
            // Get existing data
            Bukutamu existing = bukutamuEJB.findById(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("Data tidak ditemukan"))
                        .build();
            }
            
            // Validate ownership
            if (!existing.getMember().getId().equals(member.getId())) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(ApiResponse.error("Anda tidak memiliki akses"))
                        .build();
            }
            
            // Update data
            existing.setMessages(bukutamu.getMessages());
            bukutamuEJB.update(existing);
            
            return Response.ok(ApiResponse.success("Buku tamu berhasil diupdate", existing))
                    .build();
                    
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage()))
                    .build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        try {
            // Get logged in member from JWT
            Member member = (Member) request.getAttribute("member");
            
            // Get existing data
            Bukutamu existing = bukutamuEJB.findById(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("Data tidak ditemukan"))
                        .build();
            }
            
            // Only admin or owner can delete
            if (!member.getRole().equals("admin") && 
                !existing.getMember().getId().equals(member.getId())) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(ApiResponse.error("Anda tidak memiliki akses"))
                        .build();
            }
            
            // Delete file if exists
            if (existing.getGambar() != null) {
                FileUtil.deleteFile(existing.getGambar(), request.getServletContext().getRealPath("/"));
            }
            
            bukutamuEJB.delete(id);
            return Response.ok(ApiResponse.success("Buku tamu berhasil dihapus"))
                    .build();
                    
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage()))
                    .build();
        }
    }
} 