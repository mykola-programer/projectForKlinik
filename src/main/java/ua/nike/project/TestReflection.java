package ua.nike.project;

import ua.nike.project.service.JdbcStoragePatient;
import ua.nike.project.struct.Patient;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;

public class TestReflection {

    public static void main(String[] args) {
        Field[] fields;
        String[] keys;
        try {
            Class clss = Class.forName("ua.nike.project.struct.Patient");
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
            JdbcStoragePatient jdbcStoragePatient = new JdbcStoragePatient();
            try (Statement statement = jdbcStoragePatient.getConnect().createStatement()) {
                statement.executeUpdate(String.format("create table test1 (%s serial primary key)",keys[0]));
                for (int j = 1; j<=keys.length-1; j++) {
                    statement.executeUpdate(String.format("ALTER TABLE test1 ADD %s VARCHAR(25);", keys[j]));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //System.out.println(clss.getDeclaredField("surname"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("No class 'Patient'");
        }


    }

}
