package eprocurementapi.service;

import eprocurementapi.dao.ServiceResult;
import eprocurementapi.exception.UnexpectedException;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService userDetailsService();

    ServiceResult userDetail(String authHeader) throws UnexpectedException;
}
