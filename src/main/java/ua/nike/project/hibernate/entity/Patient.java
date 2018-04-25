package ua.nike.project.hibernate.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@NamedQuery(name = "getPatients", query = "FROM Patient ")
@Table(name = "patients")
public class Patient implements Serializable, Comparable<Patient> {

    @Version
    private long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Integer patientId;

    @Column(name = "surname")
    private String surname;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "sex")
    private Character sex;

    @Column(name = "status")
    private String status;

    @OneToOne(fetch = FetchType.LAZY)
    private Patient relative;

    @Column(name = "telephone")
    private String telephone;

    @OneToMany(targetEntity = Operation.class, fetch = FetchType.LAZY, mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Operation> operations;

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
        this.sex = Character.toUpperCase(sex);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status.toLowerCase();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Patient getRelative() {
        return relative;
    }

    public void setRelative(Patient relative) {
        this.relative = relative;
    }

    public Set<Operation> getOperations() {
        return operations;
    }

    public void setOperations(Set<Operation> operations) {
        this.operations = operations;
    }

    private String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Patient patient = (Patient) o;

        if (patientId != null ? !patientId.equals(patient.patientId) : patient.patientId != null) return false;
        if (surname != null ? !surname.equals(patient.surname) : patient.surname != null) return false;
        if (firstName != null ? !firstName.equals(patient.firstName) : patient.firstName != null) return false;
        if (secondName != null ? !secondName.equals(patient.secondName) : patient.secondName != null) return false;
        return sex != null ? !sex.equals(patient.sex) : patient.sex != null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientId, surname, firstName, secondName, sex);
    }

    @Override
    public String toString() {
        return "Patient{" +
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
    public int compareTo(Patient patient) {
        final int UP = -1;
        final int DOWN = 1;
        final int EQUALS = 0;

        if (this.surname != null && patient.surname != null) {
            if (this.surname.compareTo(patient.surname) != EQUALS) return this.surname.compareTo(patient.surname);
        } else if (this.surname != null) return UP;
        else if (patient.surname != null) return DOWN;

        // ---------------------------------------------------------------- //

        if (this.firstName != null && patient.firstName != null) {
            if (this.firstName.compareTo(patient.firstName) != EQUALS)
                return this.firstName.compareTo(patient.firstName);
        } else if (this.firstName != null) return UP;
        else if (patient.firstName != null) return DOWN;

        // ---------------------------------------------------------------- //

        if (this.secondName != null && patient.secondName != null) {
            if (this.secondName.compareTo(patient.secondName) != EQUALS)
                return this.secondName.compareTo(patient.secondName);
        } else if (this.secondName != null) return UP;
        else if (patient.secondName != null) return DOWN;

        // ---------------------------------------------------------------- //

        if (this.sex != null && patient.sex != null) {
            if (this.sex.compareTo(patient.sex) != EQUALS) return this.sex.compareTo(patient.sex);
        } else if (this.sex != null) return UP;
        else if (patient.sex != null) return DOWN;

        // ---------------------------------------------------------------- //

        if (this.patientId != null && patient.patientId != null) {
            if (this.patientId.compareTo(patient.patientId) != EQUALS)
                return this.patientId.compareTo(patient.patientId);
        } else if (this.patientId != null) return UP;
        else if (patient.patientId != null) return DOWN;

        // ---------------------------------------------------------------- //

        return EQUALS;
    }
}
