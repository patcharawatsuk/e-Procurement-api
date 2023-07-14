package eprocurementapi.service.impl;

import eprocurementapi.dao.ServiceResult;
import eprocurementapi.dao.request.SignUpRequest;
import eprocurementapi.dao.request.SigninRequest;
import eprocurementapi.dao.response.JwtAuthenticationResponse;
import eprocurementapi.entities.Approver;
import eprocurementapi.entities.Role;
import eprocurementapi.entities.User;
import eprocurementapi.exception.AuthenException;
import eprocurementapi.repository.ApproverRepository;
import eprocurementapi.repository.UserRepository;
import eprocurementapi.service.AuthenticationService;
import eprocurementapi.service.JwtService;
import eprocurementapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final ApproverRepository approverRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public ServiceResult signup(SignUpRequest request) throws AuthenException {
        Optional<User> findUser = userRepository.findByEmail(request.getEmail());
        if(findUser.isPresent()) {
            throw AuthenException.emailTaken();
        }

        try {
            var user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole()).build();
            userRepository.save(user);

            if (request.getRole().equals(Role.APPROVER)) {
                var approver = Approver.builder()
                        .email(request.getEmail())
                        .level(request.getSpendinglimit())
                        .build();
                approverRepository.save(approver);
            }

            var access_token = jwtService.generateAccessToken(user);
            var refresh_token = jwtService.generateRefreshToken(user);

            JwtAuthenticationResponse jwt = JwtAuthenticationResponse.builder()
                    .access_token(access_token)
                    .refresh_token(refresh_token)
                    .build();

            ServiceResult serviceResult = new ServiceResult();
            serviceResult.createResponseData(jwt);
            serviceResult.setStatus(HttpStatus.CREATED.value());
            return serviceResult;
        } catch (Exception ex) {
            throw AuthenException.createUserFail();
        }
    }

    @Override
    public ServiceResult signin(SigninRequest request) throws AuthenException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            var user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
            var access_token = jwtService.generateAccessToken(user);
            var refresh_token = jwtService.generateRefreshToken(user);

            JwtAuthenticationResponse jwt = JwtAuthenticationResponse.builder()
                    .access_token(access_token)
                    .refresh_token(refresh_token)
                    .build();

            ServiceResult serviceResult = new ServiceResult();
            serviceResult.createResponseData(jwt);
            return serviceResult;
        } catch (Exception ex) {
            throw AuthenException.loginFail("Invalid email or password.");
        }
    }

    @Override
    public ServiceResult refreshToken(String authHeader) throws AuthenException {
        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractUserName(jwt);
        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var access_token = jwtService.generateAccessToken(user);
        var refresh_token = jwtService.generateRefreshToken(user);

        JwtAuthenticationResponse newJwt = JwtAuthenticationResponse.builder()
                .access_token(access_token)
                .refresh_token(refresh_token)
                .build();
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.createResponseData(newJwt);
        return serviceResult;
    }
}
