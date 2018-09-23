package ua.nike.project.spring.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class VisitDateVO implements Serializable {

    private Integer visitDateId;
    private LocalDate date;
    private boolean lock = false;

    public Integer getVisitDateId() {
        return visitDateId;
    }

    public void setVisitDateId(Integer visitDateId) {
        this.visitDateId = visitDateId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VisitDateVO that = (VisitDateVO) o;

        return date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VisitDateVO{");
        sb.append("visitDateId=").append(visitDateId);
        sb.append(", date=").append(date);
        sb.append(", lock=").append(lock);
        sb.append('}');
        return sb.toString();
    }
}
