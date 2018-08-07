package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ua.nike.project.hibernate.model.OperationBean;
import ua.nike.project.spring.dao.OperationBeanDAO;
import ua.nike.project.spring.dao.OperationDayDAO;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@EnableWebMvc
@Controller
public class ControllerSpringMVC {

    @Autowired
    ApplicationContext context;

    @Autowired
    OperationDayDAO operationDayDAO;

    @Autowired
    OperationBeanDAO operationBeanDAO;

    @RequestMapping(value = "/operations_report", method = RequestMethod.GET)
    public ModelAndView methodGET(@ModelAttribute("date") String reqDate, ModelAndView model) {
        try {
            List<LocalDate> operation_dates = operationDayDAO.getOperationDates();
            model.addObject("operation_dates", operation_dates);

            if (reqDate != null && !reqDate.equals("")) {
                Date date = Date.valueOf(reqDate);
                model.addObject("selected_date", date);

                List<OperationBean> operations = operationBeanDAO.list(date);
                model.addObject("operations", operations);

            } else {
                model.addObject("Massage", "Введіть обовязково дату!");
            }
        } catch (IllegalArgumentException e) {
            model.addObject("ErrorMassage", "Некорректна дата ! Введіть обовязково дату!");

        } catch (Exception e) {
            model.addObject("ErrorMassage", e.getMessage());

        }
        model.setViewName("/operations_report");
        return model;

    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public ModelAndView indexTest() {
        ModelAndView model = new ModelAndView();
        model.setViewName("/start");
        model.addObject("name", "Spring MVC");
        return model;
    }
}
