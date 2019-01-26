package ua.nike.project.spring.vo;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class VisitDateVO implements VisualObject {

    private Integer visitDateId;

    @NotNull(message = "visit_dates.date.null")
    @Future(message = "visit_dates.date.future")
    private LocalDate date;
    private boolean disable;

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

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
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
        sb.append(", disable=").append(disable);
        sb.append('}');
        return sb.toString();
    }
}
