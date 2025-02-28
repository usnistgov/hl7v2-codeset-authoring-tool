package gov.nist.hit.hl7.codesetauthoringtool.service;

import gov.nist.hit.hl7.codesetauthoringtool.exception.NotFoundException;
import gov.nist.hit.hl7.codesetauthoringtool.model.ApplicationUser;
import gov.nist.hit.hl7.codesetauthoringtool.model.request.UserRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.List;

public interface UserService {
    public List<ApplicationUser> getUsers() throws IOException;
    public ApplicationUser getUser(String id) throws IOException, NotFoundException;
    public ApplicationUser createUser(UserRequest newUser) throws IOException;
    public ApplicationUser editUser(String id, UserRequest user) throws IOException;
    public void deleteUser(String id) throws IOException;
    public ApplicationUser getUserByEmail(String email) throws NotFoundException;

}
