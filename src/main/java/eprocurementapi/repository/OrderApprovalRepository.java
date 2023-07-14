package eprocurementapi.repository;

import eprocurementapi.entities.OrderApproval;
import eprocurementapi.entities.OrderApprovalPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderApprovalRepository extends JpaRepository<OrderApproval, OrderApprovalPK> {
    @Query(value = " SELECT e.step FROM _order_approval e where e.order_id = :orderId ORDER BY e.step desc limit 1 ", nativeQuery = true)
    Integer findLastApproveStep(int orderId);
}
