package eprocurementapi.config;

import eprocurementapi.exception.AuthenException;
import eprocurementapi.service.JwtService;
import eprocurementapi.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    public static final List<String> REQUEST_WHITELIST = Arrays.asList(
            //#### swagger ui ####
            "/api-docs/**",
            //#### others ####
            "/",
            "/api/auth/**",
            "/swagger-ui/**"
    );

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (REQUEST_WHITELIST.stream().anyMatch(p -> pathMatcher.match(p, request.getServletPath()))) {
            filterChain.doFilter(request, response);
        } else {
            try {
                if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
                    handlerExceptionResolver.resolveException(request, response, null, new AuthenException("Unauthorized"));
                    return;
                }
                jwt = authHeader.substring(7);
                userEmail = jwtService.extractUserName(jwt);
                if (StringUtils.isNotEmpty(userEmail)
                        && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userService.userDetailsService()
                            .loadUserByUsername(userEmail);
                    if (jwtService.isTokenValid(jwt, userDetails)) {
                        SecurityContext context = SecurityContextHolder.createEmptyContext();
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        context.setAuthentication(authToken);
                        SecurityContextHolder.setContext(context);
                    }
                }
                filterChain.doFilter(request, response);
            } catch (Exception ex) {
                handlerExceptionResolver.resolveException(request, response, null, new AuthenException("Unauthorized"));
                return;
            }
        }
    }
}
