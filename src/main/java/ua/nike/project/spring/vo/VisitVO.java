package ua.nike.project.spring.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ua.nike.project.spring.service.convert.ArrayToLocalTimeConverter;
import ua.nike.project.spring.service.convert.LocalTimeToArrayConverter;

import javax.validation.constraints.PositiveOrZero;
import java.time.LocalTime;
import java.util.Objects;

public class VisitVO implements VisualObject {

    private int visitId;
    @JsonSerialize(converter = LocalTimeToArrayConverter.class)
    @JsonDeserialize(converter = ArrayToLocalTimeConverter.class)
    private LocalTime timeForCome;
    @PositiveOrZero
    private Integer orderForCome;
    @PositiveOrZero
    private int clientID;
    private String status;
    @PositiveOrZero
    private int patientID;
    @PositiveOrZero
    private int operationTypeID;

    //    @Pattern(regexp = "^(OD|OS|OU)$", message = "visit.eye.pattern")
    private String eye;
    @PositiveOrZero
    private int managerID;
    @PositiveOrZero
    private int accomodationID;
    @PositiveOrZero
    private int surgeonPlanId;
    private String note;

    public int getVisitId() {
        return visitId;
    }

    public void setVisitId(int visitId) {
        this.visitId = visitId;
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

    public int getSurgeonPlanId() {
        return surgeonPlanId;
    }

    public void setSurgeonPlanId(int surgeonPlanId) {
        this.surgeonPlanId = surgeonPlanId;
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
        return clientID == visitVO.clientID &&
                operationTypeID == visitVO.operationTypeID &&
                surgeonPlanId == visitVO.surgeonPlanId &&
                Objects.equals(eye, visitVO.eye);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientID, operationTypeID, eye, surgeonPlanId);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VisitVO{");
        sb.append("visitId=").append(visitId);
        sb.append(", timeForCome=").append(timeForCome);
        sb.append(", orderForCome=").append(orderForCome);
        sb.append(", clientID=").append(clientID);
        sb.append(", status='").append(status).append('\'');
        sb.append(", patientID=").append(patientID);
        sb.append(", operationTypeID=").append(operationTypeID);
        sb.append(", eye='").append(eye).append('\'');
        sb.append(", managerID=").append(managerID);
        sb.append(", accomodationID=").append(accomodationID);
        sb.append(", surgeonPlanId=").append(surgeonPlanId);
        sb.append(", note='").append(note).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
