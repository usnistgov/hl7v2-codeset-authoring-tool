package gov.nist.hit.hl7.codesetauthoringtool.repository;

import gov.nist.hit.hl7.codesetauthoringtool.model.Codeset;
import gov.nist.hit.hl7.codesetauthoringtool.model.CodesetVersion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodesetVersionRepository extends JpaRepository<CodesetVersion, String>, JpaSpecificationExecutor<CodesetVersion> {
    Optional<CodesetVersion> findByCodesetIdAndId(String codesetId, String versionId);
    Optional<List<CodesetVersion>> findByCodesetId(String codesetId);

    @EntityGraph(attributePaths = {"codes"})
    Optional<CodesetVersion> findWithCodesById(String id);
}
