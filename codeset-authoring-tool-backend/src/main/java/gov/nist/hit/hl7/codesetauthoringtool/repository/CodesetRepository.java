package gov.nist.hit.hl7.codesetauthoringtool.repository;

import gov.nist.hit.hl7.codesetauthoringtool.model.Codeset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodesetRepository extends JpaRepository<Codeset, String>, JpaSpecificationExecutor<Codeset> {
    Optional<Codeset> findById(String id);
    Optional<Codeset> findByName(String name);
    List<Codeset> findByOwnerUsername(String username);
    List<Codeset> findByNameContaining( String name);


}
