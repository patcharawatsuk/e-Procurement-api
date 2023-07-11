package eprocurementapi.exception;

public abstract class BaseException extends Exception {

    private final int errorCode;
    private Object errorMsgList = null;

    public BaseException(int errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }

    public BaseException(int errorCode, Object errorMsgList) {
        super("");
        this.errorMsgList = errorMsgList;
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public Object getErrorMsgList() {
        return errorMsgList;
    }
}
