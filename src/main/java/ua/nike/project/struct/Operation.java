package ua.nike.project.struct;

import java.util.Date;

public class Operation {
//    private Integer index = null;
    private OperationDay operationDate; // ???????????? дата операційного дня    ????????????????
    private Date timeForCome; // час приходу в день операції
    private Integer numberOfOrder; // номер по порядку виклику в операційну
    private Patient patient;   //  ??????????????????
    private String operationAndEye;
    private String operation;
    private String eye;
    private String surgeon; // хірург, який буде робити операцію
    private String manager; // менеджер, який оформляв на операцію
    private String roomAndPlace; // палата та ліжко для паціента
    private String note; // палата та ліжко для паціента

    public Operation(OperationDay operationDate, Date timeForCome, Integer numberOfOrder, Patient patient, String operation, String eye, String surgeon, String manager, String roomAndPlace, String note) {
        this.operationDate = operationDate;
        setOperationDate(operationDate);
        setTimeForCome(timeForCome);
        setNumberOfOrder(numberOfOrder);
        setPatient(patient);
        setOperation(operation);
        setEye(eye);
        setOperationAndEye(operation, eye);
        setSurgeon(surgeon);
        setManager(manager);
        setRoomAndPlace(roomAndPlace);
        setNote(note);
    }

    public OperationDay getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(OperationDay operationDate) {
        this.operationDate = operationDate;
    }

    public Date getTimeForCome() {
        return timeForCome;
    }

    public void setTimeForCome(Date timeForCome) {
        this.timeForCome = timeForCome;
    }

    public Integer getNumberOfOrder() {
        return numberOfOrder;
    }

    public void setNumberOfOrder(Integer numberOfOrder) {
        this.numberOfOrder = numberOfOrder;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getOperationAndEye() {
        return operationAndEye;
    }

    public void setOperationAndEye(String operation, String eye) {
        this.operationAndEye = operation + " " + eye.toUpperCase();
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getEye() {
        return eye;
    }

    public void setEye(String eye) {
        this.eye = eye.toUpperCase();
    }

    public String getSurgeon() {
        return surgeon;
    }

    public void setSurgeon(String surgeon) {
        this.surgeon = firstUpperCase(surgeon);
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = firstUpperCase(manager);
    }

    public String getRoomAndPlace() {
        return roomAndPlace;
    }

    public void setRoomAndPlace(String roomAndPlace) {
        this.roomAndPlace = roomAndPlace;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    // Create first point to Upper Case
    private String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }
}
