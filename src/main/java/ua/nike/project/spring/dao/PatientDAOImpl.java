package ua.nike.project.spring.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.Patient;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.value.object.PatientVO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PatientDAOImpl implements PatientDAO {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    /**
     * Return patientID after add to database.
     */
    public int savePatient(Patient patient) {
        this.entityManager.persist(patient);
        return patient.getPatientId();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PatientVO findPatient(int patientID) throws BusinessException {
        Patient patient = this.entityManager.find(Patient.class, patientID);
        if (patient == null) {
            throw new BusinessException("This patient is not find in database !");
        }
        PatientVO patientVO = transformToPatientVO(patient);
        return patientVO;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<PatientVO> listPatients() {
        List<Patient> patients = this.entityManager.createNamedQuery("Patient.findAll", Patient.class).getResultList();
        List<PatientVO> patientVOList = new ArrayList<>();
        for (Patient patient : patients) {
            patientVOList.add(transformToPatientVO(patient));
        }
        return patientVOList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void removePatient(int patientId) {
//        this.entityManager.remove(entityManager.find(Patient.class, patientId));
    }

    private PatientVO transformToPatientVO(Patient patient) {
        if (patient == null) return null;
        PatientVO patientVO = new PatientVO();
        patientVO.setPatientId(patient.getPatientId());
        patientVO.setSurname(patient.getSurname());
        patientVO.setFirstName(patient.getFirstName());
        patientVO.setSecondName(patient.getSecondName());
        patientVO.setSex(patient.getSex());
        patientVO.setStatus(patient.getStatus());
//        patientVO.setRelative(transformToPatientVO(patient.getRelative()));
        patientVO.setTelephone(patient.getTelephone());
        return patientVO;
    }

}
