package ua.nike.project.spring.vo;

import javax.validation.constraints.PositiveOrZero;
import java.time.LocalTime;
import java.util.Objects;

public class VisitVO implements VisualObject {

    private int visitId;
    @PositiveOrZero
    private int visitDateID;
    private LocalTime timeForCome;
    @PositiveOrZero
    private Integer orderForCome;
    @PositiveOrZero
    private int clientID;
    private String status;
    private int patientID;
    private int operationTypeID;

    //    @Pattern(regexp = "^(OD|OS|OU)$", message = "visit.eye.pattern")
    private String eye;
    private int surgeonID;
    private int managerID;
    private int accomodationID;
    private String note;

    public Integer getVisitId() {
        return visitId;
    }

    public void setVisitId(Integer visitId) {
        this.visitId = visitId;
    }

    public int getVisitDateID() {
        return visitDateID;
    }

    public void setVisitDateID(int visitDateID) {
        this.visitDateID = visitDateID;
    }

    public LocalTime getTimeForCome() {
        return timeForCome;
    }

    public void setTimeForCome(LocalTime timeForCome) {
        this.timeForCome = timeForCome;
    }

    public Integer getOrderForCome() {
        return orderForCome;
    }

    public void setOrderForCome(Integer orderForCome) {
        this.orderForCome = orderForCome;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public int getOperationTypeID() {
        return operationTypeID;
    }

    public void setOperationTypeID(int operationTypeID) {
        this.operationTypeID = operationTypeID;
    }

    public String getEye() {
        return eye;
    }

    public void setEye(String eye) {
        this.eye = eye;
    }

    public int getSurgeonID() {
        return surgeonID;
    }

    public void setSurgeonID(int surgeonID) {
        this.surgeonID = surgeonID;
    }

    public int getManagerID() {
        return managerID;
    }

    public void setManagerID(int managerID) {
        this.managerID = managerID;
    }

    public int getAccomodationID() {
        return accomodationID;
    }

    public void setAccomodationID(int accomodationID) {
        this.accomodationID = accomodationID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisitVO visitVO = (VisitVO) o;
        return visitDateID == visitVO.visitDateID &&
                clientID == visitVO.clientID &&
                operationTypeID == visitVO.operationTypeID &&
                Objects.equals(eye, visitVO.eye);
    }

    @Override
    public int hashCode() {
        return Objects.hash(visitDateID, clientID, operationTypeID, eye);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VisitVO{");
        sb.append("visitId=").append(visitId);
        sb.append(", visitDateID=").append(visitDateID);
        sb.append(", timeForCome=").append(timeForCome);
        sb.append(", orderForCome=").append(orderForCome);
        sb.append(", clientID=").append(clientID);
        sb.append(", status='").append(status).append('\'');
        sb.append(", patientID=").append(patientID);
        sb.append(", operationTypeID=").append(operationTypeID);
        sb.append(", eye='").append(eye).append('\'');
        sb.append(", surgeonID=").append(surgeonID);
        sb.append(", managerID=").append(managerID);
        sb.append(", accomodationID=").append(accomodationID);
        sb.append(", note='").append(note).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
