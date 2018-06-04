package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.hibernate.entity.Patient;
import ua.nike.project.spring.dao.PatientDAO;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/patients")
public class ControllerPatientREST {

    @Autowired
    ApplicationContext context;

    @Autowired
    PatientDAO patientDAO;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<Patient> getPatients() {
        List<Patient> patients = patientDAO.listPatients();

        // It's a bad code !!! Must to do more correctly!
        //Corrected LazyLoading
        List<Patient> result = new ArrayList<>();
        for (Patient p : patients) {
            p.setOperations(null);
            result.add(p);
        }

        return result;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Patient getPatient(@PathVariable("id") int patientId) {
        Patient patient = patientDAO.findPatient(patientId);

        //IF Patient did not find in database
        if (patient == null) {
            patient = new Patient();
            patient.setSurname("NOT FOUND");
            return patient;
        }

        patient.setOperations(null);
        return patient;
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Patient savePatient(
            @RequestParam("surname") String surname,
            @RequestParam("firstName") String firstName,
            @RequestParam("secondName") String secondName,
            @RequestParam("sex") String sex,
            @RequestParam("status") String status,
            @RequestParam("telephone") String telephone) {

        // Problems with UTF-8 !
        // Problem with catch object Patient from RequestBody !
        Patient patient = (Patient) context.getBean("patient");
        patient.setSurname(surname);
        patient.setFirstName(firstName);
        patient.setSecondName(secondName);
        patient.setSex(sex.isEmpty() ? null : sex.charAt(0));
        patient.setStatus(status);
        patient.setTelephone(telephone);
        System.out.println();
        System.out.println(patient);
//        patientDAO.savePatient(patient);
        return patient;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_XML_VALUE)
    public String removePatient(@PathVariable("id") int patientId) {
//        patientDAO.removePatient(patientId);

        return "Patient : " + patientId + " - was deleted successful !!!";
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_XML_VALUE)
    public String removeAll() {
        for (Patient patient : patientDAO.listPatients()){
//            patientDAO.removePatient(patient.getPatientId());
            System.out.println(patient);
        }

        return "All Patients - was deleted successful !!!";
    }


}
