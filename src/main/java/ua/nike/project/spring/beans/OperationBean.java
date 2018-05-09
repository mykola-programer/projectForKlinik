package ua.nike.project.spring.beans;

import java.io.Serializable;
import java.util.Date;

public class OperationBean implements Serializable {
   private Date operationDate;
   private String surname;
   private String firstName;
   private String secondName;
   private String operationName;
   private String eye;
   private String surgeon;
   private String manager;

    public OperationBean() {
    }

    public OperationBean(Date operationDate, String surname, String firstName, String secondName, String operationName, String eye, String surgeon, String manager) {
        this.operationDate = operationDate;
        this.surname = surname;
        this.firstName = firstName;
        this.secondName = secondName;
        this.operationName = operationName;
        this.eye = eye;
        this.surgeon = surgeon;
        this.manager = manager;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = firstUpperCase(surname);
    }

    public String getFirstname() {
        return firstName;
    }

    public void setFirstname(String firstName) {
        this.firstName = firstUpperCase(firstName);
    }

    public String getSecondname() {
        return secondName;
    }

    public void setSecondname(String secondName) {
        this.secondName = firstUpperCase(secondName);
    }

    public String getOperation() {
        return operationName;
    }

    public void setOperation(String operationName) {
        this.operationName = operationName.toUpperCase();
    }

    public String getEye() {
        return eye;
    }

    public void setEye(String eye) {
        this.eye = eye.toUpperCase();
    }

    public String getSurgeon() {
        return surgeon;
    }

    public void setSurgeon(String surgeon) {
        this.surgeon = firstUpperCase(surgeon);
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = firstUpperCase(manager);
    }

    @Override
    public String toString() {
        return "OperationBean{" +
                "operationDate=" + operationDate +
                ", surname='" + surname + '\'' +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", operationName='" + operationName + '\'' +
                ", eye='" + eye + '\'' +
                ", surgeon='" + surgeon + '\'' +
                ", manager='" + manager + '\'' +
                '}';
    }

    private String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }
}