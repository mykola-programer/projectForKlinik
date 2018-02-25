package ua.nike.project;

import ua.nike.project.service.JdbcStoragePatient;
import ua.nike.project.struct.Patient;

public class TestMain {
    public static void main(String[] args) {

        JdbcStoragePatient jdbcStoragePatient = new JdbcStoragePatient();
        for (Patient patient : jdbcStoragePatient.getPatients()) {
            System.out.println(patient);
        }
        System.out.println("---------------------");
        {
            Patient patient = new Patient();
            patient.setPatient_id(null);
            patient.setSurname("Мельник");
            patient.setFirstName("Марія");
            patient.setSecondName("Іванівна");
            patient.setSex('Ж');
            patient.setStatus("супроводжуючий");
            patient.setRelative_id(null);
            patient.setTelephone("+380638326767");

            System.out.println(jdbcStoragePatient.editPatient(8, patient));
        }
        for (Patient patient : jdbcStoragePatient.getPatients()) {
            System.out.println(patient);
        }
    }
}
