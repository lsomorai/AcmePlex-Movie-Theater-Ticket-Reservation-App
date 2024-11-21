package com.example.movieticket.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        
        // Allow access to login, register, and public pages
        if (path.startsWith("/css") || 
            path.startsWith("/js") || 
            path.equals("/") || 
            path.equals("/login") || 
            path.equals("/register") || 
            path.equals("/signup") ||
            path.equals("/theatres")) {  // Allow guest access to theatres
            return true;
        }

        // Check if user is logged in
        HttpSession session = request.getSession();
        if (session.getAttribute("username") == null) {
            response.sendRedirect("/");
            return false;
        }
        
        return true;
    }
} 