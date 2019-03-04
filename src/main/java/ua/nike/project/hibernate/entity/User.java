package ua.nike.project.hibernate.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "User.findAll", query = "FROM User ORDER BY login"),
})
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "user_pk", columnNames = {"login"})
})
public class User implements EntityObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;
    private String login;
    @Column(name = "password")
    private String hashedPassword;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String password) {
        this.hashedPassword = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(login, user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("login='").append(login).append('\'');
        sb.append(", password='").append("******").append('\'');
        sb.append('}');
        return sb.toString();
    }
}
