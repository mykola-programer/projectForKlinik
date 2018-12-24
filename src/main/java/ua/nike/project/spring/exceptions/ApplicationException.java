package ua.nike.project.spring.exceptions;

public class ApplicationException extends Exception {
    private String errUserMsgs;
    private String errExceptMsg;

    public ApplicationException(String errUserMsgs) {
        this.errUserMsgs = errUserMsgs;
        this.errExceptMsg = super.getMessage();
    }
    public ApplicationException(String errUserMsgs, String errExceptMsg) {
        this.errUserMsgs = errUserMsgs;
        this.errExceptMsg = errExceptMsg;
    }
    public String getErrUserMsgs() {
        return errUserMsgs;
    }

    public String getErrExceptMsg() {
        return errExceptMsg;
    }

}
