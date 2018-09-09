package ua.nike.project.spring.vo;

import ua.nike.project.hibernate.type.Ward;

import java.io.Serializable;
import java.util.Objects;

public class AccomodationVO implements Serializable {

    private Integer accomodationId;
    private Ward ward;
    private Integer wardPlace;
    private Boolean placeLocked;

    public AccomodationVO() {
    }

    public AccomodationVO(Integer accomodationId, Ward ward, Integer wardPlace, Boolean placeLocked) {
        this.accomodationId = accomodationId;
        this.ward = ward;
        this.wardPlace = wardPlace;
        this.placeLocked = placeLocked;
    }

    public Integer getAccomodationId() {
        return accomodationId;
    }

    public void setAccomodationId(Integer accomodationId) {
        this.accomodationId = accomodationId;
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    public Integer getWardPlace() {
        return wardPlace;
    }

    public void setWardPlace(Integer wardPlace) {
        this.wardPlace = wardPlace;
    }

    public Boolean getPlaceLocked() {
        return placeLocked;
    }

    public void setPlaceLocked(Boolean placeBlocked) {
        this.placeLocked = placeBlocked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccomodationVO that = (AccomodationVO) o;
        return ward == that.ward &&
                Objects.equals(wardPlace, that.wardPlace) &&
                Objects.equals(placeLocked, that.placeLocked);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ward, wardPlace, placeLocked);
    }

    @Override
    public String toString() {
        return "Accomodation{" +
                "accomodationId=" + accomodationId +
                ", ward=" + ward +
                ", wardPlace=" + wardPlace +
                ", placeBlocked=" + placeLocked +
                '}';
    }


}
