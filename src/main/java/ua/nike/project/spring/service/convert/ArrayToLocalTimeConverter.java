package ua.nike.project.spring.service.convert;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.time.LocalTime;
import java.util.ArrayList;

public class ArrayToLocalTimeConverter extends StdConverter<ArrayList<Integer>, LocalTime> {

    @Override
    public LocalTime convert(ArrayList<Integer> value) {
        return LocalTime.of(value.get(0), value.get(1), 0);
    }
}
