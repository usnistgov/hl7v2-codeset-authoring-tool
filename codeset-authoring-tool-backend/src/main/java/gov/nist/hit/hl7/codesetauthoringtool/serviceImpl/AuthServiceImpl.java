package gov.nist.hit.hl7.codesetauthoringtool.serviceImpl;

import gov.nist.hit.hl7.codesetauthoringtool.model.ApplicationUser;
import gov.nist.hit.hl7.codesetauthoringtool.model.Codeset;
import gov.nist.hit.hl7.codesetauthoringtool.model.PasswordResetToken;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.AuthUser;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.JwtRequest;
import gov.nist.hit.hl7.codesetauthoringtool.model.response.ResponseMessage;
import gov.nist.hit.hl7.codesetauthoringtool.repository.PasswordResetTokenRepository;
import gov.nist.hit.hl7.codesetauthoringtool.security.JwtTokenUtil;
import gov.nist.hit.hl7.codesetauthoringtool.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private final JwtTokenUtil jwtTokenUtil;

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public AuthServiceImpl(JwtTokenUtil jwtTokenUtil, UserDetailsServiceImpl userDetailsService, AuthenticationManager authenticationManager, PasswordResetTokenRepository passwordResetTokenRepository) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    public AuthUser login(JwtRequest authenticationRequest, HttpServletResponse response) throws AuthenticationException, IOException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.GenerateToken(userDetails.getUsername());

        Cookie authCookie = new Cookie("codesetCookie", token);
        authCookie.setPath("/api");
        authCookie.setMaxAge(864000000 - 20); // 10 days

        response.setContentType("application/json");
        response.addCookie(authCookie);

        return new AuthUser(userDetails.getUsername(), true, new ArrayList<>());
    }

    public boolean resetPassword(String email, HttpServletResponse response) throws Exception {
        ApplicationUser user = userDetailsService.getUserByEmail(email);
        if (user == null) {
            throw new Exception("User not found");
        }
        String resetToken = UUID.randomUUID().toString();
        String hashedToken = jwtTokenUtil.GenerateToken(resetToken);
        Date date = new Date();
        date.setTime(date.getTime() + PasswordResetToken.getExpiration());
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(hashedToken);
        passwordResetToken.setEmail(user.getEmail());
        passwordResetToken.setExpiryDate(date);
        passwordResetTokenRepository.save(passwordResetToken);
        return true;
    }
}
