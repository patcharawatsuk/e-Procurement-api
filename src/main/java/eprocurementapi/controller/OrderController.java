package eprocurementapi.controller;

import eprocurementapi.dao.ServiceResult;
import eprocurementapi.dao.request.OrderRequest;
import eprocurementapi.exception.UnexpectedException;
import eprocurementapi.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<ServiceResult> createOrder(@Valid @RequestBody List<OrderRequest> orders) throws UnexpectedException {
        return ResponseEntity.ok(orderService.createOrder(orders));
    }

    @PostMapping("/")
    public ResponseEntity<ServiceResult> getOrder() {
        return ResponseEntity.ok(orderService.getAllOrder());
    }
}
