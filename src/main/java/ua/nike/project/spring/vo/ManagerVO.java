package ua.nike.project.spring.vo;

import java.util.Objects;

public class ManagerVO implements VisualObject {

    private Integer managerId;
    private String surname;
    private String firstName;
    private String secondName;
    private String city;
    private Character sex;
    private Boolean inactive;

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

    public Boolean isInactive() {
        return inactive;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
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
        sb.append(", inactive=").append(inactive);
        sb.append('}');
        return sb.toString();
    }
}
