package ua.nike.project;

import ua.nike.project.service.ConnectToBase;
import ua.nike.project.struct.Patient;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Statement;

public class TestReflection {

    public static void main(String[] args) {
        Field[] fields;
        String[] keys;
        Class<? extends Patient> clss = Patient.class;
        //Class clss = Class.forName("ua.nike.project.struct.Patient");
        System.out.println(clss.getName());
        fields = clss.getDeclaredFields();
        int i = 0;
        keys = new String[fields.length];
        for (Field field : fields) {
            keys[i] = field.getName();
            System.out.println(field.getModifiers() + "   " + field.getType().getName() + "  " + field.getName());
            i++;

        }
//            Constructor[] constructor = clss.getDeclaredConstructors();
//            Parameter[] parameters = constructor[1].getParameters();
//            for (Parameter parameter : parameters) {
//                System.out.println(parameter);
//            }

// ----------------   Create table with fields from class Patient.    ----------------------
        try (Statement statement = ConnectToBase.getConnection().createStatement()) {
            statement.executeUpdate(String.format("create table test1 (%s serial primary key)", keys[0]));
            for (int j = 1; j <= keys.length - 1; j++) {
                statement.executeUpdate(String.format("ALTER TABLE test1 ADD %s VARCHAR(25);", keys[j]));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //System.out.println(clss.getDeclaredField("surname"));


    }

}
