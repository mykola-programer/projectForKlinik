package ua.nike.project.spring.dao;

import ua.nike.project.spring.beans.entity.Patient;

import java.util.List;

public interface PatientDAO {

    void savePatient(Patient patient);

    Patient findPatient(int patientID);

    List<Patient> list();
}
