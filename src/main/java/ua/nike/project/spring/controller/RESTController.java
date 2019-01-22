package ua.nike.project.spring.controller;

import org.springframework.validation.BindingResult;
import ua.nike.project.spring.exceptions.ValidationException;
import ua.nike.project.spring.vo.VisualObject;

import java.util.List;

public interface RESTController<T extends VisualObject> {

    T getByID(int id);

    List<T> getAll();

    T add(T objectVO, BindingResult bindingResult) throws ValidationException;

    T editByID(int id, T objectVO, BindingResult bindingResult) throws ValidationException;

    boolean deleteByID(int id);
}
