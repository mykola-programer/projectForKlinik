package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.hibernate.model.HospitalizationBean;
import ua.nike.project.spring.dao.HospitalizationBeanDAO;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.HospitalizationBeanVO;

import java.sql.Date;
import java.util.List;

@RestController
public class ControllerHospitalizationREST {

    @Autowired
    HospitalizationBeanDAO hospitalizationBeanDAO;


    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/hospitalizations/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<HospitalizationBeanVO> getHospitalization(@PathVariable("date") String reqDate) throws BusinessException {

        if (reqDate != null && !reqDate.equals("")) {
            Date date = Date.valueOf(reqDate);
            return hospitalizationBeanDAO.listHospitalizations(date);
        } else {
            throw new BusinessException("Dates is not correct !");
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/not_hospitalizations/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<HospitalizationBeanVO> getNoHospitalization(@PathVariable("date") String reqDate) throws BusinessException {

        if (reqDate != null && !reqDate.equals("")) {
            Date date = Date.valueOf(reqDate);
            return hospitalizationBeanDAO.listNoHospitalizations(date);
        } else {
            throw new BusinessException("Dates is not correct !");
        }
    }

}
