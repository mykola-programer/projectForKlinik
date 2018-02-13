package ua.nike.project.struct;
import java.util.Date;

public class OperationDay {
    private Integer index = null;
    private Date date = null;

    public OperationDay(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
