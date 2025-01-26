package com.example.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class RestActivator extends Application {
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        
        // Add REST resources
        resources.add(AuthResource.class);
        resources.add(MemberResource.class);
        resources.add(BukutamuResource.class);
        
        // Add providers
        resources.add(CorsFilter.class);
        resources.add(AuthFilter.class);
        resources.add(GlobalExceptionMapper.class);
        
        return resources;
    }
} 