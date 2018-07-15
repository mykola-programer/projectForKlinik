package ua.nike.project.spring.vo;

public class HospitalizationBeanVO {
    private int operationID;
    private int numberOfPlace;
    private int numberOfOrder;
    private int patientID;
    private String surname;
    private String firstName;
    private String secondName;
    private char sex;
    private String status;
    private int procedureID;
    private String procedure;
    private String eye;
    private String timeForCome;
    private String surgeon;
    private String manager;
    private String note;

    public int getOperationID() {
        return operationID;
    }

    public void setOperationID(int operationID) {
        this.operationID = operationID;
    }

    public int getNumberOfPlace() {
        return numberOfPlace;
    }

    public void setNumberOfPlace(int numberOfPlace) {
        this.numberOfPlace = numberOfPlace;
    }

    public int getNumberOfOrder() {
        return numberOfOrder;
    }

    public void setNumberOfOrder(int numberOfOrder) {
        this.numberOfOrder = numberOfOrder;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
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

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getProcedureID() {
        return procedureID;
    }

    public void setProcedureID(int procedureID) {
        this.procedureID = procedureID;
    }

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
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
