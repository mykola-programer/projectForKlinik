package ua.nike.project.spring.dao;

import ua.nike.project.hibernate.entity.Patient;

import java.util.List;

public interface PatientDAO {

    int savePatient(Patient patient);

    Patient findPatient(int patientID);

    List<Patient> listPatients();
}
