package com.secureexam.backend.security.jwt;

import com.secureexam.backend.auth.jwt.JwtUtil;
import com.secureexam.backend.user.User;
import com.secureexam.backend.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtAuthFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Skip JWT authentication for public endpoints
        String requestPath = request.getRequestURI();
        if (requestPath.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            System.out.println("JwtAuthFilter: No Bearer token found in header");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = header.substring(7);
            String email = jwtUtil.extractEmail(token);
            System.out.println("JwtAuthFilter: Extracted email: " + email);

            User user = userRepository.findByEmail(email).orElse(null);

            if (user != null) {
                System.out.println("JwtAuthFilter: User found: " + user.getEmail());
                System.out.println("JwtAuthFilter: User roles: " + user.getRoles().size());

                var authorities = user.getRoles().stream()
                        .map(role -> {
                            String name = role.getName();
                            if (!name.startsWith("ROLE_")) {
                                name = "ROLE_" + name;
                            }
                            return new org.springframework.security.core.authority.SimpleGrantedAuthority(name);
                        })
                        .toList();

                System.out.println("JwtAuthFilter: Authorities: " + authorities);

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        user, null, authorities);

                auth.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("JwtAuthFilter: Authentication set in context");
            } else {
                System.out.println("JwtAuthFilter: User not found for email: " + email);
            }
        } catch (Exception e) {
            System.out.println("JwtAuthFilter: Error validating token: " + e.getMessage());
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }
}