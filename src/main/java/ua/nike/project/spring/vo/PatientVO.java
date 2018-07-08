package ua.nike.project.spring.vo;

import java.util.Objects;

public class PatientVO implements Comparable<PatientVO>{

    private Integer patientId;
    private String surname;
    private String firstName;
    private String secondName;
    private Character sex;
    private String status;
    private PatientVO relative;
    private String telephone;

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
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
        this.status = status;
    }

    public PatientVO getRelative() {
        return relative;
    }

    public void setRelative(PatientVO relative) {
        this.relative = relative;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PatientVO patient = (PatientVO) o;

        return Objects.equals(surname, patient.surname) &&
                Objects.equals(firstName, patient.firstName) &&
                Objects.equals(secondName, patient.secondName) &&
                Objects.equals(sex, patient.sex);

    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, firstName, secondName, sex);
    }

    @Override
    public String toString() {
        return "PatientVO{" +
                "patientId=" + patientId +
                ", surname='" + surname + '\'' +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", sex=" + sex +
                ", status='" + status + '\'' +
                ", relativeId=" + relative +
                ", telephone='" + telephone + '\'' +
                '}';
    }

    @Override
    public int compareTo(PatientVO patient) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;

        if (this == patient) return EQUAL;


        if (this.surname != null && patient.surname != null) {
            if (this.surname.compareTo(patient.surname) != EQUAL) return this.surname.compareTo(patient.surname);
        } else if (this.surname != null) return BEFORE;
        else if (patient.surname != null) return AFTER;

        // ---------------------------------------------------------------- //

        if (this.firstName != null && patient.firstName != null) {
            if (this.firstName.compareTo(patient.firstName) != EQUAL)
                return this.firstName.compareTo(patient.firstName);
        } else if (this.firstName != null) return BEFORE;
        else if (patient.firstName != null) return AFTER;

        // ---------------------------------------------------------------- //

        if (this.secondName != null && patient.secondName != null) {
            if (this.secondName.compareTo(patient.secondName) != EQUAL)
                return this.secondName.compareTo(patient.secondName);
        } else if (this.secondName != null) return BEFORE;
        else if (patient.secondName != null) return AFTER;

        // ---------------------------------------------------------------- //

        if (this.sex != null && patient.sex != null) {
            if (this.sex.compareTo(patient.sex) != EQUAL) return this.sex.compareTo(patient.sex);
        } else if (this.sex != null) return BEFORE;
        else if (patient.sex != null) return AFTER;

        // ---------------------------------------------------------------- //

        return EQUAL;
    }
}
