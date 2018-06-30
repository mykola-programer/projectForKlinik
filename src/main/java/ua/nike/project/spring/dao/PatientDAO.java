package ua.nike.project.spring.dao;

import org.springframework.stereotype.Component;
import ua.nike.project.hibernate.entity.Patient;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.value.object.PatientVO;

import java.util.List;

@Component
public interface PatientDAO {

    int savePatient(Patient patient);

    PatientVO findPatient(int patientID) throws BusinessException;

    List<PatientVO> listPatients();

    void removePatient(int patientId);
}
