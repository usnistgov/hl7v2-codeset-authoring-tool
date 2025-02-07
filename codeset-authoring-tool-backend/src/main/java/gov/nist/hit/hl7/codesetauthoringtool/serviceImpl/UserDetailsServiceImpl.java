package gov.nist.hit.hl7.codesetauthoringtool.serviceImpl;

import gov.nist.hit.hl7.codesetauthoringtool.dto.CodesetDTO;
import gov.nist.hit.hl7.codesetauthoringtool.exception.NotFoundException;
import gov.nist.hit.hl7.codesetauthoringtool.model.ApiKey;
import gov.nist.hit.hl7.codesetauthoringtool.model.ApplicationUser;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.UserRequest;
import gov.nist.hit.hl7.codesetauthoringtool.repository.UserRepository;
import gov.nist.hit.hl7.codesetauthoringtool.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, UserService {
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserDetailsServiceImpl(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ApplicationUser> userOptional = userRepository.findByUsername(username);

        // Use orElseThrow to handle the case where the user is not found
        ApplicationUser user = userOptional.orElseThrow(() -> new UsernameNotFoundException(username));

        // Return a UserDetails object
        return new User(user.getUsername(), user.getPassword(), emptyList());
    }

    @Override
    public List<ApplicationUser> getUsers() throws IOException {

        List<ApplicationUser> users = userRepository.findAll();
        return users.stream()
                .collect(Collectors.toList());
    }
    @Override
    public ApplicationUser getUser(String id) throws IOException, NotFoundException {

        ApplicationUser user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User not found")
        );
        return user;
    }

    @Override
    public ApplicationUser createUser(UserRequest newUser) throws IOException {
        try {
            if (userRepository.findByUsername(newUser.getUsername()).isPresent()) {
                throw new IOException("Username " + newUser.getUsername() + " already exists. Please use a different one.");
            }
            String hashedPassword = passwordEncoder.encode(newUser.getPassword());
            ApplicationUser user = new ApplicationUser();
            user.setUsername(newUser.getUsername());
            user.setPassword(hashedPassword);
            user.setFirstName(newUser.getFirstName());
            user.setLastName(newUser.getLastName());
            userRepository.save(user);

            return user;
        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
            throw new IOException("Error creating user");
        }

    }
    @Override
    public ApplicationUser editUser(String id, UserRequest userRequest) throws IOException {
        try {
            ApplicationUser existingUser = this.userRepository.findById(id).orElseThrow(() -> new IOException("User not found."));
            if(userRequest.getPassword() != null){
                String hashedPassword = passwordEncoder.encode(userRequest.getPassword());
                existingUser.setPassword(hashedPassword);
            }
            existingUser.setUsername(userRequest.getUsername());
            existingUser.setFirstName(userRequest.getFirstName());
            existingUser.setLastName(userRequest.getLastName());
            userRepository.save(existingUser);
            return existingUser;
        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
            throw new IOException("Error creating user");
        }

    }
    public void deleteUser(String id) throws IOException {
        ApplicationUser user = this.userRepository.findById(id).orElseThrow(() -> new IOException("User not found."));
        this.userRepository.delete(user);
    }


}
