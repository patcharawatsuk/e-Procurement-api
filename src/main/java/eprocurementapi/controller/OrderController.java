package eprocurementapi.controller;

import eprocurementapi.dao.ServiceResult;
import eprocurementapi.dao.request.OrderRequest;
import eprocurementapi.exception.UnexpectedException;
import eprocurementapi.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/create")
    public ResponseEntity<ServiceResult> createOrder(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody List<OrderRequest> order) throws UnexpectedException {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(authHeader, order));
    }

    @GetMapping("/")
    public ResponseEntity<ServiceResult> getAllOrder(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(orderService.getAllOrder(authHeader));
    }

    @GetMapping
    public ResponseEntity<ServiceResult> getOrderDetail(@RequestParam("id") int orderId) throws UnexpectedException {
        return ResponseEntity.ok(orderService.getOrderDetail(orderId));
    }

    @GetMapping("/approval")
    public ResponseEntity<ServiceResult> getAllApproval(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(orderService.getAllApproval(authHeader));
    }

    @PutMapping("/cancel")
    public ResponseEntity<ServiceResult> cancelOrder(@RequestHeader("Authorization") String authHeader, @RequestParam("id") int orderId, @RequestBody String message) {
        return ResponseEntity.ok(orderService.cancelOrder(authHeader, orderId, message));
    }

    @PutMapping("/approve")
    public ResponseEntity<ServiceResult> approveOrder(@RequestHeader("Authorization") String authHeader, @RequestParam("id") int orderId) {
        return ResponseEntity.ok(orderService.approveOrder(authHeader, orderId));
    }

}
