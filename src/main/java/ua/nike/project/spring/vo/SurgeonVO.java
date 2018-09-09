package ua.nike.project.spring.vo;

import java.io.Serializable;
import java.util.Objects;

public class SurgeonVO implements Serializable {

    private Integer surgeonId;
    private String surname;
    private String firstName;
    private String secondName;

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
        SurgeonVO surgeon = (SurgeonVO) o;
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
