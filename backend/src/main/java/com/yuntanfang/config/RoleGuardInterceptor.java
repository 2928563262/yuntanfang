package com.yuntanfang.config;

import com.yuntanfang.security.AuthSupport;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RoleGuardInterceptor implements HandlerInterceptor {

    private final AuthSupport authSupport;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();
        String authorization = request.getHeader("Authorization");
        if (path.startsWith("/api/admin/")) {
            authSupport.requireAnyRole(authorization, "admin", "auditor", "supervisor", "system_admin");
            return true;
        }
        if (path.startsWith("/api/vendor/")) {
            authSupport.requireAnyRole(authorization, "vendor");
            return true;
        }
        if (path.startsWith("/api/orders/")
                || path.equals("/api/orders")
                || path.startsWith("/api/complaints/")
                || path.equals("/api/complaints")
                || path.startsWith("/api/reviews/")
                || path.startsWith("/api/follows")
                || path.startsWith("/api/favorites")
                || path.startsWith("/api/subscriptions")) {
            authSupport.requireAnyRole(authorization, "consumer");
            return true;
        }
        if (path.startsWith("/api/messages/")) {
            authSupport.requireAnyRole(authorization, "consumer", "vendor", "admin", "auditor", "supervisor", "system_admin");
        }
        return true;
    }
}
