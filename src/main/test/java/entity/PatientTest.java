package entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.nike.project.hibernate.entity.Operation;
import ua.nike.project.hibernate.entity.Patient;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class PatientTest {

    private Patient patient;

    private Integer patientId = 123;
    private String surname = "Богдан";
    private String firstName = "Микола";
    private String secondName = "Орестович";
    private Character sex = 'Ч';
    private String status = "супроводжуючий";
    private Patient relative = new Patient();
    private String telephone = "+380958888888";
    private Set<Operation> operations = new HashSet<>();

    private Operation operation1;
    private Operation operation2;
    private Operation operation3;
    private Operation operation4;


    @Before
    public void beforePatientTest() {
        patient = new Patient();
        System.out.println("Created patient !");

        operation1 = new Operation();
        operation1.setOperationName("ФЕК");
        operation2 = new Operation();
        operation2.setOperationName("ЛЕК");
        operation3 = new Operation();
        operation3.setOperationName("YAG");
        operation4 = new Operation();
        operation4.setOperationName("ППЛК");

        operations.add(operation1);
        operations.add(operation2);
        operations.add(operation3);
        operations.add(operation4);
    }

    @Test
    public void setPatientParameters() {
        assertNull(patient.getPatientId());
        patient.setPatientId(patientId);
        assertTrue(patient.getPatientId() == patientId);
        System.out.println("Tested ID !");

        assertNull(patient.getSurname());
        patient.setSurname(surname);
        assertEquals(patient.getSurname(), surname);
        System.out.println("Tested Surname !");

        assertNull(patient.getFirstName());
        patient.setFirstName(firstName);
        assertEquals(patient.getFirstName(), firstName);
        System.out.println("Tested FirstName !");

        assertNull(patient.getSecondName());
        patient.setSecondName(secondName);
        assertEquals(patient.getSecondName(), secondName);
        System.out.println("Tested SecondName !");

        assertNull(patient.getSex());
        patient.setSex(sex);
        assertEquals(patient.getSex(), sex);
        System.out.println("Tested Sex !");

        assertNull(patient.getStatus());
        patient.setStatus(status);
        assertEquals(patient.getStatus(), status);
        System.out.println("Tested Status !");

        assertNull(patient.getRelative());
        patient.setRelative(relative);
        assertEquals(patient.getRelative(), relative);
        System.out.println("Tested Relative !");

        assertNull(patient.getTelephone());
        patient.setTelephone(telephone);
        assertEquals(patient.getTelephone(), telephone);
        System.out.println("Tested Telephone !");

        assertNull(patient.getOperations());
        patient.setOperations(operations);
        assertTrue(patient.getOperations().size() == operations.size());
        assertTrue(patient.getOperations().containsAll(operations));

        System.out.println("Tested set of Operation !");
    }

    @After
    public void afterPatientTest() {
        System.out.println("Test is finished !!!");
    }

}
