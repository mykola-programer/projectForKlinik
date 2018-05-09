package ua.nike.project.spring.dao;

import ua.nike.project.hibernate.entity.Patient;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class PatientDAOImpl implements PatientDAO {

    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void savePatient(Patient patient) {
        EntityTransaction transaction = this.entityManager.getTransaction();
        transaction.begin();
        this.entityManager.persist(patient);
        transaction.commit();
    }

    @Override
    public Patient findPatient(int patientID) {
        return this.entityManager.find(Patient.class, patientID);
    }

    @Override
    public List<Patient> list() {
        return this.entityManager.createNamedQuery("Patient.findAll", Patient.class).getResultList();
    }
}
