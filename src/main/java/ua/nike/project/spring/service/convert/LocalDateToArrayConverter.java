package ua.nike.project.spring.service.convert;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class LocalDateToArrayConverter extends StdConverter<LocalDate, ArrayList> {

    @Override
    public ArrayList<Integer> convert(LocalDate value) {
        return new ArrayList<>(Arrays.asList(value.getYear(), value.getMonthValue(), value.getDayOfMonth()));
    }
}

