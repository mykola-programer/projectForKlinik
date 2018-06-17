package ua.nike.project.tests;

import ua.nike.project.hibernate.entity.Patient;

import java.util.TreeSet;

public class testComparator {
    public static void main(String[] args) {

//        System.out.println("a".compareTo("d"));

        Patient patient1 = new Patient();
        patient1.setPatientId(1);
        patient1.setSurname("a");
        patient1.setFirstName("c");
        patient1.setSecondName("c");
        patient1.setSex('h');

        Patient patient2 = new Patient();
        patient2.setPatientId(2);
        patient2.setSurname("a");
        patient2.setFirstName("b");
        patient2.setSecondName("c");
        patient2.setSex('g');

        TreeSet<Patient> patients = new TreeSet<>();
        System.out.println(patient1.hashCode());
        System.out.println(patient2.hashCode());
        System.out.println(patient1.equals(patient2));
        patients.add(patient1);
        patients.add(patient2);

        System.out.println(patients.size());
        for (Patient p : patients)
            System.out.println(p);
    }
}
