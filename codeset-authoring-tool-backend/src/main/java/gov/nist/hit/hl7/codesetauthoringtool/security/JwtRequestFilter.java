package gov.nist.hit.hl7.codesetauthoringtool.security;


import gov.nist.hit.hl7.codesetauthoringtool.serviceImpl.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtService;

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    public JwtRequestFilter(JwtTokenUtil jwtService, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.jwtService = jwtService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {

            Cookie cookie = WebUtils.getCookie(request, "codesetCookie");

            String token = null;
            String username = null;
            if (cookie != null && cookie.getValue() != null && !cookie.getValue().isEmpty()) {
                token = cookie.getValue();
                username = jwtService.extractUsername(token);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }

            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
                | IllegalArgumentException e
        ) {
            e.printStackTrace();
            Cookie authCookie = new Cookie("codesetCookie", "");
            authCookie.setPath("/api");
            authCookie.setMaxAge(0);
            response.addCookie(authCookie);
            response.sendError(403);
        }
    }


}
