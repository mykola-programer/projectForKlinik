package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import ua.nike.project.spring.service.ServiceMassage;

@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    @Autowired
    ServiceMassage serviceMass;

    @ExceptionHandler(ApplicationException.class)
    protected ResponseEntity<Object> handleConflict(ApplicationException ex, WebRequest request) {

        MassageResponse bodyOfResponse = new MassageResponse(ex.getErrUserMsgs() + "\n" + (ex.getErrExceptMsg() != null ? ex.getErrExceptMsg() : ""));
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.MULTIPLE_CHOICES, request);
    }


    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<Object> handleValid(ValidationException ex, WebRequest request) {

        MassageResponse bodyOfResponse = new MassageResponse(null, transformValidMassage(ex.getBindingResult()));
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.UNSUPPORTED_MEDIA_TYPE, request);
    }

    private String transformValidMassage(BindingResult bindingResult) {
        if (bindingResult == null) return null;
        final StringBuilder result = new StringBuilder();
        result.append(serviceMass.value(bindingResult.getTarget().getClass().getSimpleName() + ".massage"))
                .append(" \n");
        result.append(serviceMass.value("count.mistakes")).append(" ")
                .append(bindingResult.getErrorCount())
                .append(": \n");
        for (FieldError error : bindingResult.getFieldErrors()) {
            result.append("* ")
                    .append(serviceMass.value(error.getDefaultMessage()))
                    .append(" (")
                    .append(serviceMass.value("mistake"))
                    .append(" [")
                    .append(bindingResult.getFieldValue(error.getField()))
                    .append("])")
                    .append(" \n");
        }
        result.append(serviceMass.value("please.validate"));
        return result.toString();
    }

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
}





/*

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