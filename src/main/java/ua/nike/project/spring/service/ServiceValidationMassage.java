package ua.nike.project.spring.service;

import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class ServiceValidationMassage {
    private Properties properties = new Properties();

     {
        try {
            properties.load(new FileInputStream(this.getClass().getClassLoader().getResource("validation/ua.properties").getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String value(String key) {
        try {
            return new String(properties.getProperty(key).getBytes("ISO8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
