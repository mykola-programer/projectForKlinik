package ua.nike.project.hibernate.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Time;
import java.time.LocalTime;

@Component
@Scope("prototype")
public class HospitalizationBean implements Serializable {
    private int numberOfPlace;
    private int numberOfOrder;
    private String surname;
    private String firstName;
    private String secondName;
    private Character sex;
    private String status;
    private String procedure;
    private String eye;
    private LocalTime timeForCome;
    private String surgeon;
    private String manager;
    private String note;

    public HospitalizationBean() {
    }

    public HospitalizationBean(int numberOfPlace, int numberOfOrder, String surname, String firstName, String secondName, Character sex, String status, String procedure, String eye, LocalTime timeForCome, String surgeon, String manager, String note) {
        this.numberOfPlace = numberOfPlace;
        this.numberOfOrder = numberOfOrder;
        this.surname = surname;
        this.firstName = firstName;
        this.secondName = secondName;
        this.sex = sex;
        this.status = status;
        this.procedure = procedure;
        this.eye = eye;
        this.timeForCome = timeForCome;
        this.surgeon = surgeon;
        this.manager = manager;
        this.note = note;
    }
    public HospitalizationBean(int numberOfOrder, String surname, String firstName, String secondName, Character sex, String status, String procedure, String eye, LocalTime timeForCome, String surgeon, String manager, String note) {
        this.numberOfOrder = numberOfOrder;
        this.surname = surname;
        this.firstName = firstName;
        this.secondName = secondName;
        this.sex = sex;
        this.status = status;
        this.procedure = procedure;
        this.eye = eye;
        this.timeForCome = timeForCome;
        this.surgeon = surgeon;
        this.manager = manager;
        this.note = note;
    }

    public int getNumberOfPlace() {
        return numberOfPlace;
    }

    public void setNumberOfPlace(int numberOfPlace) {
        this.numberOfPlace = numberOfPlace;
    }

    public int getNumberOfOrder() {
        return numberOfOrder;
    }

    public void setNumberOfOrder(int numberOfOrder) {
        this.numberOfOrder = numberOfOrder;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public String getEye() {
        return eye;
    }

    public void setEye(String eye) {
        this.eye = eye;
    }

    public LocalTime getTimeForCome() {
        return timeForCome;
    }

    public void setTimeForCome(LocalTime timeForCome) {
        this.timeForCome = timeForCome;
    }

    public String getSurgeon() {
        return surgeon;
    }

    public void setSurgeon(String surgeon) {
        this.surgeon = surgeon;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
