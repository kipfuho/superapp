package com.superapp.booking_service.security;

import java.io.IOException;
import java.net.URI;
import java.util.regex.Pattern;

import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ClusterSecretFilter extends OncePerRequestFilter {

    private final String headerName = "X-Cluster-Secret";
    private final String expectedSecret;
    // match exactly: /queue/<anything-without-/ >/capacity
    private static final Pattern ADMIN_PATH = Pattern.compile("^/queue/[^/]+/admin/.*$");

    public ClusterSecretFilter(String expectedSecret) {
        this.expectedSecret = expectedSecret;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getRequestURI();
        // Filter only /queue/{eventId}/admin/**
        return path == null || !ADMIN_PATH.matcher(path).matches();
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String host = URI.create(request.getRequestURL().toString()).getHost();
        if (host == "localhost") {
            filterChain.doFilter(request, response);
            return;
        }

        String provided = request.getHeader(headerName);
        if (provided == null || !provided.equals(expectedSecret)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }
        filterChain.doFilter(request, response);
    }
}