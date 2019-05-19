package ua.nike.project.spring.vo;

import java.util.Objects;

public class SurgeonPlanVO implements VisualObject {

    private Integer surgeonPlanId;
//    private Integer datePlanId;
    private DatePlanVO datePlan;
    private Integer surgeonId;
    private boolean disable;

    public Integer getSurgeonPlanId() {
        return surgeonPlanId;
    }

    public void setSurgeonPlanId(Integer surgeonPlanId) {
        this.surgeonPlanId = surgeonPlanId;
    }

    public DatePlanVO getDatePlan() {
        return datePlan;
    }

    public void setDatePlan(DatePlanVO datePlan) {
        this.datePlan = datePlan;
    }

    public Integer getSurgeonId() {
        return surgeonId;
    }

    public void setSurgeonId(Integer surgeonId) {
        this.surgeonId = surgeonId;
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
        SurgeonPlanVO that = (SurgeonPlanVO) o;
        return Objects.equals(datePlan, that.datePlan) &&
                Objects.equals(surgeonId, that.surgeonId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(datePlan, surgeonId);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SurgeonPlanVO{");
        sb.append("surgeonPlanId=").append(surgeonPlanId);
        sb.append(", datePlan=").append(datePlan);
        sb.append(", surgeonId=").append(surgeonId);
        sb.append(", disable=").append(disable);
        sb.append('}');
        return sb.toString();
    }
}
