package ua.nike.project.spring.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ua.nike.project.spring.service.convert.LocalDateToArrayConverter;
import ua.nike.project.spring.service.convert.ArrayToLocalDateConverter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;

public class ClientVO implements VisualObject {

    private Integer clientId;

    @NotNull(message = "client.surname.null")
    @Size(min = 1, max = 50, message = "client.surname.size")
    @Pattern(regexp = "[A-Za-zА-Яа-яЁёІіЇїЄє'\"]+", message = "client.surname.pattern")
    private String surname;

    @NotNull(message = "client.firstName.null")
    @Size(min = 1, max = 50, message = "client.firstName.size")
    @Pattern(regexp = "[A-Za-zА-Яа-яЁёІіЇїЄє'\"]+", message = "client.firstName.pattern")
    private String firstName;

    @NotNull(message = "client.secondName.null")
    @Size(min = 1, max = 50, message = "client.secondName.size")
    @Pattern(regexp = "[A-Za-zА-Яа-яЁёІіЇїЄє'\"]+", message = "client.secondName.pattern")
    private String secondName;

    @NotNull(message = "client.sex.null")
    private Character sex;

    @NotNull(message = "client.birthday.null")
    @JsonSerialize(converter = LocalDateToArrayConverter.class)
    @JsonDeserialize(converter = ArrayToLocalDateConverter.class)
    private LocalDate birthday;

    @Size(max = 19, message = "client.telephone.size")
    @Pattern(regexp = "[ ()0-9+-]*", message = "client.telephone.pattern")
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
