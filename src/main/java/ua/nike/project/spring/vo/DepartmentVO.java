package ua.nike.project.spring.vo;

import java.util.Objects;

public class DepartmentVO implements VisualObject {

    private Integer departmentId;
    private String name;
    private Boolean disable;

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = firstUpperCase(name);
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentVO that = (DepartmentVO) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DepartmentVO{");
        sb.append("departmentId=").append(departmentId);
        sb.append(", name='").append(name).append('\'');
        sb.append(", disable=").append(disable);
        sb.append('}');
        return sb.toString();
    }

    private String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }
}
