package eprocurementapi.controller;

import eprocurementapi.dao.ServiceResult;
import eprocurementapi.dao.request.UserRequest;
import eprocurementapi.exception.UnexpectedException;
import eprocurementapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ServiceResult> userDetail(@RequestHeader("Authorization") String authHeader) throws UnexpectedException {
        return ResponseEntity.ok(userService.userDetail(authHeader));
    }
}
