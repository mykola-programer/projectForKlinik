package ua.nike.project.struct;

public class Patient {
    private String surname = null;
    private String firstName = null;
    private String secondName = null;
    private String allName = null;
    private String sex = null;
    private String status = null;
    private Integer telephone = null;

    public Patient(String surname, String firstName, String secondName, String sex, String status) {
        setSurname(surname);
        setFirstName(firstName);
        setSecondName(secondName);
        setAllName(surname, firstName, secondName);
        setSex(sex);
        setStatus(status);
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

    public String getAllName() {
        return allName;
    }

    public void setAllName(String surname, String firstName, String secondName) {
        this.allName = surname + " " + firstName + " " + secondName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex.toLowerCase();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status.toLowerCase();
    }

    public Integer getTelephone() {
        return telephone;
    }

    public void setTelephone(Integer telephone) {
        this.telephone = telephone;
    }

    private String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }
}
