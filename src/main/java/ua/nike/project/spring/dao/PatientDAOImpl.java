package ua.nike.project.spring.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.Patient;
import ua.nike.project.spring.exceptions.BusinessException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    public Patient findPatient(int patientID) throws BusinessException {
        Patient patient = this.entityManager.find(Patient.class, patientID);
        if (patient == null) {
            throw new BusinessException("This patient is not find in database !");
        }
        return patient;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Patient> listPatients() {
        return this.entityManager.createNamedQuery("Patient.findAll", Patient.class).getResultList();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void removePatient(int patientId) throws BusinessException {
        this.entityManager.remove(findPatient(patientId));
    }

}
