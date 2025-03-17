package gov.nist.hit.hl7.codesetauthoringtool.serviceImpl;

import gov.nist.hit.hl7.codesetauthoringtool.model.ApplicationUser;
import gov.nist.hit.hl7.codesetauthoringtool.model.Codeset;
import gov.nist.hit.hl7.codesetauthoringtool.model.PasswordResetToken;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.AuthUser;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.JwtRequest;
import gov.nist.hit.hl7.codesetauthoringtool.model.response.ResponseMessage;
import gov.nist.hit.hl7.codesetauthoringtool.repository.PasswordResetTokenRepository;
import gov.nist.hit.hl7.codesetauthoringtool.repository.UserRepository;
import gov.nist.hit.hl7.codesetauthoringtool.security.JwtTokenUtil;
import gov.nist.hit.hl7.codesetauthoringtool.service.AuthService;
import gov.nist.hit.hl7.codesetauthoringtool.service.EmailService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    @Value("${frontendUrl}")
    private String frontendUrl;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;


    public AuthServiceImpl(JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder, UserDetailsServiceImpl userDetailsService, AuthenticationManager authenticationManager, PasswordResetTokenRepository passwordResetTokenRepository, UserRepository userRepository, EmailService emailService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
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

    public boolean forgotPassword(String email, HttpServletResponse response) throws Exception {
        ApplicationUser user = userDetailsService.getUserByEmail(email);
        if (user == null) {
            // We don't show that user doesn't exist because it can be exploited
            return true;
        }
        String resetToken = UUID.randomUUID().toString();
        String hashedToken = jwtTokenUtil.GenerateToken(resetToken);
        Date date = new Date();
        date.setTime(date.getTime() + PasswordResetToken.getExpiration());
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(hashedToken);
        passwordResetToken.setEmail(user.getEmail());
        passwordResetToken.setExpiryDate(date);
        String url = frontendUrl + "/reset-password?token=" + hashedToken;
//        sendResetTokenUrl(user.getFirstName(), user.getUsername(), user.getEmail(), resetToken);
        String text = "Dear " + user.getFirstName() + " \n\n"
                + "**** If you have not requested a password reset, please disregard this email **** \n\n\n"
                + "You password reset request has been processed.\n"
                + "Copy and paste the following url to your browser to initiate the password change:\n"
                + url + " \n\n" + "Sincerely, " + "\n\n" + "The IGAMT Team" + "\n\n"
                + "P.S: If you need help, contact us at '" + "'";
        emailService.sendEmail(user.getEmail(), "Password Reset Request", text);
        System.out.println(hashedToken);
        passwordResetTokenRepository.save(passwordResetToken);
        return true;
    }
    public boolean resetPassword(String token, String password) throws Exception {
        System.out.println(token);
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new Exception("Invalid or expired token"));

        if (passwordResetToken.isExpired()) {
            throw new Exception("Token has expired");
        }
        ApplicationUser user = userDetailsService.getUserByEmail(passwordResetToken.getEmail());
        if (user == null) {
            throw new Exception("User not found");
        }

        // Update password
        String hashedPassword = passwordEncoder.encode(password);
        user.setPassword(hashedPassword);
        this.userRepository.save(user);

        passwordResetTokenRepository.delete(passwordResetToken);
        return true;
    }


}
