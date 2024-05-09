package gov.nist.hit.hl7.codesetauthoringtool.controller;

import gov.nist.hit.hl7.codesetauthoringtool.model.request.JwtRequest;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.JwtResponse;
import gov.nist.hit.hl7.codesetauthoringtool.security.JwtTokenUtil;
import gov.nist.hit.hl7.codesetauthoringtool.serviceImpl.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/v1")
public class AuthController {
    private final JwtTokenUtil jwtTokenUtil;

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;

    public AuthController(JwtTokenUtil jwtTokenUtil, UserDetailsServiceImpl userDetailsService, AuthenticationManager authenticationManager) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }
    @GetMapping("/")
    public String hello() {
        return "Hello";
    }

    @RequestMapping(value = "/login" , produces = "application/json",
            method = RequestMethod.POST)
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authenticationRequest) throws Exception {


        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

            final String token = jwtTokenUtil.GenerateToken(userDetails.getUsername());
            return ResponseEntity.ok(new JwtResponse(token));


    }


}
