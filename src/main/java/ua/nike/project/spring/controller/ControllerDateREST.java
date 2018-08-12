package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.hibernate.entity.OperationDate;
import ua.nike.project.spring.dao.OperationDateDAO;
import ua.nike.project.spring.vo.OperationDateVO;
import ua.nike.project.spring.vo.PatientVO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/dates")
public class ControllerDateREST {

    @Autowired
    OperationDateDAO operationDateDAO;

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<NgbDateStruct> getDates() {
        List<NgbDateStruct> result = new ArrayList<>();
        for (LocalDate localDate : operationDateDAO.getDates()) {
            result.add(new NgbDateStruct(localDate.getDayOfMonth(), localDate.getMonthValue(), localDate.getYear()));
        }
        return result;

    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<OperationDateVO> addDates(@RequestBody List <NgbDateStruct> ngbDates) {
        Set<LocalDate> localDates = new HashSet<>();
        for (NgbDateStruct ngbDate : ngbDates) {
            localDates.add(LocalDate.of(ngbDate.getYear(), ngbDate.getMonth(), ngbDate.getDay()));
        }
        return operationDateDAO.saveDates(localDates);
    }

private static class NgbDateStruct {
        private int day;
        private int month;
        private int year;

    public NgbDateStruct() {
    }

    public NgbDateStruct(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "NgbDateStruct{" +
                "day=" + day +
                ", month=" + month +
                ", year=" + year +
                '}';
    }
}
}