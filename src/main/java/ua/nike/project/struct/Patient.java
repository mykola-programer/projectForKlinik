package ua.nike.project.struct;

import java.util.Objects;

public class Patient {
    private Integer patient_id;
    private String surname;
    private String firstName;
    private String secondName;
    private Character sex;
    private String status;

    /**
     * Link to another patient, who is a relative of this patient.
     */
    private Integer relative_id;
    private String telephone;

    /**
     * Initializes a newly created {@code Patient} object.
     */
    public Patient() {
    }

    /**
     * Initializes a newly created {@code Patient} object with parameters.
     *
     * @param patient_id  ID in the database
     * @param surname     patient.
     * @param firstName   patient.
     * @param secondName  patient.
     * @param sex         patient.
     * @param status      patient. Is a patient or relative.
     * @param relative_id Link to another patient, who is a relative of this patient.
     * @param telephone   patient. Format : "+380508888888".
     */
    @Deprecated
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

    /**
     * Returns the {@code Integer} value of the patient_id from patient
     */
    public Integer getPatient_id() {
        if (patient_id == null) {
            patient_id = 0;
        }
        return patient_id;
    }

    /**
     * Set patient_id
     *
     * @param patient_id
     */
    public void setPatient_id(Integer patient_id) {
        if (patient_id == null) {
            patient_id = 0;
        }
        this.patient_id = patient_id;
    }

    /**
     * Returns the {@code String} value of the surname from patient.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Set surname and convert it to firstUpperCase.
     *
     * @param surname
     */
    public void setSurname(String surname) {
        this.surname = firstUpperCase(surname);
    }

    /**
     * Returns the {@code String} value of the firstName from patient.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set firstName and convert it to firstUpperCase.
     *
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstUpperCase(firstName);
    }

    /**
     * Returns the {@code String} value of the secondName from patient.
     */
    public String getSecondName() {
        return secondName;
    }

    /**
     * Set secondName and convert it to firstUpperCase.
     *
     * @param secondName
     */
    public void setSecondName(String secondName) {
        this.secondName = firstUpperCase(secondName);
    }

    /**
     * Returns the {@code Character} value of the sex from patient.
     */
    public Character getSex() {
        return sex;
    }

    /**
     * Set sex.
     *
     * @param sex
     */
    public void setSex(Character sex) {
        this.sex = sex;
    }

    /**
     * Returns the {@code String} value of the status from patient.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set status and convert it to LowerCase.
     *
     * @param status
     */
    public void setStatus(String status) {
        this.status = status.toLowerCase();
    }

    /**
     * Returns the {@code Integer} value of the relative_id from patient
     */
    public Integer getRelative_id() {
        return relative_id;
    }

    /**
     * Set relative_id.
     *
     * @param relative_id
     */
    public void setRelative_id(Integer relative_id) {
        this.relative_id = relative_id;
    }

    /**
     * Returns the {@code String} value of the telephone from patient.
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Set telephone.
     *
     * @param telephone
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * Convert first symbol in this {@code String} to Upper Case. Another symbols is Lower Case.
     *
     * @param word This is a {@code String}, that needs to be converted to a specific format.
     * @return {@code String} first symbol to Upper Case and another symbols is Lower Case.
     * If word is null or "", that return "".
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
        return "Patient " + patient_id + ". {" +
                " " + surname + " " + firstName + " " + secondName + " " +
                "(" + sex + ")" +
                " - " + status + ". " +
                " Тел.='" + telephone +
                '}';
    }
}
