package ua.nike.project.spring.dao;

import org.springframework.transaction.PlatformTransactionManager;
import ua.nike.project.spring.beans.entity.Patient;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;

public class PatientDAOImpl implements PatientDAO {

    private EntityManagerFactory entityManagerFactory;
    private PlatformTransactionManager transactionManager;

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void savePatient(Patient patient) {
        EntityManager entityManager = this.entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(patient);
        transaction.commit();
        entityManager.close();
    }

    @Override
    public Patient findPatient(int patientID) {
        EntityManager entityManager = this.entityManagerFactory.createEntityManager();
        Patient patient = entityManager.find(Patient.class, patientID);
        entityManager.close();
        return patient;
    }

    @Override
    public List<Patient> list() {
        EntityManager entityManager = this.entityManagerFactory.createEntityManager();
        List<Patient> patients = new ArrayList<Patient>(entityManager.createNamedQuery("Patient.findAll").getResultList());
        entityManager.close();
        return patients;
    }
}
