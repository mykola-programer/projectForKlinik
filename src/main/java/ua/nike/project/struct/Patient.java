package ua.nike.project.struct;

import java.util.Objects;

public class Patient {
    private Integer patient_id;
    private String surname;
    private String firstName;
    private String secondName;
    private Character sex;
    private String status;
    private Integer relative_id;
    private String telephone;

    public Patient() {
    }

    public Patient(Integer patient_id, String surname, String firstName, String secondName, Character sex, String status, Integer relative_id, String telephone) {
        setPatient_id(patient_id);
        setSurname(surname);
        setFirstName(firstName);
        setSecondName(secondName);
        setSex(sex);
        setStatus(status);
        setRelative_id(relative_id);
        setTelephone(telephone);
    }

    public Integer getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(Integer patient_id) {
        this.patient_id = patient_id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = firstUpperCase(surname);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstUpperCase(firstName);
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = firstUpperCase(secondName);
    }

    public Character getSex() {
        return sex;
    }

    public void setSex(Character sex) {
        this.sex = sex;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status.toLowerCase();
    }

    public Integer getRelative_id() {
        return relative_id;
    }

    public void setRelative_id(Integer relative_id) {
        this.relative_id = relative_id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * Create first point to Upper Case. Another case is Lower Case.
     *
     * @param word - це будь яке слово типу String, яке треба вивести з великої букви
     * @return String - повертає теж саме слово, але з великої букви.
     * Якщо переданий пареметр пуста строка, чи пуста ссилка, то метод повертає пусту строку.
     */
    private String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;
        Patient patient = (Patient) o;
        return Objects.equals(patient_id, patient.patient_id) &&
                Objects.equals(surname, patient.surname) &&
                Objects.equals(firstName, patient.firstName) &&
                Objects.equals(secondName, patient.secondName) &&
                Objects.equals(sex, patient.sex) &&
                Objects.equals(status, patient.status) &&
                Objects.equals(relative_id, patient.relative_id) &&
                Objects.equals(telephone, patient.telephone);
    }

    @Override
    public int hashCode() {

        return Objects.hash(patient_id, surname, firstName, secondName, sex, status, relative_id, telephone);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "Індекс=" + patient_id +
                ", Прізвище='" + surname + '\'' +
                ", Ім'я='" + firstName + '\'' +
                ", По-батькові='" + secondName + '\'' +
                ", Стать=" + sex +
                ", Статус='" + status + '\'' +
                ", Телефон='" + telephone + '\'' +
                '}';
    }
}
