package eprocurementapi.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import eprocurementapi.exception.AuthenException;
import eprocurementapi.exception.BaseException;
import eprocurementapi.util.Constant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

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
        @JsonFormat(timezone = Constant.TimeZoneIds.ASIA_BANGKOK, pattern = Constant.DATETIME_FORMAT_ZONE)
        private Date timestamp = new Date();
        private int status;
        private Object error;
        private String path;
    }
}
