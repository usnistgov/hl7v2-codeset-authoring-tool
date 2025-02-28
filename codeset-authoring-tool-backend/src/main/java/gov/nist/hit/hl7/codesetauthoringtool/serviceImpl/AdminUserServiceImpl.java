package gov.nist.hit.hl7.codesetauthoringtool.serviceImpl;

import gov.nist.hit.hl7.codesetauthoringtool.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

import javax.annotation.PostConstruct;

@Service
@Transactional
public class AdminUserServiceImpl implements AdminUserService {
    @Value("${adminDefaultUsername}")
    private String adminDefaultUsername;
    @Value("${adminDefaultPassowrd}")
    private String adminDefaultPassword;
    @Value("${adminDefaultEmail}")
    private String adminDefaultEmail;
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    public AdminUserServiceImpl(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }
    @PostConstruct
    public void createAdminUser() {
        int existingUsers = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
        if (existingUsers == 0) {
            String hashedPassword = passwordEncoder.encode(adminDefaultPassword);
            String id = UUID.randomUUID().toString().replace("-", "");
            System.out.println("Creating admin user with id: " + id);
            jdbcTemplate.update("INSERT INTO users (id, username, email, password) VALUES (?,?, ?, ?)",id, adminDefaultUsername, adminDefaultEmail, hashedPassword);
            System.out.println("Admin user created successfully.");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}
