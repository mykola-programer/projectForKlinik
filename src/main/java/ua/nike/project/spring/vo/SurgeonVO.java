package ua.nike.project.spring.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

public class SurgeonVO implements VisualObject {

    private Integer surgeonId;

    @NotNull(message = "surgeon.surname.null")
    @Size(min = 1, max = 50, message = "surgeon.surname.size")
    @Pattern(regexp = "[A-Za-zА-Яа-яЁёІіЇїЄє]+", message = "surgeon.surname.pattern")
    private String surname;

    @NotNull(message = "surgeon.firstName.null")
    @Size(min = 1, max = 50, message = "surgeon.firstName.size")
    @Pattern(regexp = "[A-Za-zА-Яа-яЁёІіЇїЄє]+", message = "surgeon.firstName.pattern")
    private String firstName;

    @NotNull(message = "surgeon.secondName.null")
    @Size(min = 1, max = 50, message = "surgeon.secondName.size")
    @Pattern(regexp = "[A-Za-zА-Яа-яЁёІіЇїЄє]+", message = "surgeon.secondName.pattern")
    private String secondName;

    @NotNull(message = "manager.city.null")
    @Size(min = 1, max = 50, message = "manager.city.size")
    @Pattern(regexp = "[A-Za-zА-Яа-яЁёІіЇїЄє]+", message = "manager.city.pattern")
    private String city;

    @NotNull(message = "surgeon.sex.null")
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

    public String getCity() {
        return city;
    }

    public SurgeonVO setCity(String city) {
        this.city = firstUpperCase(city);
        return this;
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
        SurgeonVO surgeonVO = (SurgeonVO) o;
        return Objects.equals(surname, surgeonVO.surname) &&
                Objects.equals(firstName, surgeonVO.firstName) &&
                Objects.equals(secondName, surgeonVO.secondName) &&
                Objects.equals(city, surgeonVO.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, firstName, secondName, city);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SurgeonVO{");
        sb.append("surgeonId=").append(surgeonId);
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
