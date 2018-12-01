package ua.nike.project.spring.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

public class SurgeonVO implements Serializable, VisualObject {

    private Integer surgeonId;

    @NotNull(message = "Прізвище повинно бути задано.")
    @Size(min = 1, max = 50, message = "Прізвище повинно мати до 50 символів !")
    @Pattern(regexp = "[A-Za-zА-Яа-яЁёІіЇїЄє]+", message = "Прізвище повинно мати тільки літери !")
    private String surname;

    @NotNull(message = "Ім'я повинно бути задано.")
    @Size(min = 1, max = 50, message = "Ім'я повинно мати від 50 символів !")
    @Pattern(regexp = "[A-Za-zА-Яа-яЁёІіЇїЄє]+", message = "Ім'я повинно мати тільки літери !")
    private String firstName;

    @NotNull(message = "По-Батькові повинно бути задано.")
    @Size(min = 1, max = 50, message = "По-Батькові повинно мати до 50 символів !")
    @Pattern(regexp = "[A-Za-zА-Яа-яЁёІіЇїЄє]+", message = "По-Батькові повинно мати тільки літери !")
    private String secondName;

    @NotNull(message = "Стать повинна бути задана.")
    private Character sex;

    private boolean inactive;

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

    public Character getSex() {
        return sex;
    }

    public void setSex(Character sex) {
        this.sex = sex;
    }

    public boolean isInactive() {
        return inactive;
    }

    public void setInactive(boolean inactive) {
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
        final StringBuilder sb = new StringBuilder("SurgeonVO{");
        sb.append("surgeonId=").append(surgeonId);
        sb.append(", surname='").append(surname).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", secondName='").append(secondName).append('\'');
        sb.append(", sex=").append(sex);
        sb.append(", inactive=").append(inactive);
        sb.append('}');
        return sb.toString();
    }
}
