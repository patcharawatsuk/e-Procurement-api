package eprocurementapi.repository;

import eprocurementapi.entities.OrderItem;
import eprocurementapi.entities.OrderItemPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {
    List<OrderItem> findByIdOrderId(int orderId);
}