package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ua.nike.project.spring.exceptions.ApplicationException;
import ua.nike.project.spring.exceptions.ValidationException;

import javax.persistence.EntityNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@ControllerAdvice
@PropertySource({"classpath:validation/ua.properties"})
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    /*
     nl - new line
     Use "<br>" for html-string;
     Use "\n" for text-string;
     */
    private final String nl = "<br>";
    @Autowired
    private Environment env;

    @ExceptionHandler(ApplicationException.class)
    protected ResponseEntity<Object> handleConflictException(ApplicationException ex, WebRequest request) {
        StringBuilder bodyOfResponse = new StringBuilder().append(getValue(ex.getErrUserMsgs())).append(nl).append(getValue(ex.getErrExceptMsg()));
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }


    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<Object> handleValidException(ValidationException ex, WebRequest request) {
        String bodyOfResponse = transformValidMassage(ex.getBindingResult());
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
        StringBuilder bodyOfResponse = new StringBuilder();
        HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        for (Throwable runtimeException = ex; runtimeException != null; ) {
//            bodyOfResponse.append("RuntimeException : ").append(runtimeException.getClass().getSimpleName()).append(" => ").append(runtimeException.getLocalizedMessage()).append(nl);
            if (runtimeException instanceof EntityNotFoundException) {
                bodyOfResponse.append(getValue("object.not.find")).append(nl);
                httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            } else if (runtimeException instanceof SQLException) {
                bodyOfResponse.append(getValue("violation.of.integrity")).append(nl);
                httpStatus = HttpStatus.BAD_REQUEST;
            }
            runtimeException = runtimeException.getCause();
        }
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), httpStatus, request);
    }


    private String transformValidMassage(BindingResult bindingResult) {
        if (bindingResult == null) return null;
        final StringBuilder result = new StringBuilder();
        result.append(getValue("violation.mistake"))
                .append(nl);
//        result.append(getValue("mistakes.count")).append(" ")
//                .append(bindingResult.getErrorCount())
//                .append(": " + nl);
        int i = 1;
        for (FieldError error : bindingResult.getFieldErrors()) {
            result.append(i + ". ")
                    .append(getValue(error.getDefaultMessage()))
                    .append(" (")
                    .append(getValue("mistake"))
                    .append(" [")
                    .append(bindingResult.getFieldValue(error.getField()))
                    .append("])")
                    .append(nl);
            i++;
        }
        result.append(getValue("please.validate"));
        return result.toString();
    }

    private String getValue(String key) {
        if (key == null) {
            return null;
        }
        try {
            return new String(env.getProperty(key, key).getBytes("ISO8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}





/*


    private static class MassageResponse {
        private String exceptionMassage;
        private String validationMassage;

        public MassageResponse(String exceptionMassage, String validationMassage) {
            this.exceptionMassage = exceptionMassage;
            this.validationMassage = validationMassage;
        }

        public MassageResponse(String exceptionMassage) {
            this.exceptionMassage = exceptionMassage;
        }

        public String getExceptionMassage() {
            return exceptionMassage;
        }

        public void setExceptionMassage(String exceptionMassage) {
            this.exceptionMassage = exceptionMassage;
        }

        public String getValidationMassage() {
            return validationMassage;
        }

        public void setValidationMassage(String validationMassage) {
            this.validationMassage = validationMassage;
        }
    }

        @ExceptionHandler(Exception.class)
    public ModelAndView handleAllException(Exception ex) {

        ModelAndView model = new ModelAndView("error");
        model.addObject("errUserMsg", "This is Exception.class");
        model.addObject("errExceptMsg", ex.getMessage());

        return model;

    }

    @ExceptionHandler(ApplicationException.class)
    public ModelAndView handleCustomException(ApplicationException ex) {
        ModelAndView model = new ModelAndView("error");
        model.addObject("errUserMsg", ex.getErrUserMsgs());
        model.addObject("errExceptMsg", ex.getErrExceptMsg());
        model.addObject("errExceptionMsg", ex.getMessage());

        return model;

    }*/