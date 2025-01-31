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
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    public AdminUserServiceImpl(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }
    @PostConstruct
    public void createAdminUser() {
        // Check for existing users
        int existingUsers = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
        if (existingUsers == 0) {
            String hashedPassword = passwordEncoder.encode(adminDefaultPassword);
            String id = UUID.randomUUID().toString().replace("-", "");
            // Insert admin user
            jdbcTemplate.update("INSERT INTO users (id, username, password) VALUES (?,?, ?)",id, adminDefaultUsername, hashedPassword);
            System.out.println("Admin user created successfully.");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}
