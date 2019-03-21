package ua.nike.project.hibernate.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "Department.findAll", query = "FROM Department ORDER BY name"),
})
@Table(name = "department", uniqueConstraints = {
        @UniqueConstraint(name = "department_uk", columnNames = {"name"})
})
public class Department implements EntityObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Integer departmentId;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "disable")
    private Boolean disable;

    @OneToMany(targetEntity = DatePlan.class, fetch = FetchType.LAZY, mappedBy = "department")
    private List<DatePlan> datePlans;

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = firstUpperCase(name);
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public List<DatePlan> getDatePlans() {
        return datePlans;
    }

    public void setDatePlans(List<DatePlan> datePlans) {
        this.datePlans = datePlans;
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
        Department that = (Department) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Department{");
        sb.append("departmentId=").append(departmentId);
        sb.append(", name='").append(name).append('\'');
        sb.append(", disable=").append(disable);
        sb.append('}');
        return sb.toString();
    }
}
