package eprocurementapi.exception;

import org.springframework.http.HttpStatus;

public class UnexpectedException extends BaseException{
    public UnexpectedException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public static UnexpectedException databaseFailHandling(String msg) {
        return new UnexpectedException(HttpStatus.EXPECTATION_FAILED.value(), msg);
    }

    public static UnexpectedException handleCustomError(String msg) {
        return new UnexpectedException(HttpStatus.EXPECTATION_FAILED.value(), msg);
    }
}
