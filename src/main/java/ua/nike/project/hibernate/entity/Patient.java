package ua.nike.project.hibernate.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "patients")
public class Patient implements Serializable {

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

    @OneToMany(targetEntity = Operation.class, fetch = FetchType.LAZY, mappedBy = "patient")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Patient patient = (Patient) o;

        if (patientId != null ? !patientId.equals(patient.patientId) : patient.patientId != null) return false;
        if (surname != null ? !surname.equals(patient.surname) : patient.surname != null) return false;
        if (firstName != null ? !firstName.equals(patient.firstName) : patient.firstName != null) return false;
        if (secondName != null ? !secondName.equals(patient.secondName) : patient.secondName != null) return false;
        if (sex != null ? !sex.equals(patient.sex) : patient.sex != null) return false;
        if (status != null ? !status.equals(patient.status) : patient.status != null) return false;
        if (relative != null ? !relative.equals(patient.relative) : patient.relative != null) return false;
        return telephone != null ? telephone.equals(patient.telephone) : patient.telephone == null;
    }

    @Override
    public int hashCode() {
        int result = patientId != null ? patientId.hashCode() : 0;
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (secondName != null ? secondName.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (relative != null ? relative.hashCode() : 0);
        result = 31 * result + (telephone != null ? telephone.hashCode() : 0);
        return result;
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
}
