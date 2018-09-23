package ua.nike.project.spring.vo;

import ua.nike.project.hibernate.type.Eye;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

public class VisitVO implements Serializable {

    private Integer visitId;
    private VisitDateVO visitDate;
    private LocalTime timeForCome;
    private Integer orderForCome;
    private ClientVO client;
    private String status;
    private ClientVO relative;
    private OperationTypeVO operationType;
    private Eye eye;
    private SurgeonVO surgeon;
    private ManagerVO manager;
    private AccomodationVO accomodation;
    private String note;

    public Integer getVisitId() {
        return visitId;
    }

    public void setVisitId(Integer visitId) {
        this.visitId = visitId;
    }

    public VisitDateVO getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(VisitDateVO visitDate) {
        this.visitDate = visitDate;
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

    public ClientVO getClient() {
        return client;
    }

    public void setClient(ClientVO client) {
        this.client = client;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ClientVO getRelative() {
        return relative;
    }

    public void setRelative(ClientVO relative) {
        this.relative = relative;
    }

    public OperationTypeVO getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationTypeVO operationType) {
        this.operationType = operationType;
    }

    public Eye getEye() {
        return eye;
    }

    public void setEye(Eye eye) {
        this.eye = eye;
    }

    public SurgeonVO getSurgeon() {
        return surgeon;
    }

    public void setSurgeon(SurgeonVO surgeon) {
        this.surgeon = surgeon;
    }

    public ManagerVO getManager() {
        return manager;
    }

    public void setManager(ManagerVO manager) {
        this.manager = manager;
    }

    public AccomodationVO getAccomodation() {
        return accomodation;
    }

    public void setAccomodation(AccomodationVO accomodation) {
        this.accomodation = accomodation;
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
        return Objects.equals(visitDate, visitVO.visitDate) &&
                Objects.equals(timeForCome, visitVO.timeForCome) &&
                Objects.equals(orderForCome, visitVO.orderForCome) &&
                Objects.equals(client, visitVO.client) &&
                status == visitVO.status &&
                Objects.equals(relative, visitVO.relative) &&
                Objects.equals(operationType, visitVO.operationType) &&
                eye == visitVO.eye &&
                Objects.equals(surgeon, visitVO.surgeon) &&
                Objects.equals(manager, visitVO.manager) &&
                Objects.equals(accomodation, visitVO.accomodation) &&
                Objects.equals(note, visitVO.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(visitDate, timeForCome, orderForCome, client, status, relative, operationType, eye, surgeon, manager, accomodation, note);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VisitVO{");
        sb.append("visitId=").append(visitId);
        sb.append(", visitDate=").append(visitDate);
        sb.append(", timeForCome=").append(timeForCome);
        sb.append(", orderForCome=").append(orderForCome);
        sb.append(", client=").append(client);
        sb.append(", status=").append(status);
        sb.append(", relative=").append(relative);
        sb.append(", operationType=").append(operationType);
        sb.append(", eye=").append(eye);
        sb.append(", surgeon=").append(surgeon);
        sb.append(", manager=").append(manager);
        sb.append(", accomodation=").append(accomodation);
        sb.append(", note='").append(note).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
