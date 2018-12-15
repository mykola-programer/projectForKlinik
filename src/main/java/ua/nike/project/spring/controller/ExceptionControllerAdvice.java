package ua.nike.project.spring.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ua.nike.project.spring.exceptions.ApplicationException;

@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllException(Exception ex) {

        ModelAndView model = new ModelAndView("error");
        model.addObject("errUserMsg", "This is Exception.class");
        model.addObject("errExceptMsg", ex.getMessage());

        return model;

    }

/*    @ExceptionHandler(ApplicationException.class)
    public ModelAndView handleCustomException(ApplicationException ex) {
        ModelAndView model = new ModelAndView("error");
        model.addObject("errUserMsg", ex.getErrUserMsgs());
        model.addObject("errExceptMsg", ex.getErrExceptMsg());
        model.addObject("errExceptionMsg", ex.getMessage());

        return model;

    }*/

    @ExceptionHandler(value
            = {ApplicationException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleConflict(
            ApplicationException ex, WebRequest request) {
//            String bodyOfResponse = "This should be application specific";
        MassageResponse bodyOfResponse = new MassageResponse(ex.getErrUserMsgs());
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.MULTIPLE_CHOICES, request);
    }

    private static class MassageResponse {
        private String exeptionMassage;

        public MassageResponse(String exeptionMassage) {
            this.exeptionMassage = exeptionMassage;
        }

        public String getExeptionMassage() {
            return exeptionMassage;
        }

        public void setExeptionMassage(String exeptionMassage) {
            this.exeptionMassage = exeptionMassage;
        }
    }
}
