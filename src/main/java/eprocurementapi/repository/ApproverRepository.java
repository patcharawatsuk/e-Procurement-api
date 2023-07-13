package eprocurementapi.repository;

import eprocurementapi.entities.Approver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApproverRepository extends JpaRepository<Approver, String> {
}
