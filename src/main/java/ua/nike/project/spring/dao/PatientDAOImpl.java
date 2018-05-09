package ua.nike.project.spring.dao;

import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.Patient;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class PatientDAOImpl implements PatientDAO {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
    /**
     * Return patientID after add to database.
     */
    public int savePatient(Patient patient) {
        this.entityManager.persist(patient);
        return patient.getPatientId();
    }

    @Override
    @Transactional
    public Patient findPatient(int patientID) {
        return this.entityManager.find(Patient.class, patientID);
    }

    @Override
    @Transactional
    public List<Patient> listPatients() {
        return this.entityManager.createNamedQuery("Patient.findAll", Patient.class).getResultList();
    }
}
