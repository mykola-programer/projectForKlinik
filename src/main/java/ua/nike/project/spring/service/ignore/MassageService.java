package ua.nike.project.spring.service;

import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class MassageService {
    private Properties properties = new Properties();

     {
        try (FileInputStream fileInputStream = new FileInputStream(this.getClass().getClassLoader().getResource("validation/ua.properties").getFile())){
            properties.load(fileInputStream);
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }

    public String getValue(String key) {
        try {
            return new String(properties.getProperty(key, key).getBytes("ISO8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
