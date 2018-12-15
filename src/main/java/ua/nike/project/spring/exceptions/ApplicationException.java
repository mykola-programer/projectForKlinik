package ua.nike.project.spring.exceptions;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class ApplicationException extends Exception {
    private String errUserMsgs;
    private String errExceptMsg;
    private HttpStatus httpStatus;

    public ApplicationException(String errUserMsgs/*, HttpStatus httpStatus*/) {
        this.errUserMsgs = errUserMsgs;
        this.errExceptMsg = super.getMessage();
        this.httpStatus = httpStatus;
    }

    public String getErrUserMsgs() {
        return errUserMsgs;
    }

    public String getErrExceptMsg() {
        return errExceptMsg;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
