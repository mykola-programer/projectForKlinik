package ua.nike.project.spring.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.Patient;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.PatientVO;

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
     * Return patientID after edit to database.
     */
    public int editPatient(PatientVO patientVO) {
        Patient patient = this.entityManager.find(Patient.class, patientVO.getPatientId());
        this.copyToPatient(patientVO, patient);
        this.entityManager.persist(patient);
        return patient.getPatientId();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    /**
     * Return patientID after add to database.
     */
    public int savePatient(PatientVO patientVO) {
        Patient patient = new Patient();
        this.copyToPatient(patientVO, patient);
        this.entityManager.persist(patient);
        this.entityManager.flush();
        return patient.getPatientId();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PatientVO findPatient(int patientID) throws BusinessException {
        Patient patient = this.entityManager.find(Patient.class, patientID);
        if (patient == null) {
            throw new BusinessException("This patient is not find in database !");
        }
        PatientVO result = transformToPatientVO(patient);
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<PatientVO> listPatients() {
        List<Patient> patients = this.entityManager.createNamedQuery("Patient.findAll", Patient.class).getResultList();
        List<PatientVO> result = new ArrayList<>();
        for (Patient patient : patients) {
            result.add(transformToPatientVO(patient));
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<PatientVO> listRelatives() {
        List<Patient> patients = this.entityManager.createNamedQuery("Patient.findAllRelatives", Patient.class).getResultList();
        List<PatientVO> result = new ArrayList<>();
        for (Patient patient : patients) {
            result.add(transformToPatientVO(patient));
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void removePatient(int patientId) {
//        this.entityManager.remove(entityManager.find(Patient.class, patientId));
    }

    private PatientVO transformToPatientVO(Patient patient) {
        if (patient == null) return null;
        PatientVO result = new PatientVO();
        result.setPatientId(patient.getPatientId());
        result.setSurname(patient.getSurname());
        result.setFirstName(patient.getFirstName());
        result.setSecondName(patient.getSecondName());
        result.setSex(patient.getSex());
        result.setStatus(patient.getStatus());
        if (patient.getRelative() != null) {
            result.setRelativeID(patient.getRelative().getPatientId());
        } else {
            result.setRelativeID(0);
        }
        result.setTelephone(patient.getTelephone());
        return result;
    }

    private void copyToPatient(PatientVO original, Patient result) {
        if (original != null) {
            result.setSurname(original.getSurname());
            result.setFirstName(original.getFirstName());
            result.setSecondName(original.getSecondName());
            result.setSex(original.getSex());
            result.setStatus(original.getStatus());
            if (original.getRelativeID() > 0) {
                result.setRelative(this.entityManager.find(Patient.class, original.getRelativeID()));
            } else {
                result.setRelative(null);
            }
            result.setTelephone(original.getTelephone());
        }
    }

}
