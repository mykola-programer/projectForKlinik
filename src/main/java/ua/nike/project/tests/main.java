package ua.nike.project.tests;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import ua.nike.project.hibernate.entity.Operation;
import ua.nike.project.hibernate.entity.OperationDay;
import ua.nike.project.hibernate.entity.Patient;

import javax.naming.Context;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class main {
    private static final EntityManagerFactory managerFactory = Persistence.createEntityManagerFactory("Hibernate_JPA");

    public static void main(String[] args) throws InterruptedException {

        EntityManager entityManager = managerFactory.createEntityManager();
//        EntityManager entityManager2 = managerFactory.createEntityManager();
//        EntityManager entityManager3 = managerFactory.createEntityManager();
//
//        System.out.println("managerFactory");
//        Thread.sleep(10000);
//        entityManager.getTransaction().begin();
//        System.out.println("Tranc1");
//        Thread.sleep(5000);
//        entityManager2.getTransaction().begin();
//        System.out.println("Tranc2");
//        Thread.sleep(5000);
//        entityManager3.getTransaction().begin();
//        System.out.println("Tranc3");
//        System.out.println("entityManager");
//        Thread.sleep(10000);
//
        Set<Patient> set = new HashSet<Patient>(entityManager.createQuery("FROM Patient").getResultList());
//        Patient patient1 = (Patient) entityManager.createQuery("FROM Patient p where p.surname = 'Терещук'").getSingleResult();
//        Query query = entityManager.createQuery("FROM Patient p where p.surname = 'Терещук'");

        for (Patient pat : set) {
            System.out.println(pat);
            Set<Operation> operations = pat.getOperations();
            System.out.println("List: ");
            for (Operation oper : operations) {
                System.out.println(oper);
            }
            Thread.sleep(5000);
        }
//
//        Set<OperationDay> set2 = new HashSet<OperationDay>(entityManager.createQuery("FROM OperationDay ").getResultList());
//        for (OperationDay pat : set2) {
//            System.out.println(pat);
//            Set<Operation> operations = pat.getOperations();
//            System.out.println("List: ");
//            for (Operation oper : operations) {
//                System.out.println(oper);
//            }
//            Thread.sleep(10000);
//        }

//        System.out.println("Patient1-------------------");
//        entityManager.find(Patient.class, 3);
//        System.out.println(entityManager.find(Patient.class, 3));
//        Set<Patient> patients2 = new HashSet<Patient>(entityManager.createNamedQuery("getPatients").getResultList());
//
//        for (Patient pat:patients2) {
//            System.out.println(pat);
//        }
//entityManager.getTransaction().begin();
//        System.out.println("Patient2-------------------");
//        Patient patient = entityManager.find(Patient.class, 3);
//        System.out.println("//////////////");
//        Patient patient111 = entityManager.getReference(Patient.class, 4);
////        entityManager.refresh(patient);
//        System.out.println("_______ flash _________");
//        patient.setFirstName("Світлана");
//        patient111.setFirstName("Іван");
////        entityManager.flush();
//
//        System.out.println(patient);
//        System.out.println(patient111);
//
//        System.out.println("_____ transaction comit ___________");
//        entityManager.getTransaction().commit();

//        System.out.println("Patients-------------------");
//        Set<Patient> patients3 = new HashSet<Patient>(entityManager.createNamedQuery("getPatients").getResultList());
//        patients3.forEach(System.out::println);
////        for (Patient pat:patients3) {
////            System.out.println(pat);
////        }
//        System.out.println("Criteria -----------------------");
//        entityManager.getTransaction().begin();
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//
//        CriteriaQuery<Patient> cq = cb.createQuery(Patient.class);
//        Root<Patient> from = cq.from(Patient.class);
//        cq.select(from);
//        entityManager.createQuery(cq)
//                .getResultList()
//                .forEach(System.out::println);
//        entityManager.getTransaction().commit();


        //        OperationDay operationDay = entityManager.find(OperationDay.class, 3);
//        Set<Operation> operations = operationDay.getOperations();
//        List<Patient> patients = new ArrayList<Patient>();
//        for (Operation oper : operations) {
//            System.out.println(oper.getPatient());
//            patients.add(oper.getPatient());
//        }
//        Thread.sleep(5000);

        entityManager.close();
//        entityManager2.close();
//        entityManager3.close();
        managerFactory.close();
    }
}
