package ua.nike.project.spring.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

public class OperationTypeVO implements VisualObject {

    private Integer operationTypeId;

    @NotNull(message = "operationType.name.null")
    @Size(min = 1, max = 50, message = "operationType.name.size")
    @Pattern(regexp = "[A-Za-zА-Яа-яЁёІіЇїЄє +-.]+", message = "operationType.name.pattern")
    private String name;
    private boolean disable;

    public Integer getOperationTypeId() {
        return operationTypeId;
    }

    public void setOperationTypeId(Integer operationTypeId) {
        this.operationTypeId = operationTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        OperationTypeVO that = (OperationTypeVO) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OperationTypeVO{");
        sb.append("operationTypeId=").append(operationTypeId);
        sb.append(", name='").append(name).append('\'');
        sb.append(", disable=").append(disable);
        sb.append('}');
        return sb.toString();
    }
}
