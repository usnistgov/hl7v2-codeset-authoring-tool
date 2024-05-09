package gov.nist.hit.hl7.codesetauthoringtool.repository;

import gov.nist.hit.hl7.codesetauthoringtool.model.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApiKeyRepository extends JpaRepository<ApiKey, String> {

}
