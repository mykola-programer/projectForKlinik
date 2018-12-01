package ua.nike.project.hibernate.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "operation_type")
@NamedQueries(value = {
        @NamedQuery(name = "OperationType.findAll", query = "FROM OperationType ORDER BY name"),
        @NamedQuery(name = "OperationType.getAllActive", query = "FROM OperationType WHERE inactive = false ORDER BY name")

})
public class OperationType implements EntityObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_type_id")
    private Integer operationTypeId;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "inactive", nullable = false)
    private boolean inactive;

    @OneToMany(targetEntity = Visit.class, fetch = FetchType.LAZY, mappedBy = "operationType")
    private List<Visit> visits;

    public Integer getOperationTypeId() {
        return operationTypeId;
    }

    public void setOperationTypeId(Integer operationTypeId) {
        this.operationTypeId = operationTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInactive() {
        return inactive;
    }

    public void setInactive(boolean lockType) {
        this.inactive = lockType;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationType that = (OperationType) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OperationType{");
        sb.append("operationTypeId=").append(operationTypeId);
        sb.append(", name='").append(name).append('\'');
        sb.append(", inactive=").append(inactive);
        sb.append('}');
        return sb.toString();
    }
}
