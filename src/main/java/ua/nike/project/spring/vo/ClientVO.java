package ua.nike.project.spring.vo;

import ua.nike.project.hibernate.type.Sex;

import java.io.Serializable;
import java.util.Objects;

public class ClientVO implements Serializable{

    private Integer clientId;
    private String surname;
    private String firstName;
    private String secondName;
    private Sex sex;
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

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
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
                Objects.equals(sex, clientVO.sex);

    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, firstName, secondName, sex);
    }

    @Override
    public String toString() {
        return "ClientVO{" +
                "clientId=" + clientId +
                ", surname='" + surname + '\'' +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", sex=" + sex +
                ", telephone='" + telephone + '\'' +
                '}';
    }

}
