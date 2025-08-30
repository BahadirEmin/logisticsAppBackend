package com.baem.logisticapp.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

// @Component  // Kapalı çünkü interceptor kullanıyoruz
public class RequestResponseLoggingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Sadece API endpoint'lerini logla
        if (!httpRequest.getRequestURI().startsWith("/api/")) {
            chain.doFilter(request, response);
            return;
        }

        long startTime = System.currentTimeMillis();

        try {
            chain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            // Sadece endpoint, status ve süre
            log.info("{} {} - {} ({}ms)", 
                httpRequest.getMethod(), 
                httpRequest.getRequestURI(), 
                httpResponse.getStatus(),
                duration);
        }
    }
}
