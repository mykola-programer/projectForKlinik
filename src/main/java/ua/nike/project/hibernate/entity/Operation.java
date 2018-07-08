package ua.nike.project.hibernate.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.time.LocalTime;

@Entity
@NamedQuery(name = "Operation.findAll", query = "FROM Operation ")
@Table(name = "operations")
public class Operation implements Serializable {

    @Version
    private long version;

    @Id
    @Column(name = "operation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer operationId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operation_day")
    private OperationDay operationDay;

    private LocalTime timeForCome;

    private Integer numberOfOrder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient")
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "procedure")
    private Procedure procedure;

    private String eye;

    private String manager;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hospitalization")
    private Hospitalization hospitalization;

    private String note;

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Hospitalization getHospitalization() {
        return hospitalization;
    }

    public void setHospitalization(Hospitalization hospitalization) {
        this.hospitalization = hospitalization;
    }

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

    public LocalTime getTimeForCome() {
        return timeForCome;
    }

    public void setTimeForCome(LocalTime timeForCome) {
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

    public Procedure getProcedure() {
        return procedure;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
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

        if (!operationDay.equals(operation.operationDay)) return false;
        if (timeForCome != null ? !timeForCome.equals(operation.timeForCome) : operation.timeForCome != null)
            return false;
        if (numberOfOrder != null ? !numberOfOrder.equals(operation.numberOfOrder) : operation.numberOfOrder != null)
            return false;
        if (!patient.equals(operation.patient)) return false;
        if (!procedure.equals(operation.procedure)) return false;
        if (!eye.equals(operation.eye)) return false;
        return manager != null ? manager.equals(operation.manager) : operation.manager == null;
    }

    @Override
    public int hashCode() {
        int result = operationDay.hashCode();
        result = 31 * result + (timeForCome != null ? timeForCome.hashCode() : 0);
        result = 31 * result + (numberOfOrder != null ? numberOfOrder.hashCode() : 0);
        result = 31 * result + patient.hashCode();
        result = 31 * result + procedure.hashCode();
        result = 31 * result + eye.hashCode();
        result = 31 * result + (manager != null ? manager.hashCode() : 0);
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
                ", procedure=" + procedure +
                ", eye='" + eye + '\'' +
                ", manager='" + manager + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
