package ua.nike.project.hibernate.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "hospitalization")
@NamedQueries({
        @NamedQuery(name = "Hospitalization.findAll", query = "FROM Hospitalization ")})

public class Hospitalization implements Serializable {

    @Version
    private long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospitalization_id")
    private Integer hospitalizationId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operation_date")
    private OperationDate operationDate;

    @Column(name = "numberOfPlace", nullable = false)
    private Integer numberOfPlace;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "hospitalization")
    private Operation operation;

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Integer getHospitalizationId() {
        return hospitalizationId;
    }

    public void setHospitalizationId(Integer hospitalizationId) {
        this.hospitalizationId = hospitalizationId;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public OperationDate getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(OperationDate operationDate) {
        this.operationDate = operationDate;
    }

    public Integer getNumberOfPlace() {
        return numberOfPlace;
    }

    public void setNumberOfPlace(Integer numberOfPlace) {
        this.numberOfPlace = numberOfPlace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hospitalization that = (Hospitalization) o;

        if (!operationDate.equals(that.operationDate)) return false;
        return numberOfPlace.equals(that.numberOfPlace);
    }

    @Override
    public int hashCode() {
        int result = operationDate.hashCode();
        result = 31 * result + numberOfPlace.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Hospitalization{" +
                "hospitalizationId=" + hospitalizationId +
                ", operationDate=" + operationDate +
                ", numberOfPlace=" + numberOfPlace +
                '}';
    }
}
