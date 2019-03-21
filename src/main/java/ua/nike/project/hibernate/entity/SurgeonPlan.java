package ua.nike.project.hibernate.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "surgeon_plan", uniqueConstraints = {
        @UniqueConstraint(name = "surgeon_plan_uk", columnNames = {"date_plan_id", "surgeon_id"})
})
@NamedQueries(value = {
        @NamedQuery(name = "SurgeonPlan.findAll", query = "FROM SurgeonPlan"),
})
public class SurgeonPlan implements EntityObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "surgeon_plan_id")
    private Integer surgeonPlanId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "date_plan_id")
    private DatePlan datePlan;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "surgeon_id")
    private Surgeon surgeon;

    @Column(name = "disable", nullable = false)
    private boolean disable;

    @OneToMany(targetEntity = Visit.class, fetch = FetchType.LAZY, mappedBy = "surgeonPlan")
    private Set<Visit> visits;

    public Integer getSurgeonPlanId() {
        return surgeonPlanId;
    }

    public void setSurgeonPlanId(Integer surgeonPlanId) {
        this.surgeonPlanId = surgeonPlanId;
    }

    public DatePlan getDatePlan() {
        return datePlan;
    }

    public void setDatePlan(DatePlan datePlan) {
        this.datePlan = datePlan;
    }

    public Surgeon getSurgeon() {
        return surgeon;
    }

    public void setSurgeon(Surgeon surgeon) {
        this.surgeon = surgeon;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public Set<Visit> getVisits() {
        return visits;
    }

    public void setVisits(Set<Visit> visits) {
        this.visits = visits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurgeonPlan that = (SurgeonPlan) o;
        return Objects.equals(datePlan, that.datePlan) &&
                Objects.equals(surgeon, that.surgeon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(datePlan, surgeon);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SurgeonPlan{");
        sb.append("surgeonPlanId=").append(surgeonPlanId);
        sb.append(", datePlan=").append(datePlan);
        sb.append(", surgeon=").append(surgeon);
        sb.append(", disable=").append(disable);
        sb.append('}');
        return sb.toString();
    }
}
