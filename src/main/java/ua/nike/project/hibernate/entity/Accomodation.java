package ua.nike.project.hibernate.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ua.nike.project.hibernate.type.PostgreSQLEnumType;
import ua.nike.project.hibernate.type.Ward;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "Accomodation.findAll", query = "FROM Accomodation acc ORDER BY acc.ward, acc.wardPlace"),
        @NamedQuery(name = "Accomodation.getAllUnlock", query = "FROM Accomodation acc where acc.placeLocked = false ORDER BY acc.ward, acc.wardPlace")
})
@Table(name = "accomodation")
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
public class Accomodation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accomodation_id")
    private Integer accomodationId;

    @Column(name = "ward")
    @Enumerated(value = EnumType.STRING)
    @Type(type = "pgsql_enum")
    private Ward ward;

    @Column(name = "ward_place")
    private Integer wardPlace;

    @Column(name = "locked_place")
    private Boolean placeLocked;

    @OneToMany(targetEntity = Visit.class, fetch = FetchType.LAZY, mappedBy = "accomodation")
    private List<Visit> visits;

    public Integer getAccomodationId() {
        return accomodationId;
    }

    public void setAccomodationId(Integer accomodationId) {
        this.accomodationId = accomodationId;
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    public Integer getWardPlace() {
        return wardPlace;
    }

    public void setWardPlace(Integer wardPlace) {
        this.wardPlace = wardPlace;
    }

    public Boolean isPlaceLocked() {
        return placeLocked;
    }

    public void setPlaceLocked(Boolean placeBlocked) {
        this.placeLocked = placeBlocked;
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
        Accomodation that = (Accomodation) o;
        return ward == that.ward &&
                Objects.equals(wardPlace, that.wardPlace) &&
                Objects.equals(placeLocked, that.placeLocked);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ward, wardPlace, placeLocked);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Accomodation{");
        sb.append("accomodationId=").append(accomodationId);
        sb.append(", ward=").append(ward);
        sb.append(", wardPlace=").append(wardPlace);
        sb.append(", placeLocked=").append(placeLocked);
        sb.append('}');
        return sb.toString();
    }
}