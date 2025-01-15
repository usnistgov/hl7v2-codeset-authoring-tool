package gov.nist.hit.hl7.codesetauthoringtool.repository;

import gov.nist.hit.hl7.codesetauthoringtool.model.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey, String> {

    Optional<ApiKey> findByToken(String hashedKey);
}
