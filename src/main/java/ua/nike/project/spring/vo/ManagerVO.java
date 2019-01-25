package ua.nike.project.spring.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

public class ManagerVO implements VisualObject {

    private Integer managerId;

    @NotNull(message = "manager.surname.null")
    @Size(min = 1, max = 50, message = "manager.surname.size")
    @Pattern(regexp = "[A-Za-zА-Яа-яЁёІіЇїЄє]+", message = "manager.surname.pattern")
    private String surname;

    @NotNull(message = "manager.firstName.null")
    @Size(min = 1, max = 50, message = "manager.firstName.size")
    @Pattern(regexp = "[A-Za-zА-Яа-яЁёІіЇїЄє]+", message = "manager.firstName.pattern")
    private String firstName;

    @NotNull(message = "manager.secondName.null")
    @Size(min = 1, max = 50, message = "manager.secondName.size")
    @Pattern(regexp = "[A-Za-zА-Яа-яЁёІіЇїЄє]+", message = "manager.secondName.pattern")
    private String secondName;

    @NotNull(message = "manager.city.null")
    @Size(min = 1, max = 50, message = "manager.city.size")
    @Pattern(regexp = "[A-Za-zА-Яа-яЁёІіЇїЄє]+", message = "manager.city.pattern")
    private String city;

    @NotNull(message = "manager.sex.null")
    private Character sex;
    private Boolean disable;

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = firstUpperCase(city);
    }

    public Character getSex() {
        return sex;
    }

    public void setSex(Character sex) {
        this.sex = sex;
    }

    public Boolean isDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
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
        ManagerVO manager = (ManagerVO) o;
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
        final StringBuilder sb = new StringBuilder("ManagerVO{");
        sb.append("managerId=").append(managerId);
        sb.append(", surname='").append(surname).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", secondName='").append(secondName).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", sex=").append(sex);
        sb.append(", disable=").append(disable);
        sb.append('}');
        return sb.toString();
    }
}
