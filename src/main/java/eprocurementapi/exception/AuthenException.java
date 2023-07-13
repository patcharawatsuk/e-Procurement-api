package eprocurementapi.exception;

import lombok.Data;

@Data
public class AuthenException extends Exception {

    private Integer errorCode;
    private Object errorMsgList = null;
    public AuthenException(String msg) {
        super(msg);
    }
    public AuthenException(int errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }
    public AuthenException(int errorCode, Object errorMsgList) {
        super("");
        this.errorCode = errorCode;
        this.errorMsgList = errorMsgList;
    }

    public static AuthenException loginFail(String msg) {
        return new AuthenException(msg);
    }

    public static AuthenException createUserFail() {
        return new AuthenException("invalid input");
    }

    public static AuthenException createUserFail(int errorCode, String msg) {
        return new AuthenException(errorCode, msg);
    }

    public static AuthenException createUserFail(int errorCode, Object errorMsgList) {
        return new AuthenException(errorCode, errorMsgList);
    }

    public static AuthenException emailTaken() {
        return new AuthenException("Email has already registered");
    }

}
