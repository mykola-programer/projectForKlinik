package ua.nike.project.hibernate.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@NamedQuery(name = "OperationDay.findAll", query = "FROM OperationDay")
@Table(name = "operation_days")
public class OperationDay implements Serializable {

    @Version
    private long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_day_id")
    private Integer operationDayId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operation_date")
    private OperationDate operationDate;

    @Column(name = "surgeon", nullable = false)
    private String surgeon;

    @OneToMany(targetEntity = Operation.class, fetch = FetchType.LAZY, mappedBy = "operationDay")
    private Set<Operation> operations;

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Integer getOperationDayId() {
        return operationDayId;
    }

    public void setOperationDayId(Integer operationDayId) {
        this.operationDayId = operationDayId;
    }

    public OperationDate getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(OperationDate operationDate) {
        this.operationDate = operationDate;
    }

    public String getSurgeon() {
        return surgeon;
    }

    public void setSurgeon(String surgeon) {
        this.surgeon = surgeon;
    }

    public Set<Operation> getOperations() {
        return operations;
    }

    public void setOperations(Set<Operation> operations) {
        this.operations = operations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OperationDay that = (OperationDay) o;

        if (!operationDate.equals(that.operationDate)) return false;
        return surgeon.equals(that.surgeon);
    }

    @Override
    public int hashCode() {
        int result = operationDate.hashCode();
        result = 31 * result + surgeon.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "OperationDay{" +
                "operationDayId=" + operationDayId +
                ", operationDate=" + operationDate +
                ", surgeon='" + surgeon + '\'' +
                '}';
    }
}
