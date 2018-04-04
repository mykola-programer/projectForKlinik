package ua.nike.project.hibernate.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table (name = "operation_days")
public class OperationDay /*implements Serializable*/ {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    @Column (name = "operation_day_id")
    private Integer operationDayId;

    @Column (name = "operation_date", nullable = false)
    private Date operationDate;

    @Column (name = "surgeon", nullable = false)
    private String surgeon;

    public Integer getOperationDayId() {
        return operationDayId;
    }

    public void setOperationDayId(Integer operationDayId) {
        this.operationDayId = operationDayId;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public String getSurgeon() {
        return surgeon;
    }

    public void setSurgeon(String surgeon) {
        this.surgeon = surgeon;
    }
}
