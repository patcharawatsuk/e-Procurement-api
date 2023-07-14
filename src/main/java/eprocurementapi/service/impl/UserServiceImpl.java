package eprocurementapi.service.impl;

import eprocurementapi.dao.ServiceResult;
import eprocurementapi.dao.request.UserRequest;
import eprocurementapi.dao.response.UserResponse;
import eprocurementapi.entities.User;
import eprocurementapi.exception.UnexpectedException;
import eprocurementapi.repository.UserRepository;
import eprocurementapi.service.AuthenticationService;
import eprocurementapi.service.JwtService;
import eprocurementapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    @Override
    public ServiceResult userDetail(String authHeader) {
        String jwt = authHeader.substring(7);
        String email = jwtService.extractUserName(jwt);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        UserResponse userResponse = UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.createResponseData(userResponse);
        return serviceResult;
    }
}
