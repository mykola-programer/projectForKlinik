package ua.nike.project.spring.vo;

import ua.nike.project.hibernate.entity.Hospitalization;
import ua.nike.project.hibernate.entity.Operation;
import ua.nike.project.hibernate.entity.OperationDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

public class OperationDateVO implements Serializable {

    private Integer operationDateId;
    private LocalDate date;
    private Set<Operation> operations;
    private Set<Hospitalization> hospitalizations;

    public OperationDateVO() {
    }

    public OperationDateVO(Integer operationDateId, LocalDate date, Set<Operation> operations, Set<Hospitalization> hospitalizations) {
        this.operationDateId = operationDateId;
        this.date = date;
        this.operations = operations;
        this.hospitalizations = hospitalizations;
    }

    public Integer getOperationDateId() {
        return operationDateId;
    }

    public void setOperationDateId(Integer operationDateId) {
        this.operationDateId = operationDateId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<Operation> getOperations() {
        return operations;
    }

    public void setOperations(Set<Operation> operations) {
        this.operations = operations;
    }

    public Set<Hospitalization> getHospitalizations() {
        return hospitalizations;
    }

    public void setHospitalizations(Set<Hospitalization> hospitalizations) {
        this.hospitalizations = hospitalizations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OperationDateVO that = (OperationDateVO) o;

        return date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

    @Override
    public String toString() {
        return "OperationDate{" +
                "operationDateId=" + operationDateId +
                ", date=" + date +
                '}';
    }
}
