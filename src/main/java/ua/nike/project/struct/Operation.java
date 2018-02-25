package ua.nike.project.struct;

import java.time.LocalTime;
import java.util.Objects;

public class Operation {
    private Integer index;
    private Integer operationDay_id;
    private LocalTime timeForCome;
    private Integer numberOfOrder;
    private Integer patient_id;
    private Integer operation_id;
    private String eye;
    private String surgeon;
    private String manager;
    private String note;

    /**
     * Initializes a newly created {@code Operation} object.
     */
    public Operation() {
    }

    @Deprecated
    public Operation(Integer index, Integer operationDay_id, LocalTime timeForCome, Integer numberOfOrder, Integer patient_id, Integer operation_id, String eye, String surgeon, String manager, String note) {
        this.index = index;
        setOperationDay_id(operationDay_id);
        setTimeForCome(timeForCome);
        setNumberOfOrder(numberOfOrder);
        setPatient_id(patient_id);
        setOperation_id(operation_id);
        setEye(eye);
        setSurgeon(surgeon);
        setManager(manager);
        setNote(note);
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getOperationDay_id() {
        return operationDay_id;
    }

    public void setOperationDay_id(Integer operationDay_id) {
        this.operationDay_id = operationDay_id;
    }

    public LocalTime getTimeForCome() {
        return timeForCome;
    }

    public void setTimeForCome(LocalTime timeForCome) {
        this.timeForCome = timeForCome;
    }

    public Integer getNumberOfOrder() {
        return numberOfOrder;
    }

    public void setNumberOfOrder(Integer numberOfOrder) {
        this.numberOfOrder = numberOfOrder;
    }

    public Integer getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(Integer patient_id) {
        this.patient_id = patient_id;
    }

    public Integer getOperation_id() {
        return operation_id;
    }

    public void setOperation_id(Integer operation_id) {
        this.operation_id = operation_id;
    }

    public String getEye() {
        return eye;
    }

    public void setEye(String eye) {
        this.eye = eye.toUpperCase();
    }

    public String getSurgeon() {
        return surgeon;
    }

    public void setSurgeon(String surgeon) {
        this.surgeon = firstUpperCase(surgeon);
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = firstUpperCase(manager);
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Operation)) return false;
        Operation operation = (Operation) o;
        return Objects.equals(index, operation.index) &&
                Objects.equals(operationDay_id, operation.operationDay_id) &&
                Objects.equals(timeForCome, operation.timeForCome) &&
                Objects.equals(numberOfOrder, operation.numberOfOrder) &&
                Objects.equals(patient_id, operation.patient_id) &&
                Objects.equals(operation_id, operation.operation_id) &&
                Objects.equals(eye, operation.eye) &&
                Objects.equals(surgeon, operation.surgeon) &&
                Objects.equals(manager, operation.manager) &&
                Objects.equals(note, operation.note);
    }

    @Override
    public int hashCode() {

        return Objects.hash(index, operationDay_id, timeForCome, numberOfOrder, patient_id, operation_id, eye, surgeon, manager, note);
    }

    @Override
    public String toString() {
        return "Operation{" +
                "index=" + index +
                ", operationDay_id=" + operationDay_id +
                ", timeForCome=" + timeForCome +
                ", numberOfOrder=" + numberOfOrder +
                ", patient_id=" + patient_id +
                ", operation_id=" + operation_id +
                ", eye='" + eye + '\'' +
                ", surgeon='" + surgeon + '\'' +
                ", manager='" + manager + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
