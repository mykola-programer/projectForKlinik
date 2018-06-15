package mock;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.Patient;
import ua.nike.project.spring.dao.PatientDAO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

public class PatientDAOImplMock implements PatientDAO {

    private static List<Patient> patients = new ArrayList<>();

    static {
        Patient patient1 = new Patient("Богдан", "Микола", "Орестович", 'Ч', "супровід", null, null);
        patients.add(patient1);
        patient1.setPatientId(patients.indexOf(patient1));

        Patient patient2 = new Patient("Іванов", "Іван", "Іванович", 'Ч', "пацієнт", null, null);
        patients.add(patient2);
        patient2.setPatientId(patients.indexOf(patient2));

        Patient patient3 = new Patient("Василов", "Василь", "Васильович", 'Ч', "пацієнт", null, null);
        patients.add(patient3);
        patient3.setPatientId(patients.indexOf(patient3));
    }

    @Override
    public int savePatient(Patient patient) {
        patients.add(patient);
        patient.setPatientId(patients.indexOf(patient));
        return patients.indexOf(patient);
    }

    @Override
    public Patient findPatient(int patientID) {
        return patients.get(patientID);
    }

    @Override
    public List<Patient> listPatients() {
        return patients;
    }

    @Override
    public void removePatient(int patientId) {
        patients.remove(patientId);
    }

}
