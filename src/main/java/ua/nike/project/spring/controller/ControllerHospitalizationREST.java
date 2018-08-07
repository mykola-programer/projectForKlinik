package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.hibernate.model.HospitalizationBean;
import ua.nike.project.spring.dao.HospitalizationBeanDAO;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.HospitalizationBeanVO;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class ControllerHospitalizationREST {
private final String DATE_FORMAT = "dd.MM.yyyy";
    @Autowired
    HospitalizationBeanDAO hospitalizationBeanDAO;


    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/hospitalizations/{date}/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<HospitalizationBeanVO> getHospitalization(@PathVariable("date") String reqDate) throws BusinessException {
        System.out.println(reqDate);

        if (reqDate != null && !reqDate.equals("")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            LocalDate date = LocalDate.parse(reqDate,formatter);
            return hospitalizationBeanDAO.listHospitalizations(date);
        } else {
            throw new BusinessException("Dates is not correct !");
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/not_hospitalizations/{date}/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<HospitalizationBeanVO> getNoHospitalization(@PathVariable("date") String reqDate) throws BusinessException {

        if (reqDate != null && !reqDate.equals("")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            LocalDate date = LocalDate.parse(reqDate,formatter);
            return hospitalizationBeanDAO.listNoHospitalizations(date);
        } else {
            throw new BusinessException("Dates is not correct !");
        }
    }

}
