package ua.nike.project.hibernate.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "Role.findAll", query = "FROM Role ORDER BY name"),
        @NamedQuery(name = "Role.findByName", query = "FROM Role WHERE upper(name) like upper(?1)"),
})
@Table(name = "roles")
public class Role implements EntityObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false, updatable = false)
    private long roleID;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(targetEntity = User.class, fetch = FetchType.LAZY, mappedBy = "role")
    private List<User> users;

    public long getRoleID() {
        return roleID;
    }

    public void setRoleID(long roleID) {
        this.roleID = roleID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toUpperCase();
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
