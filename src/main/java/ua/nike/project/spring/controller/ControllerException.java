package ua.nike.project.spring.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ua.nike.project.spring.exceptions.BusinessException;

@ControllerAdvice
public class ControllerException {

    @ExceptionHandler(BusinessException.class)
    public ModelAndView handleCustomException(BusinessException ex) {

        ModelAndView model = new ModelAndView("error");
        model.addObject("errUserMsg", ex.getErrUserMsg());
        model.addObject("errExceptMsg", ex.getErrExceptMsg());
        model.addObject("errExceptionMsg", ex.getMessage());

        return model;

    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllException(Exception ex) {

        ModelAndView model = new ModelAndView("error");
        model.addObject("errUserMsg", "This is Exception.class");
        model.addObject("errExceptMsg", ex.getMessage());

        return model;

    }
}
