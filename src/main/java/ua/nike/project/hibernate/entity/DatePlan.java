package ua.nike.project.hibernate.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "date_plan", uniqueConstraints = {
        @UniqueConstraint(name = "date_plan_uk", columnNames = {"date", "department_id"})
})
@NamedQueries(value = {
        @NamedQuery(name = "DatePlan.findAll", query = "FROM DatePlan ORDER BY date"),
        @NamedQuery(name = "DatePlan.findByDepartment", query = "FROM DatePlan WHERE department.departmentId = ?1 AND date > ?2 ORDER BY date"),
        @NamedQuery(name = "DatePlan.deleteByIDs", query = "DELETE DatePlan vd WHERE vd.datePlanId IN (:IDs)")
})
public class DatePlan implements EntityObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_plan_id")
    private Integer datePlanId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "disable", nullable = false)
    private boolean disable;

    @OneToMany(targetEntity = SurgeonPlan.class, fetch = FetchType.LAZY, mappedBy = "datePlan")
    private List<SurgeonPlan> surgeonPlans;

    public Integer getDatePlanId() {
        return datePlanId;
    }

    public void setDatePlanId(Integer visitDateId) {
        this.datePlanId = visitDateId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public List<SurgeonPlan> getSurgeonPlans() {
        return surgeonPlans;
    }

    public void setSurgeonPlans(List<SurgeonPlan> surgeonPlans) {
        this.surgeonPlans = surgeonPlans;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatePlan datePlan = (DatePlan) o;
        return Objects.equals(date, datePlan.date) &&
                Objects.equals(department, datePlan.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, department);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DatePlan{");
        sb.append("datePlanId=").append(datePlanId);
        sb.append(", date=").append(date);
        sb.append(", department=").append(department);
        sb.append(", disable=").append(disable);
        sb.append('}');
        return sb.toString();
    }
}
