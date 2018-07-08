package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.hibernate.entity.Patient;
import ua.nike.project.spring.dao.PatientDAO;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.PatientVO;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class ControllerPatientREST {

    @Autowired
    PatientDAO patientDAO;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PatientVO> getPatients() {
        return patientDAO.listPatients();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PatientVO getPatient(@PathVariable("id") int patientId) throws BusinessException, Exception {
        return patientDAO.findPatient(patientId);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Patient savePatient(@ModelAttribute Patient patient ) {

        /*        patientDAO.savePatient(patient);        */

        return patient;
    }


    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_XML_VALUE)
    public String deletePatient(@PathVariable("id") int patientId) {

        /* patientDAO.removePatient(patientId); */

        return "PatientVO : " + patientId + " - was deleted successful !!!";
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_XML_VALUE)
    public String deleteAll() {
        for (PatientVO patient : patientDAO.listPatients()) {

            /* patientDAO.removePatient(patient.getPatientId()); */

            System.out.println(patient);
        }

        return "All Patients - was deleted successful !!!";
    }


}
