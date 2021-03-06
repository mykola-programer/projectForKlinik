package ua.nike.project.hibernate.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ua.nike.project.hibernate.type.PostgreSQLEnumType;
import ua.nike.project.hibernate.type.Sex;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "Surgeon.findAll", query = "FROM Surgeon ORDER BY surname, firstName, secondName"),
})
@Table(name = "surgeon", uniqueConstraints = {
        @UniqueConstraint(name = "surgeon_uk", columnNames = {"surname", "first_name", "second_name"})
})
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
public class Surgeon implements EntityObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "surgeon_id")
    private Integer surgeonId;

    @Column(name = "surname", length = 50, nullable = false)
    private String surname;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "second_name", length = 50, nullable = false)
    private String secondName;

    @Column(name = "sex")
    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    private Sex sex;

    private boolean disable;

    @OneToMany(targetEntity = SurgeonPlan.class, fetch = FetchType.LAZY, mappedBy = "surgeon")
    private List<SurgeonPlan> surgeonPlans;

    public Integer getSurgeonId() {
        return surgeonId;
    }

    public void setSurgeonId(Integer surgeonId) {
        this.surgeonId = surgeonId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = firstUpperCase(surname);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstUpperCase(firstName);
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = firstUpperCase(secondName);
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
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
        Surgeon surgeon = (Surgeon) o;
        return Objects.equals(surname, surgeon.surname) &&
                Objects.equals(firstName, surgeon.firstName) &&
                Objects.equals(secondName, surgeon.secondName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, firstName, secondName);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Surgeon{");
        sb.append("surgeonId=").append(surgeonId);
        sb.append(", surname='").append(surname).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", secondName='").append(secondName).append('\'');
        sb.append(", sex=").append(sex);
        sb.append(", disable=").append(disable);
        sb.append('}');
        return sb.toString();
    }
}
