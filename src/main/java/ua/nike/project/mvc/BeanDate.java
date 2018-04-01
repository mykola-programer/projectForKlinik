package ua.nike.project.mvc;

import java.io.Serializable;
import java.util.Date;


public class BeanDate implements Serializable {
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
