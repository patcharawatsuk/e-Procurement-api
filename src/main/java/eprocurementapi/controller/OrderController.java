package eprocurementapi.controller;

import eprocurementapi.dao.ServiceResult;
import eprocurementapi.dao.request.OrderRequest;
import eprocurementapi.exception.UnexpectedException;
import eprocurementapi.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<ServiceResult> createOrder(@Valid @RequestBody List<OrderRequest> orders) throws UnexpectedException {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orders));
    }

    @GetMapping
    public ResponseEntity<ServiceResult> getOrder() {
        return ResponseEntity.ok(orderService.getAllOrder());
    }
}
