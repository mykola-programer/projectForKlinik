package ua.nike.project.spring.dao;

import org.springframework.stereotype.Component;
import ua.nike.project.hibernate.entity.Patient;
import ua.nike.project.spring.exceptions.BusinessException;

import java.util.List;

@Component
public interface PatientDAO {

    int savePatient(Patient patient);

    Patient findPatient(int patientID) throws BusinessException;

    List<Patient> listPatients();

    void removePatient(int patientId) throws BusinessException;
}
