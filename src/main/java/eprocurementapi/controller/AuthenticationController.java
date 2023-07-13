package eprocurementapi.controller;

import eprocurementapi.dao.ServiceResult;
import eprocurementapi.dao.request.SignUpRequest;
import eprocurementapi.dao.request.SigninRequest;
import eprocurementapi.dao.response.JwtAuthenticationResponse;
import eprocurementapi.entities.Role;
import eprocurementapi.exception.AuthenException;
import eprocurementapi.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<ServiceResult> signup(@Valid @RequestBody SignUpRequest request, BindingResult bindingResult) throws AuthenException {
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                for (FieldError error : bindingResult.getFieldErrors()) {
                    errors.put(error.getField(), error.getDefaultMessage());
                }
                throw AuthenException.createUserFail(HttpStatus.BAD_REQUEST.value(), errors);
            }

            if (!isValidRole(request.getRole())) {
                throw AuthenException.createUserFail(HttpStatus.BAD_REQUEST.value(), "Invalid role.");
            }
            // Perform signup logic if the request is valid
            return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.signup(request));
        } catch (Exception ex) {
            throw AuthenException.createUserFail(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        }
    }
    private boolean isValidRole(Role role) {
        // Check if the role exists in the defined enum values
        for (Role validRole : Role.values()) {
            if (validRole.equals(role)) {
                return true;
            }
        }
        return false;
    }

    @PostMapping("/signin")
    public ResponseEntity<ServiceResult> signin(@RequestBody SigninRequest request) throws AuthenException {
        return ResponseEntity.ok(authenticationService.signin(request));
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<ServiceResult> refreshToken(@RequestHeader("Authorization") String authHeader) throws AuthenException {
        return ResponseEntity.ok(authenticationService.refreshToken(authHeader));
    }
}
