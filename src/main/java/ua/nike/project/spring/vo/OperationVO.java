package ua.nike.project.spring.vo;

import java.io.Serializable;

public class OperationVO implements Serializable {
    private Integer operationId;
    private Integer operationDateID;
    private String timeForCome;
    private Integer numberOfOrder;
    private Integer patientID;
    private Integer procedureID;
    private String procedureName;
    private String eye;
    private String surgeon;
    private String manager;
    private Integer hospitalizationID;
    private String note;

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public Integer getOperationId() {
        return operationId;
    }

    public void setOperationId(Integer operationId) {
        this.operationId = operationId;
    }

    public Integer getOperationDateID() {
        return operationDateID;
    }

    public void setOperationDateID(Integer operationDateID) {
        this.operationDateID = operationDateID;
    }

    public String getTimeForCome() {
        return timeForCome;
    }

    public void setTimeForCome(String timeForCome) {
        this.timeForCome = timeForCome;
    }

    public Integer getNumberOfOrder() {
        return numberOfOrder;
    }

    public void setNumberOfOrder(Integer numberOfOrder) {
        this.numberOfOrder = numberOfOrder;
    }

    public Integer getPatientID() {
        return patientID;
    }

    public void setPatientID(Integer patientID) {
        this.patientID = patientID;
    }

    public Integer getProcedureID() {
        return procedureID;
    }

    public void setProcedureID(Integer procedureID) {
        this.procedureID = procedureID;
    }

    public String getEye() {
        return eye;
    }

    public void setEye(String eye) {
        this.eye = eye;
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

    public Integer getHospitalizationID() {
        return hospitalizationID;
    }

    public void setHospitalizationID(Integer hospitalizationID) {
        this.hospitalizationID = hospitalizationID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
