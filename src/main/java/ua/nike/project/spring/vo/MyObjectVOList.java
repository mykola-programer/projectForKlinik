package ua.nike.project.spring.vo;

import javax.validation.Valid;
import java.util.List;

public class MyObjectVOList<T extends VisualObject> {
    @Valid
    private List<T> objects;

    public List<T> getObjects() {
        return objects;
    }

    public void setObjects(List<T> objects) {
        this.objects = objects;
    }
}
