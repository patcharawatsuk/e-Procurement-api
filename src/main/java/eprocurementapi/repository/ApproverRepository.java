package eprocurementapi.repository;

import eprocurementapi.entities.Approver;
import eprocurementapi.entities.ApproverLevel;
import eprocurementapi.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApproverRepository extends JpaRepository<Approver, String> {
    Optional<Approver> findByLevel(ApproverLevel level);
}
