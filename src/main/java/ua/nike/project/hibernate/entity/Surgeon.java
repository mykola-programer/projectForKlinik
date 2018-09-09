package ua.nike.project.hibernate.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "Surgeon.findAll", query = "FROM Surgeon ORDER BY surname")
})
@Table(name = "surgeon")
public class Surgeon implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "surgeon_id")
    private Integer surgeonId;

    @Column(name = "surname", length = 50)
    private String surname;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "second_name", length = 50)
    private String secondName;

    @OneToMany(targetEntity = Visit.class, fetch = FetchType.LAZY, mappedBy = "surgeon", cascade = CascadeType.ALL)
    private List<Visit> visits;

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
        sb.append('}');
        return sb.toString();
    }
}