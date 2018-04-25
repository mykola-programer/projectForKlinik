package ua.nike.project.hibernate.entity;

import javax.persistence.*;
import java.sql.Time;

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "Operation.findAll", query = "FROM Operation "),
})

@Table(name = "operations")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer operationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_day_id")
    private OperationDay operationDay;

    private Time timeForCome;

    private Integer numberOfOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
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
        this.operationName = operationName.toUpperCase();
    }

    public String getEye() {
        return eye;
    }

    public void setEye(String eye) {
        this.eye = eye.toUpperCase();
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = firstUpperCase(manager);
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

        Operation operation = (Operation) o;

        if (operationId != null ? !operationId.equals(operation.operationId) : operation.operationId != null)
            return false;
        if (operationDay != null ? !operationDay.equals(operation.operationDay) : operation.operationDay != null)
            return false;
        if (timeForCome != null ? !timeForCome.equals(operation.timeForCome) : operation.timeForCome != null)
            return false;
        if (numberOfOrder != null ? !numberOfOrder.equals(operation.numberOfOrder) : operation.numberOfOrder != null)
            return false;
        if (patient != null ? !patient.equals(operation.patient) : operation.patient != null) return false;
        if (operationName != null ? !operationName.equals(operation.operationName) : operation.operationName != null)
            return false;
        if (eye != null ? !eye.equals(operation.eye) : operation.eye != null) return false;
        if (manager != null ? !manager.equals(operation.manager) : operation.manager != null) return false;
        return note != null ? note.equals(operation.note) : operation.note == null;
    }

    @Override
    public int hashCode() {
        int result = operationId != null ? operationId.hashCode() : 0;
        result = 31 * result + (operationDay != null ? operationDay.hashCode() : 0);
        result = 31 * result + (timeForCome != null ? timeForCome.hashCode() : 0);
        result = 31 * result + (numberOfOrder != null ? numberOfOrder.hashCode() : 0);
        result = 31 * result + (patient != null ? patient.hashCode() : 0);
        result = 31 * result + (operationName != null ? operationName.hashCode() : 0);
        result = 31 * result + (eye != null ? eye.hashCode() : 0);
        result = 31 * result + (manager != null ? manager.hashCode() : 0);
        result = 31 * result + (note != null ? note.hashCode() : 0);
        return result;
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
