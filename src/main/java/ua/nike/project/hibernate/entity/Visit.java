package ua.nike.project.hibernate.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ua.nike.project.hibernate.type.ClientStatus;
import ua.nike.project.hibernate.type.Eye;
import ua.nike.project.hibernate.type.PostgreSQLEnumType;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "Visit.findAll", query = "FROM Visit "),
        @NamedQuery(name = "Visit.findByClient", query = "FROM Visit v WHERE v.client = :client"),
        @NamedQuery(name = "Visit.findByPatient", query = "FROM Visit v WHERE v.patient = :patient"),
        @NamedQuery(name = "Visit.findByAccomodation", query = "FROM Visit v WHERE v.accomodation = :accomodation")
})
@Table(name = "visit")
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "visit_date_id")
    private VisitDate visitDate;

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
    @JoinColumn(name = "surgeon_id")
    private Surgeon surgeon;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accomodation_id")
    private Accomodation accomodation;

    private String note;

    @Column(name = "inactive")
    private Boolean inactive;

    public Visit() {
    }

    public Visit(long version, VisitDate visitDate, LocalTime timeForCome, @Min(1) Integer orderForCome, Client client, ClientStatus status, Client patient, OperationType operationType, Eye eye, Surgeon surgeon, Manager manager, Accomodation accomodation, String note) {
        this.version = version;
        this.visitDate = visitDate;
        this.timeForCome = timeForCome;
        this.orderForCome = orderForCome;
        this.client = client;
        this.status = status;
        this.patient = patient;
        this.operationType = operationType;
        this.eye = eye;
        this.surgeon = surgeon;
        this.manager = manager;
        this.accomodation = accomodation;
        this.note = note;
    }

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

    public VisitDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(VisitDate visitDate) {
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

    public Surgeon getSurgeon() {
        return surgeon;
    }

    public void setSurgeon(Surgeon surgeon) {
        this.surgeon = surgeon;
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

    public Boolean getInactive() {
        return inactive;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Visit visit = (Visit) o;
        return Objects.equals(visitDate, visit.visitDate) &&
                Objects.equals(timeForCome, visit.timeForCome) &&
                Objects.equals(orderForCome, visit.orderForCome) &&
                Objects.equals(client, visit.client) &&
                status == visit.status &&
                Objects.equals(patient, visit.patient) &&
                Objects.equals(operationType, visit.operationType) &&
                eye == visit.eye &&
                Objects.equals(surgeon, visit.surgeon) &&
                Objects.equals(manager, visit.manager) &&
                Objects.equals(accomodation, visit.accomodation) &&
                Objects.equals(note, visit.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(visitDate, timeForCome, orderForCome, client, status, patient, operationType, eye, surgeon, manager, accomodation, note);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Visit{");
        sb.append("visitDate=").append(visitDate);
        sb.append(", timeForCome=").append(timeForCome);
        sb.append(", orderForCome=").append(orderForCome);
        sb.append(", client=").append(client);
        sb.append(", status=").append(status);
        sb.append(", relative=").append(patient);
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
