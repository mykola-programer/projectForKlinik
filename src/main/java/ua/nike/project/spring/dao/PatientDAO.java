package ua.nike.project.spring.dao;

import ua.nike.project.hibernate.entity.Patient;

import java.util.List;

public interface PatientDAO {

    void savePatient(Patient patient);

    Patient findPatient(int patientID);

    List<Patient> list();
}
