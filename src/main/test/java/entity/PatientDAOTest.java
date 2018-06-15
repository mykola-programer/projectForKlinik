package entity;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.nike.project.hibernate.entity.Patient;
import ua.nike.project.spring.dao.PatientDAO;
import ua.nike.project.spring.exceptions.BusinessException;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/applicationContextTest.xml")
public class PatientDAOTest {

    @Autowired
    private PatientDAO patientDAO;

    private Patient patient1;
    private Patient patient2;
    private Patient patient3;
    private Patient patient4;

    private int defaultSize = 3;

    @Before
    public void init() {

        System.out.println("Start !!!");

        patient1 = new Patient("Богдан", "Микола", "Орестович", 'Ч', "супровід", null, null);
        patient2 = new Patient("Іванов", "Іван", "Іванович", 'Ч', "пацієнт", null, null);
        patient3 = new Patient("Василов", "Василь", "Васильович", 'Ч', "пацієнт", null, null);
        patient4 = new Patient("Олександров", "Олександр", "Олександрович", 'Ч', "супроводжуючий", null, null);
    }

    @Test
    public void AaddPatient() {
        assertEquals(patientDAO.listPatients().size(), defaultSize);
        Patient patient = new Patient("Олександров", "Олександр", "Олександрович", 'Ч', "супроводжуючий", null, null);
        int index = patientDAO.savePatient(patient);
        patient.setPatientId(index);
        assertEquals(patientDAO.listPatients().size(), defaultSize + 1);

        System.out.println("1. Test addPatient is finished !!!");
    }

    @Test
    public void BfindPatient() throws BusinessException {

        System.out.println("  0. " + patientDAO.findPatient(0));
        assertEquals(patient1, patientDAO.findPatient(0));
        System.out.println("  1. " + patientDAO.findPatient(1));
        assertEquals(patient2, patientDAO.findPatient(1));
        System.out.println("  2. " + patientDAO.findPatient(2));
        assertEquals(patient3, patientDAO.findPatient(2));
        if (patientDAO.listPatients().size() == 4) {
            System.out.println("  3. " + patientDAO.findPatient(3));
            assertEquals(patient4, patientDAO.findPatient(3));
        }
        System.out.println("2. Test findPatient is finished !!!");

    }

    @Test
    public void CdeletePatient() throws BusinessException {
        assertEquals(patientDAO.listPatients().size(), defaultSize + 1);
        patientDAO.removePatient(3);
        assertEquals(patientDAO.listPatients().size(), defaultSize);
        System.out.println("3. Test deletePatient is finished !!!");
    }
}
