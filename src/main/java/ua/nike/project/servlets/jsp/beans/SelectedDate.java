package ua.nike.project.servlets.jsp.beans;

public class SelectedDate {
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        if (date == null) {
            date = "";
        }
        this.date = date;
    }
}
