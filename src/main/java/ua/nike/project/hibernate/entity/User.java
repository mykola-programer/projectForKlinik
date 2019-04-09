package ua.nike.project.hibernate.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "User.findAll", query = "FROM User ORDER BY username"),
})
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "user_pk", columnNames = {"username"})
})
public class User implements EntityObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;
    private String username;
    @Column(name = "password")
    private String password;

    private boolean enabled;
    @OneToMany(targetEntity = Role.class, fetch = FetchType.EAGER, mappedBy = "user")
    private List<Role> roles;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("login='").append(username).append('\'');
        sb.append(", password='").append("******").append('\'');
        sb.append('}');
        return sb.toString();
    }
}
