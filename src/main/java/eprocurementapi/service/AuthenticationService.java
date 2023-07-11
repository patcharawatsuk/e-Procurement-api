package eprocurementapi.service;

import eprocurementapi.dao.request.SignUpRequest;
import eprocurementapi.dao.request.SigninRequest;
import eprocurementapi.dao.response.JwtAuthenticationResponse;
import eprocurementapi.exception.AuthenException;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request) throws AuthenException;
}
