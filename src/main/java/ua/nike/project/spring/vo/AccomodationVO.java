package ua.nike.project.spring.vo;

import java.util.Objects;

public class AccomodationVO implements VisualObject {

    private Integer accomodationId;
    private Integer ward;
    private Integer wardPlace;
    private Boolean disable;

    public Integer getAccomodationId() {
        return accomodationId;
    }

    public void setAccomodationId(Integer accomodationId) {
        this.accomodationId = accomodationId;
    }

    public Integer getWard() {
        return ward;
    }

    public void setWard(Integer ward) {
        this.ward = ward;
    }

    public Integer getWardPlace() {
        return wardPlace;
    }

    public void setWardPlace(Integer wardPlace) {
        this.wardPlace = wardPlace;
    }

    public Boolean isDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccomodationVO that = (AccomodationVO) o;
        return ward == that.ward &&
                Objects.equals(wardPlace, that.wardPlace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ward, wardPlace);
    }

    @Override
    public String toString() {
        return "Accomodation{" +
                "accomodationId=" + accomodationId +
                ", ward=" + ward +
                ", wardPlace=" + wardPlace +
                ", disable=" + disable +
                '}';
    }


}
