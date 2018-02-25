package ua.nike.project.struct;

import java.time.LocalDate;
import java.util.Objects;

public class OperationDay {
    private Integer operationDay_id;
    private LocalDate date;
    private String surgeon;

    /**
     * Initializes a newly created {@code OperationDay} object.
     */
    public OperationDay() {
    }

    /**
     * Returns the {@code Integer} value of the operationDay_id from OperationDay.
     */
    public Integer getOperationDay_id() {
        return operationDay_id;
    }

    /**
     * Set operationDay_id.
     *
     * @param operationDay_id
     */
    public void setOperationDay_id(Integer operationDay_id) {
        this.operationDay_id = operationDay_id;
    }

    /**
     * Returns the {@code LocateDate} value of the date from OperationDay.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Set date.
     *
     * @param date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Returns the {@code String} value of the surgeon from OperationDay.
     */
    public String getSurgeon() {
        return surgeon;
    }

    /**
     * Set surgeon.
     *
     * @param surgeon
     */
    public void setSurgeon(String surgeon) {
        this.surgeon = firstUpperCase(surgeon);
    }

    /**
     * Convert first symbol in this {@code String} to Upper Case. Another symbols is Lower Case.
     *
     * @param word This is a {@code String}, that needs to be converted to a specific format.
     * @return {@code String} first symbol to Upper Case and another symbols is Lower Case.
     * If word is null or "", that return "".
     */
    private String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }

    @Override
    public String toString() {
        return "OperationDay{" +
                "operationDay_id=" + operationDay_id +
                ", date=" + date +
                ", surgeon='" + surgeon + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OperationDay)) return false;
        OperationDay that = (OperationDay) o;
        return Objects.equals(operationDay_id, that.operationDay_id) &&
                Objects.equals(date, that.date) &&
                Objects.equals(surgeon, that.surgeon);
    }

    @Override
    public int hashCode() {

        return Objects.hash(operationDay_id, date, surgeon);
    }
}
