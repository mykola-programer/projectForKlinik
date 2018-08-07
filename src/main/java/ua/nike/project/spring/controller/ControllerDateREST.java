package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.nike.project.spring.dao.OperationDayDAO;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/dates")
public class ControllerDateREST {

    @Autowired
    OperationDayDAO operationDayDAO;

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<LocalDate> getDates() {
        return operationDayDAO.getOperationDates();
    }

}
