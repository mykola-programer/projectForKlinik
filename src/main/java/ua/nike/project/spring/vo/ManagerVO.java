package ua.nike.project.spring.vo;

import java.io.Serializable;
import java.util.Objects;

public class ManagerVO implements Serializable, VisualObject {

    private Integer managerId;
    private String surname;
    private String firstName;
    private String secondName;
    private String cityFrom;

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

    public String getCityFrom() {
        return cityFrom;
    }

    public void setCityFrom(String cityFrom) {
        this.cityFrom = firstUpperCase(cityFrom);
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
                Objects.equals(cityFrom, manager.cityFrom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, firstName, secondName, cityFrom);
    }

    @Override
    public String toString() {
        return "Manager{" +
                "managerId=" + managerId +
                ", surname='" + surname + '\'' +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", cityFrom='" + cityFrom + '\'' +
                '}';
    }
}
