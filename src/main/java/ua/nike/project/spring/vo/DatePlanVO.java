package ua.nike.project.spring.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ua.nike.project.spring.service.convert.ArrayToLocalDateConverter;
import ua.nike.project.spring.service.convert.LocalDateToArrayConverter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;
import java.util.StringJoiner;

public class DatePlanVO implements VisualObject {

    private Integer datePlanId;

    @NotNull(message = "visit_dates.date.null")
    @Future(message = "visit_dates.date.future")
    @JsonSerialize(converter = LocalDateToArrayConverter.class)
    @JsonDeserialize(converter = ArrayToLocalDateConverter.class)
    private LocalDate date;
    private Integer departmentID;
    private boolean disable;

    public Integer getDatePlanId() {
        return datePlanId;
    }

    public void setDatePlanId(Integer datePlanId) {
        this.datePlanId = datePlanId;
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

    public Integer getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(Integer departmentID) {
        this.departmentID = departmentID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatePlanVO that = (DatePlanVO) o;
        return Objects.equals(date, that.date) &&
                Objects.equals(departmentID, that.departmentID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, departmentID);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DatePlanVO.class.getSimpleName() + "[", "]")
                .add("datePlanId=" + datePlanId)
                .add("date=" + date)
                .add("departmentID=" + departmentID)
                .add("disable=" + disable)
                .toString();
    }
}
