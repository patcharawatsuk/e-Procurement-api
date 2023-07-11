package eprocurementapi.service.impl;

import eprocurementapi.dao.request.SignUpRequest;
import eprocurementapi.dao.request.SigninRequest;
import eprocurementapi.dao.response.JwtAuthenticationResponse;
import eprocurementapi.entities.Role;
import eprocurementapi.entities.User;
import eprocurementapi.exception.AuthenException;
import eprocurementapi.repository.UserRepository;
import eprocurementapi.service.AuthenticationService;
import eprocurementapi.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER).build();
        userRepository.save(user);
        var access_token = jwtService.generateAccessToken(user);
        var refresh_token = jwtService.generateRefreshToken(user);
        return JwtAuthenticationResponse.builder()
                .access_token(access_token)
                .refresh_token(refresh_token)
                .build();
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) throws AuthenException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            var user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
            var access_token = jwtService.generateAccessToken(user);
            var refresh_token = jwtService.generateRefreshToken(user);

            return JwtAuthenticationResponse.builder()
                    .access_token(access_token)
                    .refresh_token(refresh_token)
                    .build();
        } catch (Exception ex) {
            throw AuthenException.loginFail("Invalid email or password.");
        }
    }
}
