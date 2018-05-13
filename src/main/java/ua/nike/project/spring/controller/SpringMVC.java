package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ua.nike.project.hibernate.model.OperationBean;
import ua.nike.project.spring.dao.OperationBeanDAO;
import ua.nike.project.spring.dao.OperationDayDAO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Controller
public class SpringMVC {

    @Autowired
    ApplicationContext context;

    @RequestMapping(value = "/operations_report", method = RequestMethod.GET)
    public ModelAndView methodGET(@ModelAttribute("date") String reqDate, ModelAndView model) {
        try {
            OperationDayDAO operationDayDAO = context.getBean(OperationDayDAO.class);
            Set<Date> operation_dates = operationDayDAO.getOperationDates();
            model.addObject("operation_dates", operation_dates);

            if (reqDate != null && !reqDate.equals("")) {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(reqDate, format);
                model.addObject("selected_date", java.sql.Date.valueOf(date));

                OperationBeanDAO operationBeanDAO = context.getBean(OperationBeanDAO.class);
                List<OperationBean> operations = operationBeanDAO.list(date);
                model.addObject("operations", operations);

            } else {
                model.addObject("Massage", "Введіть обовязково дату!");
            }
        } catch (Exception e) {
            model.addObject("ErrorMassage", e.getMessage());
        } finally {
            return model;
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView indexTest() {
        ModelAndView model = new ModelAndView();
        model.setViewName("index");
        model.addObject("name", "Spring MVC");
        return model;
    }
}
