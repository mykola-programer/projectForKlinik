package ua.nike.project.hibernate.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "visit_date", uniqueConstraints = {
        @UniqueConstraint(name = "date_pk", columnNames = {"date"})
})
@NamedQueries(value = {
        @NamedQuery(name = "VisitDate.findAll", query = "FROM VisitDate ORDER BY date"),
        @NamedQuery(name = "VisitDate.findAllActive", query = "FROM VisitDate vd WHERE vd.inactive = false ORDER BY date"),
        @NamedQuery(name = "VisitDate.deleteByIDs", query = "DELETE VisitDate vd WHERE vd.visitDateId IN (:IDs)")
})
public class VisitDate implements Serializable, EntityObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visit_date_id")
//    @Access(AccessType.FIELD) // TODO Work without annotation !
    private Integer visitDateId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "inactive", nullable = false)
    private boolean inactive = false;

    @OneToMany(targetEntity = Visit.class, fetch = FetchType.LAZY, mappedBy = "visitDate")
    private Set<Visit> visits;

    public Integer getVisitDateId() {
        return visitDateId;
    }

    public void setVisitDateId(Integer visitDateId) {
        this.visitDateId = visitDateId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isInactive() {
        return inactive;
    }

    public void setInactive(boolean inactive) {
        this.inactive = inactive;
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

        VisitDate that = (VisitDate) o;

        return date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VisitDate{");
        sb.append("visitDateId=").append(visitDateId);
        sb.append(", date=").append(date);
        sb.append(", inactive=").append(inactive);
        sb.append('}');
        return sb.toString();
    }
}
