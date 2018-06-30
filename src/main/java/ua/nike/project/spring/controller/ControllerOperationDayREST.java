package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.hibernate.entity.Patient;
import ua.nike.project.spring.dao.OperationDayDAO;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.value.object.PatientVO;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/dates")
public class ControllerOperationDayREST {

    @Autowired
    OperationDayDAO operationDayDAO;

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Set<String> getDates() {
        Set<String> dates = new HashSet<>();
        for (Date date : operationDayDAO.getOperationDates()){
            dates.add(date.toString());
        }
        return dates;
    }

}
