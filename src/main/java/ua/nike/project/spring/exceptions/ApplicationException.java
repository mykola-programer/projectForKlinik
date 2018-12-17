package ua.nike.project.spring.exceptions;

public class ApplicationException extends Exception {
    private String errUserMsgs;
    private String errExceptMsg;

    public ApplicationException(String errUserMsgs) {
        this.errUserMsgs = errUserMsgs;
        this.errExceptMsg = super.getMessage();
    }

    public String getErrUserMsgs() {
        return errUserMsgs;
    }

    public String getErrExceptMsg() {
        return errExceptMsg;
    }

}
