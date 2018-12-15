package ua.nike.project.hibernate.type;

public enum ClientStatus {
    PATIENT,
    RELATIVE;

    public static ClientStatus getInstance(String status) {
        switch (status) {
            case "пацієнт":
                return ClientStatus.PATIENT;
            case "супров.":
            case "супроводжуючий":
            default:
                return ClientStatus.RELATIVE;
        }
    }
   public String convertToCyrillic () {
        switch (this.toString().toUpperCase()) {
            case "RELATIVE":
                return "супров.";
            case "PATIENT":
            default:
                return "пацієнт";
        }
    }

}
