package ua.nike.project.hibernate.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "OperationDay.getUniqueOperationDates", query = "SELECT DISTINCT od.operationDate FROM OperationDay od ORDER BY od.operationDate"),
        @NamedQuery(name = "OperationDay.findAll", query = "FROM OperationDay")
})
@Table(name = "operation_days")
//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="operationDayId")
public class OperationDay implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_day_id")
    private Integer operationDayId;

    @Column(name = "operation_date", nullable = false)
    private Date operationDate;

    @Column(name = "surgeon", nullable = false)
    private String surgeon;

    @OneToMany(targetEntity = Operation.class, fetch = FetchType.LAZY, mappedBy = "operationDay")
    private Set<Operation> operations;


    public Integer getOperationDayId() {
        return operationDayId;
    }

    public void setOperationDayId(Integer operationDayId) {
        this.operationDayId = operationDayId;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public String getSurgeon() {
        return surgeon;
    }

    public void setSurgeon(String surgeon) {
        this.surgeon = firstUpperCase(surgeon);
    }

    public Set<Operation> getOperations() {
        return operations;
    }

    public void setOperations(Set<Operation> operations) {
        this.operations = operations;
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

        OperationDay that = (OperationDay) o;

        if (operationDayId != null ? !operationDayId.equals(that.operationDayId) : that.operationDayId != null)
            return false;
        if (operationDate != null ? !operationDate.equals(that.operationDate) : that.operationDate != null)
            return false;
        return surgeon != null ? surgeon.equals(that.surgeon) : that.surgeon == null;
    }

    @Override
    public int hashCode() {
        int result = operationDayId != null ? operationDayId.hashCode() : 0;
        result = 31 * result + (operationDate != null ? operationDate.hashCode() : 0);
        result = 31 * result + (surgeon != null ? surgeon.hashCode() : 0);
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
