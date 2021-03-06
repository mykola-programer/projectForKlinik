package ua.nike.project.hibernate.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ua.nike.project.hibernate.type.ClientStatus;
import ua.nike.project.hibernate.type.Eye;
import ua.nike.project.hibernate.type.PostgreSQLEnumType;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "Visit.findAll", query = "FROM Visit "),
        @NamedQuery(name = "Visit.findAllByDate", query = "FROM Visit v WHERE v.surgeonPlan.datePlan.date = ?1 "),
})
@Table(name = "visit", uniqueConstraints = {
        @UniqueConstraint(name = "visit_uk", columnNames = {"client_id", "operation_type_id", "surgeon_plan_id", "eye"}),
//        @UniqueConstraint(name = "visit_date_order", columnNames = {"visit_date_id", "order_for_come"})
})
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
public class Visit implements EntityObject {

    @Version
    private long version;

    @Id
    @Column(name = "visit_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer visitId;

    @Column(name = "time_for_come")
    private LocalTime timeForCome;

    @Column(name = "order_for_come")
    @Min(1)
    private Integer orderForCome;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "client_status")
    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    private ClientStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    private Client patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operation_type_id")
    private OperationType operationType;

    @Column(name = "eye")
    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    private Eye eye;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accomodation_id")
    private Accomodation accomodation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "surgeon_plan_id")
    private SurgeonPlan surgeonPlan;

    private String note;

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Integer getVisitId() {
        return visitId;
    }

    public void setVisitId(Integer visitId) {
        this.visitId = visitId;
    }

    public SurgeonPlan getSurgeonPlan() {
        return surgeonPlan;
    }

    public void setSurgeonPlan(SurgeonPlan surgeonPlan) {
        this.surgeonPlan = surgeonPlan;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ClientStatus getStatus() {
        return status;
    }

    public void setStatus(ClientStatus status) {
        this.status = status;
    }

    public Client getPatient() {
        return patient;
    }

    public void setPatient(Client relative) {
        this.patient = relative;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Eye getEye() {
        return eye;
    }

    public void setEye(Eye eye) {
        this.eye = eye;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Accomodation getAccomodation() {
        return accomodation;
    }

    public void setAccomodation(Accomodation accomodation) {
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
        Visit visit = (Visit) o;
        return Objects.equals(client, visit.client) &&
                Objects.equals(operationType, visit.operationType) &&
                eye == visit.eye &&
                Objects.equals(surgeonPlan, visit.surgeonPlan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, operationType, eye, surgeonPlan);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Visit{");
        sb.append("version=").append(version);
        sb.append(", visitId=").append(visitId);
        sb.append(", timeForCome=").append(timeForCome);
        sb.append(", orderForCome=").append(orderForCome);
        sb.append(", client=").append(client);
        sb.append(", status=").append(status);
        sb.append(", patient=").append(patient);
        sb.append(", operationType=").append(operationType);
        sb.append(", eye=").append(eye);
        sb.append(", manager=").append(manager);
        sb.append(", accomodation=").append(accomodation);
        sb.append(", surgeonPlan=").append(surgeonPlan);
        sb.append(", note='").append(note).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
