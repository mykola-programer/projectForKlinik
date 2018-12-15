package ua.nike.project.hibernate.type;

public enum Ward {
    N201,
    N202,
    N203,
    N204,
    N205,
    N206,
    N207;

    public Integer toInteger() {
        return Integer.valueOf(this.toString().substring(1));
    }
}