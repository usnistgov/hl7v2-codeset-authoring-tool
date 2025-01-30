package gov.nist.hit.hl7.codesetauthoringtool.controller;

import gov.nist.hit.hl7.codesetauthoringtool.dto.CodesetDTO;
import gov.nist.hit.hl7.codesetauthoringtool.model.ApplicationUser;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.AuthUser;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.CodesetSearchCriteria;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.JwtRequest;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.NewUserRequest;
import gov.nist.hit.hl7.codesetauthoringtool.model.response.ResponseMessage;
import gov.nist.hit.hl7.codesetauthoringtool.security.JwtTokenUtil;
import gov.nist.hit.hl7.codesetauthoringtool.service.AuthService;
import gov.nist.hit.hl7.codesetauthoringtool.serviceImpl.AuthServiceImpl;
import gov.nist.hit.hl7.codesetauthoringtool.serviceImpl.UserDetailsServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final JwtTokenUtil jwtTokenUtil;

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    public UserController(JwtTokenUtil jwtTokenUtil, UserDetailsServiceImpl userDetailsService,
                          AuthenticationManager authenticationManager, AuthServiceImpl authService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }

    @RequestMapping(value = "", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<List<ApplicationUser>> getUsers() throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<ApplicationUser> users = userDetailsService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @RequestMapping(value = "", produces = "application/json",
            method = RequestMethod.POST)
    public ResponseMessage<?> createNewUser(@RequestBody NewUserRequest newUserRequest) throws Exception {
        ApplicationUser newUser = userDetailsService.createUser(newUserRequest);
        return new ResponseMessage<>(ResponseMessage.Status.SUCCESS, "User created Successfully", newUser.getId().toString(), null, newUser);

    }

    @RequestMapping(value = "/{id}", produces = "application/json", method = RequestMethod.DELETE)
    public ResponseMessage<Void> deleteUser(@PathVariable String id) throws IOException {
        userDetailsService.deleteUser(id);
        return new ResponseMessage<>(ResponseMessage.Status.SUCCESS, "User successfully deleted");
    }




}
