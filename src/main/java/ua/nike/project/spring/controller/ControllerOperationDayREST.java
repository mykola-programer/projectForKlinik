package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.hibernate.entity.Hospitalization;
import ua.nike.project.hibernate.model.HospitalizationBean;
import ua.nike.project.spring.dao.HospitalizationBeanDAO;
import ua.nike.project.spring.dao.OperationDayDAO;
import ua.nike.project.spring.exceptions.BusinessException;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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