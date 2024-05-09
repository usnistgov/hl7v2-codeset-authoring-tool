package gov.nist.hit.hl7.codesetauthoringtool.repository;

import gov.nist.hit.hl7.codesetauthoringtool.model.Codeset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodesetRepository extends JpaRepository<Codeset, String>, JpaSpecificationExecutor<Codeset> {
    Optional<Codeset> findByAudience(String audience);

}
