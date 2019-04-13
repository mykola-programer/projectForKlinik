package ua.nike.project.spring.service.convert;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.time.LocalDate;
import java.util.ArrayList;

public class ArrayToLocalDateConverter extends StdConverter<ArrayList<Integer>, LocalDate> {

    @Override
    public LocalDate convert(ArrayList<Integer> value) {
        return LocalDate.of(value.get(0), value.get(1), value.get(2));
    }
}
