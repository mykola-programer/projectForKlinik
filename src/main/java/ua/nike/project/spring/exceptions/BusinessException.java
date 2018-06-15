package ua.nike.project.spring.exceptions;

public class BusinessException extends Exception {
    private String message;

    public BusinessException(String message) {
        this.message = message;
    }
}
