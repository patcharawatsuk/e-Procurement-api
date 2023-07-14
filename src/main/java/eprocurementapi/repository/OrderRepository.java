package eprocurementapi.repository;

import eprocurementapi.entities.AppovalOrderInfo;
import eprocurementapi.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByRequester(String email);
    Optional<Order> findByRequesterAndId(String email, int id);

    List<Order> findByIdIn(List<Integer> orderId);

    List<Order> findByRequesterOrderByIdAsc(String email);
}