package ua.nike.project.spring.dao;

import org.springframework.stereotype.Component;
import ua.nike.project.hibernate.entity.Patient;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.PatientVO;

import java.util.List;

@Component
public interface PatientDAO {

    int editPatient(PatientVO patient);

    int savePatient(PatientVO patient);

    PatientVO findPatient(int patientID) throws BusinessException;

    List<PatientVO> listPatients();

    void removePatient(int patientId);

    List<PatientVO> listRelatives();
}
