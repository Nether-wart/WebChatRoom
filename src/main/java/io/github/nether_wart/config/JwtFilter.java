package io.github.nether_wart.config;

import io.github.nether_wart.service.CustomUserDetailsService;
import io.github.nether_wart.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@Component
public class JwtFilter extends OncePerRequestFilter {


    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;


    public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        Long id = null;
        if (header != null && header.startsWith("Bearer ")) {
            id = jwtUtil.extractId(header.substring(7));
        }
        if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadByUid(id);
            SecurityContextHolder.getContext().setAuthentication(new Authentication() {
                @NonNull
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return userDetails.getAuthorities();
                }

                @Override
                public @Nullable Object getCredentials() {
                    return null;
                }

                @Override
                public @Nullable Object getDetails() {
                    return userDetails;
                }

                @Override
                public @Nullable Object getPrincipal() {
                    return null;
                }

                @Override
                public boolean isAuthenticated() {
                    return true;
                }

                @Override
                public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

                }

                @Override
                public String getName() {
                    return "";
                }
            });
        }
        filterChain.doFilter(request, response);
    }
}
