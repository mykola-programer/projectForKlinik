package ua.nike.project.spring.vo;

import java.io.Serializable;
import java.util.Objects;

public class OperationTypeVO implements Serializable, VisualObject {

    private Integer operationTypeId;
    private String name;
    private boolean lockType;

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

    public boolean isLockType() {
        return lockType;
    }

    public void setLockType(boolean lockType) {
        this.lockType = lockType;
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
        sb.append(", lockType=").append(lockType);
        sb.append('}');
        return sb.toString();
    }
}
