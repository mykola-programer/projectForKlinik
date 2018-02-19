package ua.nike.project.struct;
import java.util.Date;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OperationDay)) return false;
        OperationDay that = (OperationDay) o;
        return Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {

        return Objects.hash(date);
    }
}
