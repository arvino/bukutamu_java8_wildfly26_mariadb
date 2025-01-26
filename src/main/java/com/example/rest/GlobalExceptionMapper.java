package com.example.rest;

import com.example.exception.BusinessException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import io.jsonwebtoken.JwtException;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    
    @Override
    public Response toResponse(Throwable exception) {
        // Business exception
        if (exception instanceof BusinessException) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(exception.getMessage()))
                    .build();
        }
        
        // JWT/Auth exception
        if (exception instanceof JwtException) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(ApiResponse.error("Token tidak valid: " + exception.getMessage()))
                    .build();
        }
        
        // Validation exception
        if (exception instanceof IllegalArgumentException) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(exception.getMessage()))
                    .build();
        }
        
        // Unknown error
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Terjadi kesalahan sistem"))
                .build();
    }
} 