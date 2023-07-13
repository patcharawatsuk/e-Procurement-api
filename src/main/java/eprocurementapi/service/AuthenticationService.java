package eprocurementapi.service;

import eprocurementapi.dao.ServiceResult;
import eprocurementapi.dao.request.SignUpRequest;
import eprocurementapi.dao.request.SigninRequest;
import eprocurementapi.exception.AuthenException;

public interface AuthenticationService {
    ServiceResult signup(SignUpRequest request) throws AuthenException;

    ServiceResult signin(SigninRequest request) throws AuthenException;

    ServiceResult refreshToken(String authHeader) throws AuthenException;
}
