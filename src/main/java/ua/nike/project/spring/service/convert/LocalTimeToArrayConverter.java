package ua.nike.project.spring.service.convert;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

public class LocalTimeToArrayConverter extends StdConverter<LocalTime, ArrayList> {

    @Override
    public ArrayList<Integer> convert(LocalTime value) {
        return new ArrayList<>(Arrays.asList(value.getHour(), value.getMinute()));
    }
}

