package eprocurementapi.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import eprocurementapi.exception.AuthenException;
import eprocurementapi.exception.BaseException;
import eprocurementapi.util.Constant;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorAdvisor {
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException e, HttpServletRequest request, HttpServletResponse response) {
        int httpStatus = HttpStatus.UNAUTHORIZED.value();
        ErrorResponse res = new ErrorResponse();
        res.setError(e.getMessage());
        res.setStatus(httpStatus);
        res.setPath(request.getServletPath());
        return ResponseEntity.status(httpStatus).body(res);
    }

    @ExceptionHandler(AuthenException.class)
    public ResponseEntity<?> handleAuthenException(AuthenException e, HttpServletRequest request, HttpServletResponse response) {
        int httpStatus = Objects.isNull(e.getErrorCode()) ? HttpStatus.UNAUTHORIZED.value() : e.getErrorCode();
        ErrorResponse res = new ErrorResponse();
        res.setError(e.getMessage());
        res.setStatus(httpStatus);
        res.setPath(request.getServletPath());
        return ResponseEntity.status(httpStatus).body(res);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> handleBaseException(BaseException e, HttpServletRequest request, HttpServletResponse response) {
        ErrorResponse res = new ErrorResponse();
        res.setError(e.getErrorMsgList() != null ? e.getErrorMsgList() : e.getMessage());
        res.setStatus(e.getErrorCode());
        res.setPath(request.getServletPath());
        return ResponseEntity.status(e.getErrorCode()).body(res);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request, HttpServletResponse response) {
        BindingResult bindingResult = ex.getBindingResult();
        List<String> errorMessages = bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ErrorResponse res = new ErrorResponse();
        res.setError(errorMessages);
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setPath(request.getServletPath());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleValidationException(HttpMessageNotReadableException ex, HttpServletRequest request, HttpServletResponse response) {
        ErrorResponse res = new ErrorResponse();
        res.setError(ex.getMessage());
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setPath(request.getServletPath());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @Data
    public class ErrorResponse {
        @JsonFormat(timezone = Constant.TimeZoneIds.ASIA_BANGKOK, pattern = Constant.DATETIME_FORMAT_ZONE)
        private Date timestamp = new Date();
        private int status;
        private Object error;
        private String path;
    }
}
