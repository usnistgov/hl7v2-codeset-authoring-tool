package gov.nist.hit.hl7.codesetauthoringtool.repository;

import gov.nist.hit.hl7.codesetauthoringtool.model.Code;
import gov.nist.hit.hl7.codesetauthoringtool.model.Codeset;
import gov.nist.hit.hl7.codesetauthoringtool.model.CodesetVersion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodesetVersionRepository extends JpaRepository<CodesetVersion, String>, JpaSpecificationExecutor<CodesetVersion> {
    Optional<CodesetVersion> findByCodesetIdAndId(String codesetId, String versionId);
    Optional<List<CodesetVersion>> findByCodesetId(String codesetId);

    @EntityGraph(attributePaths = {"codes"})
    Optional<CodesetVersion> findWithCodesById(String id);

//    @Query("SELECT c FROM Code c WHERE c.codesetVersion.id = :codesetVersionId AND (:match IS NULL OR c.code = :match)")
//@Query(value = """
//    SELECT c
//    FROM "codes" c
//    WHERE c.codeset_version.id = :codesetVersionId
//      AND (
//        (:match IS NULL OR c.code = :match)
//        OR (c.hasPattern = true AND FUNCTION('REGEXP', c.pattern, COALESCE(:match, '')) = 1)
//      )
//""", nativeQuery = true)
//    List<Code> findCodesByVersionIdAndMatch(@Param("codesetVersionId") String codesetVersionId, @Param("match") String match);
@Query(value = """
    SELECT c.id, c.code, c.has_pattern, c.pattern, c.system, c.usage, c.display
    FROM "codes" c 
    JOIN "codeset-versions" cv ON c.codeset_version_id = cv.id
    WHERE cv.id = :codesetVersionId 
      AND (
        (:match IS NULL OR c.code = :match) 
        OR (c.has_pattern = true AND c.pattern ~ COALESCE(:match, ''))
      )
""", nativeQuery = true)
List<Object[]> findCodesByVersionIdAndMatch(@Param("codesetVersionId") String codesetVersionId, @Param("match") String match);

}
