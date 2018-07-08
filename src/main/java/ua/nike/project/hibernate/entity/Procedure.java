package ua.nike.project.hibernate.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "procedure")
@NamedQuery(name = "Procedure.findAll", query = "FROM Procedure ")
public class Procedure implements Serializable {

    @Version
    private long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "procedure_id")
    private Integer procedureId;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(targetEntity = Operation.class, fetch = FetchType.LAZY, mappedBy = "procedure")
    private Set<Operation> operations;

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Integer getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(Integer procedureId) {
        this.procedureId = procedureId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

        Procedure procedure = (Procedure) o;

        if (!procedureId.equals(procedure.procedureId)) return false;
        return name.equals(procedure.name);
    }

    @Override
    public int hashCode() {
        int result = procedureId.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Procedure{" +
                "procedureId=" + procedureId +
                ", name='" + name + '\'' +
                '}';
    }
}
