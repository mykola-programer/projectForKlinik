package ua.nike.project.spring.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class ClientVO implements Serializable, VisualObject {

    private Integer clientId;

    @NotNull(message = "Прізвище повинно бути задано.")
    @Size(min = 1, max = 50, message = "Прізвище повинно мати від 1 до 50 символів !")
    @Pattern(regexp = "[A-Za-zА-Яа-яЁёІіЇїЄє]+", message = "Прізвище повинно мати тільки літери !")
    private String surname;

    @NotNull(message = "Ім'я повинно бути задано.")
    @Size(min = 1, max = 50, message = "Ім'я повинно мати від 1 до 50 символів !")
    @Pattern(regexp = "[A-Za-zА-Яа-яЁёІіЇїЄє]+", message = "Ім'я повинно мати тільки літери !")
    private String firstName;

    @NotNull(message = "По-Батькові повинно бути задано.")
    @Size(min = 1, max = 50, message = "По-Батькові повинно мати від 1 до 50 символів !")
    @Pattern(regexp = "[A-Za-zА-Яа-яЁёІіЇїЄє]+", message = "По-Батькові повинно мати тільки літери !")
    private String secondName;

    @NotNull(message = "Стать повинна бути задана.")
    private Character sex;

    @NotNull(message = "Дата народження повинна бути задана.")
    private LocalDate birthday;

    @Size(max = 19, message = "Телефон повинен бути не більше 19 символів !")
    @Pattern(regexp = "[ ()0-9+-]*")
    private String telephone;

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Character getSex() {
        return sex;
    }

    public void setSex(Character sex) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientVO clientVO = (ClientVO) o;

        return Objects.equals(surname, clientVO.surname) &&
                Objects.equals(firstName, clientVO.firstName) &&
                Objects.equals(secondName, clientVO.secondName) &&
                Objects.equals(birthday, clientVO.birthday);

    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, firstName, secondName, birthday);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ClientVO{");
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

}
