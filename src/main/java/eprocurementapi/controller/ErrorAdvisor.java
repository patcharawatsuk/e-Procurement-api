package eprocurementapi.controller;

import eprocurementapi.exception.AuthenException;
import eprocurementapi.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZoneId;

@ControllerAdvice
public class ErrorAdvisor {

    @ExceptionHandler(AuthenException.class)
    public ResponseEntity<?> handleAuthenException(AuthenException e, HttpServletRequest request, HttpServletResponse response) {
        int httpStatus = HttpStatus.FORBIDDEN.value();
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

    @Data
    public class ErrorResponse {
        private final LocalDateTime localDateTimeTH = LocalDateTime.now().atZone(ZoneId.of("Asia/Bangkok")).toLocalDateTime();
        private int status;
        private Object error;
        private String path;
    }
}
