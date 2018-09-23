package ua.nike.project.spring.vo.ignore;

import java.io.Serializable;

public class HospitalizationBeanVO implements Serializable {
    private Integer dateID;

    private Integer hospitalizationID;
    private Integer numberOfPlace;

    private Integer patientID;
    private String surname;
    private String firstName;
    private String secondName;
    private Character sex;
    private String status;

    private Integer operationID;
    private Integer numberOfOrder;
    private Integer operationTypeID;
    private String operationType;
    private String eye;
    private String timeForCome;
    private String surgeon;
    private String manager;
    private String note;

    public Integer getDateID() {
        return dateID;
    }

    public void setDateID(Integer dateID) {
        this.dateID = dateID;
    }

    public Integer getHospitalizationID() {
        return hospitalizationID;
    }

    public void setHospitalizationID(Integer hospitalizationID) {
        this.hospitalizationID = hospitalizationID;
    }

    public Integer getNumberOfPlace() {
        return numberOfPlace;
    }

    public void setNumberOfPlace(Integer numberOfPlace) {
        this.numberOfPlace = numberOfPlace;
    }

    public Integer getPatientID() {
        return patientID;
    }

    public void setPatientID(Integer patientID) {
        this.patientID = patientID;
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

    public Integer getOperationID() {
        return operationID;
    }

    public void setOperationID(Integer operationID) {
        this.operationID = operationID;
    }

    public Integer getNumberOfOrder() {
        return numberOfOrder;
    }

    public void setNumberOfOrder(Integer numberOfOrder) {
        this.numberOfOrder = numberOfOrder;
    }

    public Integer getOperationTypeID() {
        return operationTypeID;
    }

    public void setOperationTypeID(Integer operationTypeID) {
        this.operationTypeID = operationTypeID;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getEye() {
        return eye;
    }

    public void setEye(String eye) {
        this.eye = eye;
    }

    public String getTimeForCome() {
        return timeForCome;
    }

    public void setTimeForCome(String timeForCome) {
        this.timeForCome = timeForCome;
    }

    public String getSurgeon() {
        return surgeon;
    }

    public void setSurgeon(String surgeon) {
        this.surgeon = surgeon;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}