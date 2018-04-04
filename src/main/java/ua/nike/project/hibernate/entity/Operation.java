package ua.nike.project.hibernate.entity;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Table (name = "operations")
public class Operation {
    @Id
    @GeneratedValue
    private Integer operationId;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "operation_day_id")
    private OperationDay operationDay;

    private Time timeForCome;

    private Integer numberOfOrder;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "patient_id")
    private Patient patient;

    private String operationName;

    private String eye;

    private String manager;

    private String note;

    public Integer getOperationId() {
        return operationId;
    }

    public void setOperationId(Integer operationId) {
        this.operationId = operationId;
    }

    public OperationDay getOperationDay() {
        return operationDay;
    }

    public void setOperationDay(OperationDay operationDay) {
        this.operationDay = operationDay;
    }

    public Time getTimeForCome() {
        return timeForCome;
    }

    public void setTimeForCome(Time timeForCome) {
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

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getEye() {
        return eye;
    }

    public void setEye(String eye) {
        this.eye = eye;
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

    @Override
    public String toString() {
        return "Operation{" +
                "operationId=" + operationId +
                ", operationDay=" + operationDay +
                ", timeForCome=" + timeForCome +
                ", numberOfOrder=" + numberOfOrder +
                ", patient=" + patient +
                ", operationName='" + operationName + '\'' +
                ", eye='" + eye + '\'' +
                ", manager='" + manager + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
