package eprocurementapi.dao.request;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderRequestTest {
    @Order(1)
    @Test
    void testCreateOrder() {
        OrderRequest req = OrderRequest.builder()
                .productId(1)
                .qty(Double.valueOf(100))
                .price(Double.valueOf(2))
                .build();
        System.out.println("total " + req.getPrice() * req.getQty());
    }
}