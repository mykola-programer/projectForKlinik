package ua.nike.project.hibernate.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ua.nike.project.hibernate.type.PostgreSQLEnumType;
import ua.nike.project.hibernate.type.Sex;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "Client.findAll", query = "FROM Client cl ORDER BY surname,firstName,secondName"),
        @NamedQuery(name = "Client.deleteByIDs", query = "DELETE Client cl WHERE cl.clientId IN (:IDs)")
})

@Table(name = "client", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"surname", "first_name", "second_name", "birthday"})
})
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
public class Client implements Comparable<Client>, EntityObject {

    @Version
    private long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
//    @Access(AccessType.FIELD) // TODO Work without annotation !
    private Integer clientId;

    @Column(name = "surname", length = 50)
    //    @Access(AccessType.PROPERTY) // TODO Work without annotation !
    private String surname;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "second_name", length = 50)
    private String secondName;

    @Column(name = "sex")
    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    private Sex sex;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;


    @Column(name = "telephone")
    private String telephone;


    @OneToMany(targetEntity = Visit.class, fetch = FetchType.LAZY, mappedBy = "client")
    private List<Visit> visitsForClient;

    @OneToMany(targetEntity = Visit.class, fetch = FetchType.LAZY, mappedBy = "patient")
    private List<Visit> visitsForRelative;

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Integer getClientId() {
        System.out.println("getClientId");
        return clientId;
    }

    public void setClientId(Integer clientId) {
        System.out.println("setClientId");
        this.clientId = clientId;
    }

    public String getSurname() {
        System.out.println("getSurname");
        return surname;
    }

    public void setSurname(String surname) {
        System.out.println("setSurname");
        this.surname = firstUpperCase(surname);
    }

    public String getFirstName() {
        System.out.println("getFirstName");
        return firstName;
    }

    public void setFirstName(String firstName) {
        System.out.println("setFirstName");
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

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public List<Visit> getVisitsForClient() {
        return visitsForClient;
    }

    public void setVisitsForClient(List<Visit> visits) {
        this.visitsForClient = visits;
    }

    public List<Visit> getVisitsForRelative() {
        return visitsForRelative;
    }

    public void setVisitsForRelative(List<Visit> visitsForRelative) {
        this.visitsForRelative = visitsForRelative;
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

        Client client = (Client) o;

        return Objects.equals(surname, client.surname) &&
                Objects.equals(firstName, client.firstName) &&
                Objects.equals(secondName, client.secondName) &&
                Objects.equals(birthday, client.birthday);

    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, firstName, secondName, birthday);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Client{");
        sb.append("clientId=").append(clientId);
        sb.append(", surname='").append(surname).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", secondName='").append(secondName).append('\'');
        sb.append(", sex=").append(sex);
        sb.append(", birthday=").append(birthday);
        sb.append(", telephone='").append(telephone).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(Client client) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;

        if (this == client) return EQUAL;


        if (this.surname != null && client.surname != null) {
            if (this.surname.compareTo(client.surname) != EQUAL) return this.surname.compareTo(client.surname);
        } else if (this.surname != null) return BEFORE;
        else if (client.surname != null) return AFTER;

        // ---------------------------------------------------------------- //

        if (this.firstName != null && client.firstName != null) {
            if (this.firstName.compareTo(client.firstName) != EQUAL)
                return this.firstName.compareTo(client.firstName);
        } else if (this.firstName != null) return BEFORE;
        else if (client.firstName != null) return AFTER;

        // ---------------------------------------------------------------- //

        if (this.secondName != null && client.secondName != null) {
            if (this.secondName.compareTo(client.secondName) != EQUAL)
                return this.secondName.compareTo(client.secondName);
        } else if (this.secondName != null) return BEFORE;
        else if (client.secondName != null) return AFTER;

        // ---------------------------------------------------------------- //

        if (this.sex != null && client.sex != null) {
            if (this.sex.compareTo(client.sex) != EQUAL) return this.sex.compareTo(client.sex);
        } else if (this.sex != null) return BEFORE;
        else if (client.sex != null) return AFTER;

        // ---------------------------------------------------------------- //

        return EQUAL;
    }


}