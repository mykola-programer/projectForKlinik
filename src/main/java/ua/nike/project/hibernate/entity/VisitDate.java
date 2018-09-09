package ua.nike.project.hibernate.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "visit_date")
@NamedQueries(value = {
        @NamedQuery(name = "VisitDate.getVisitDates", query = "SELECT DISTINCT od.date FROM VisitDate od WHERE lock = false ORDER BY od.date"),
        @NamedQuery(name = "VisitDate.findAll", query = "FROM VisitDate ORDER BY date"),
        @NamedQuery(name = "VisitDate.getAllUnlock", query = "FROM VisitDate WHERE lock=false ORDER BY date")
})
public class VisitDate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visit_date_id")
    private Integer visitDateId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "lock", nullable = false)
    private boolean lock = false;

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

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
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
        sb.append(", lock=").append(lock);
        sb.append('}');
        return sb.toString();
    }
}
