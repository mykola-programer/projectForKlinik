package ua.nike.project.spring.exceptions;

import org.springframework.validation.BindingResult;

public class ValidationException extends Exception {
    private String errUserMsg;
    private BindingResult bindingResult;

    public ValidationException(String errUserMsg, BindingResult bindingResult) {
        this.errUserMsg = errUserMsg;
        this.bindingResult = bindingResult;
    }

    public String getErrUserMsg() {
        return errUserMsg;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
