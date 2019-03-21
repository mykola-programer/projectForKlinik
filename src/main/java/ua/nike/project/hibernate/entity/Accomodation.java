package ua.nike.project.hibernate.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ua.nike.project.hibernate.type.PostgreSQLEnumType;
import ua.nike.project.hibernate.type.Ward;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "Accomodation.findAll", query = "FROM Accomodation acc ORDER BY acc.ward, acc.wardPlace"),
//        @NamedQuery(name = "Accomodation.getAllActive", query = "FROM Accomodation acc where acc.disable = false ORDER BY acc.ward, acc.wardPlace"),
//        @NamedQuery(name = "Accomodation.getActiveWards", query = "SELECT DISTINCT acc.ward FROM Accomodation acc where acc.disable = false ORDER BY acc.ward"),
//        @NamedQuery(name = "Accomodation.getWards", query = "SELECT DISTINCT acc.ward FROM Accomodation acc ORDER BY acc.ward")
})
@Table(name = "accomodation", uniqueConstraints = {
        @UniqueConstraint(name = "accomodation_uk", columnNames = {"ward", "ward_place"})
})
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
public class Accomodation implements EntityObject {

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

    @Column(name = "disable")
    private Boolean disable;

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

    public Boolean isDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
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
                Objects.equals(wardPlace, that.wardPlace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ward, wardPlace);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Accomodation{");
        sb.append("accomodationId=").append(accomodationId);
        sb.append(", ward=").append(ward);
        sb.append(", wardPlace=").append(wardPlace);
        sb.append(", disable=").append(disable);
        sb.append('}');
        return sb.toString();
    }
}
