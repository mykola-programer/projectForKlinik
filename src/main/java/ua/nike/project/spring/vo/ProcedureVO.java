package ua.nike.project.spring.vo;

import ua.nike.project.hibernate.entity.Operation;
import ua.nike.project.hibernate.entity.Procedure;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

public class ProcedureVO implements Serializable {

    private Integer procedureId;
    private String name;

    public Integer getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(Integer procedureId) {
        this.procedureId = procedureId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
