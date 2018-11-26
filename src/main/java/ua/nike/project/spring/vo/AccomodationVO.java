package ua.nike.project.spring.vo;

import java.io.Serializable;
import java.util.Objects;

public class AccomodationVO implements Serializable, VisualObject {

    private Integer accomodationId;
    private Integer ward;
    private Integer wardPlace;
    private Boolean inactive;

    public AccomodationVO() {
    }

    public AccomodationVO(Integer accomodationId, Integer ward, Integer wardPlace, Boolean inactive) {
        this.accomodationId = accomodationId;
        this.ward = ward;
        this.wardPlace = wardPlace;
        this.inactive = inactive;
    }

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

    public Boolean getInactive() {
        return inactive;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccomodationVO that = (AccomodationVO) o;
        return ward == that.ward &&
                Objects.equals(wardPlace, that.wardPlace) &&
                Objects.equals(inactive, that.inactive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ward, wardPlace, inactive);
    }

    @Override
    public String toString() {
        return "Accomodation{" +
                "accomodationId=" + accomodationId +
                ", ward=" + ward +
                ", wardPlace=" + wardPlace +
                ", inactive=" + inactive +
                '}';
    }


}
