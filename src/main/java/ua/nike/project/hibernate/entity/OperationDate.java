package ua.nike.project.hibernate.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "operation_dates")
@NamedQueries(value = {
        @NamedQuery(name = "OperationDate.getOperationDates", query = "SELECT DISTINCT od.date FROM OperationDate od ORDER BY od.date"),
        @NamedQuery(name = "OperationDate.findAll", query = "FROM OperationDate")
})
public class OperationDate implements Serializable {

    @Version
    private long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_date_id")
    private Integer operationDateId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @OneToMany(targetEntity = Operation.class, fetch = FetchType.LAZY, mappedBy = "operationDate")
    private Set<Operation> operations;

    @OneToMany(targetEntity = Hospitalization.class, fetch = FetchType.LAZY, mappedBy = "operationDate")
    private Set<Hospitalization> hospitalizations;

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Integer getOperationDateId() {
        return operationDateId;
    }

    public void setOperationDateId(Integer operationDateId) {
        this.operationDateId = operationDateId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<Operation> getOperations() {
        return operations;
    }

    public void setOperations(Set<Operation> operations) {
        this.operations = operations;
    }

    public Set<Hospitalization> getHospitalizations() {
        return hospitalizations;
    }

    public void setHospitalizations(Set<Hospitalization> hospitalizations) {
        this.hospitalizations = hospitalizations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OperationDate that = (OperationDate) o;

        return date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

    @Override
    public String toString() {
        return "OperationDate{" +
                "operationDateId=" + operationDateId +
                ", date=" + date +
                '}';
    }
}
