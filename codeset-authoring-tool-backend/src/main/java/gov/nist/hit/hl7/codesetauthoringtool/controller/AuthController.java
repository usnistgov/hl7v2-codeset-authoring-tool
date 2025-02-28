package gov.nist.hit.hl7.codesetauthoringtool.controller;

import gov.nist.hit.hl7.codesetauthoringtool.model.request.AuthUser;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.JwtRequest;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.ResetPasswordRequest;
import gov.nist.hit.hl7.codesetauthoringtool.model.response.ResponseMessage;
import gov.nist.hit.hl7.codesetauthoringtool.service.AuthService;
import gov.nist.hit.hl7.codesetauthoringtool.serviceImpl.AuthServiceImpl;
import gov.nist.hit.hl7.codesetauthoringtool.serviceImpl.UserDetailsServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/auth/v1")
public class AuthController {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    public AuthController( UserDetailsServiceImpl userDetailsService,
                          AuthenticationManager authenticationManager, AuthServiceImpl authService) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }

    @RequestMapping(value = "/login", produces = "application/json",
            method = RequestMethod.POST)
    public ResponseMessage<?> login(@RequestBody JwtRequest authenticationRequest, HttpServletResponse response) throws Exception {
        AuthUser user = authService.login(authenticationRequest, response);
        return new ResponseMessage<>(ResponseMessage.Status.SUCCESS, "Logged in Successfully", null, null, user);

    }


    @RequestMapping(value = "/logout", produces = "application/json",
            method = RequestMethod.POST)
    public void logout(HttpServletResponse response) throws Exception {
        Cookie authCookie = new Cookie("codesetCookie", "");
        authCookie.setPath("/api");
        authCookie.setMaxAge(0);
        response.addCookie(authCookie);
    }

    @RequestMapping(value = "/status", produces = "application/json",
            method = RequestMethod.GET)
    public ResponseEntity<AuthUser> checkAuthStatus(@AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response) throws Exception {
        try {
            return ResponseEntity.ok(new AuthUser(userDetails.getUsername(), true, new ArrayList<>()));
        } catch (Exception e) {
            e.printStackTrace();
            Cookie authCookie = new Cookie("codesetCookie", "");
            authCookie.setPath("/api");
            authCookie.setMaxAge(0);
            response.addCookie(authCookie);
            response.sendError(403);
            throw e;
        }
    }

    @RequestMapping(value = "/reset-password", produces = "application/json",
            method = RequestMethod.POST)
    public ResponseMessage<?> resetPassword(@RequestBody ResetPasswordRequest request, HttpServletResponse response) throws Exception {
        authService.resetPassword(request.getEmail(), response);
        return new ResponseMessage<>(ResponseMessage.Status.SUCCESS, "Password reset link sent to email", null, null, null);
    }

    }
