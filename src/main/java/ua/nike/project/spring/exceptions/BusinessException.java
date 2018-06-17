package ua.nike.project.spring.exceptions;

public class BusinessException extends Exception {
    private String errUserMsg;
    private String errExceptMsg;

    public String getErrUserMsg() {
        return errUserMsg;
    }

    public String getErrExceptMsg() {
        return errExceptMsg;
    }

    public BusinessException(String errUserMsg) {
        this.errUserMsg = errUserMsg;
        this.errExceptMsg = super.getMessage();
    }
}
