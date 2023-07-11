package eprocurementapi.exception;

public class AuthenException extends Exception {

    public AuthenException(String msg) {
        super(msg);
    }

    public static AuthenException loginFail(String msg) {
        return new AuthenException(msg);
    }

}
