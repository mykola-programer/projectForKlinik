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
        @NamedQuery(name = "Manager.findAll", query = "FROM Manager ORDER BY surname, firstName, secondName"),
})
@Table(name = "manager", uniqueConstraints = {
        @UniqueConstraint(name = "manager_uk", columnNames = {"surname", "first_name", "second_name", "city_from"})
})
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
public class Manager implements EntityObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manager_id")
    private Integer managerId;

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

    @Column(name = "city_from", length = 50, nullable = false)
    private String city;

    @Column(name = "disable")
    private Boolean disable;

    @OneToMany(targetEntity = Visit.class, fetch = FetchType.LAZY, mappedBy = "manager")
    private List<Visit> visits;

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
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

    public String getCity() {
        return city;
    }

    public void setCity(String cityFrom) {
        this.city = firstUpperCase(cityFrom);
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
        Manager manager = (Manager) o;
        return Objects.equals(surname, manager.surname) &&
                Objects.equals(firstName, manager.firstName) &&
                Objects.equals(secondName, manager.secondName) &&
                Objects.equals(city, manager.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, firstName, secondName, city);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Manager{");
        sb.append("managerId=").append(managerId);
        sb.append(", surname='").append(surname).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", secondName='").append(secondName).append('\'');
        sb.append(", sex=").append(sex);
        sb.append(", cityFrom='").append(city).append('\'');
        sb.append(", disable=").append(disable);
        sb.append('}');
        return sb.toString();
    }
}
