package com.example.rest;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.container.PreMatching;
import io.jsonwebtoken.Claims;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {
    
    private static final String AUTH_PATH = "auth";
    
    @Override
    public void filter(ContainerRequestContext requestContext) {
        String path = requestContext.getUriInfo().getPath();
        
        // Skip authentication for login and register
        if (path.contains(AUTH_PATH)) {
            return;
        }
        
        // Get token from header
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            abortWithUnauthorized(requestContext);
            return;
        }
        
        // Validate token
        String token = authHeader.substring(7);
        try {
            Claims claims = JwtUtil.validateToken(token);
            // Add claims to request context for later use
            requestContext.setProperty("claims", claims);
        } catch (Exception e) {
            abortWithUnauthorized(requestContext);
        }
    }
    
    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(
            Response.status(Response.Status.UNAUTHORIZED)
                    .entity(ApiResponse.error("Token tidak valid atau expired"))
                    .build());
    }
} 