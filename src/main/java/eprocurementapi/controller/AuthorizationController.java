package eprocurementapi.controller;

import eprocurementapi.dao.ServiceResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/resource")
public class AuthorizationController {
    @GetMapping
    public ResponseEntity<ServiceResult> accessResource() {
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.setStatus(HttpStatus.OK.value());
        serviceResult.createResponseData("Here is your resource");
        return ResponseEntity.ok(serviceResult);
    }
}
