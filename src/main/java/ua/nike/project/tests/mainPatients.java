package ua.nike.project.tests;

import ua.nike.project.hibernate.entity.Operation;
import ua.nike.project.hibernate.entity.OperationDay;
import ua.nike.project.hibernate.entity.Patient;
import ua.nike.project.hibernate.model.EntityManagerFactorySingleton;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.time.LocalDate;

public class mainPatients {
    public static void main(String[] args) {
        Patient p1 = new Patient();
        p1.setSurname("Ірха");
        p1.setFirstName("Надія");
        p1.setSecondName("Петрівна");
        p1.setSex('Ж');
        p1.setStatus("пацієнт");

        OperationDay operationDay = new OperationDay();
        operationDay.setOperationDate( Date.valueOf(LocalDate.of (2018,4,15)));
        operationDay.setSurgeon("совва");

        Operation operation = new Operation();
        operation.setOperationDay(operationDay);
        operation.setOperationName("ФЕК");
        operation.setEye("Ou");
        operation.setManager("флора");
        operation.setPatient(p1);

        EntityManager em = EntityManagerFactorySingleton.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        em.persist(operationDay);
        em.persist(p1);
        em.persist(operation);
        em.getTransaction().commit();

        em.close();
        EntityManagerFactorySingleton.getEntityManagerFactory().close();
    }
}
