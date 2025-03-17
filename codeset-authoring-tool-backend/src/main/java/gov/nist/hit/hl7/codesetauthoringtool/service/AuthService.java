package gov.nist.hit.hl7.codesetauthoringtool.service;

import gov.nist.hit.hl7.codesetauthoringtool.model.ApplicationUser;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.AuthUser;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.JwtRequest;
import gov.nist.hit.hl7.codesetauthoringtool.model.response.ResponseMessage;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.util.List;

public interface AuthService {

    public AuthUser login(JwtRequest authenticationRequest, HttpServletResponse response) throws AuthenticationException, IOException;

    public boolean forgotPassword(String email, HttpServletResponse response) throws Exception;
    public boolean resetPassword(String token, String password) throws Exception;

}
