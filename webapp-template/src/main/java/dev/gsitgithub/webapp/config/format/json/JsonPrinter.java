package dev.gsitgithub.webapp.config.format.json;

import com.google.gson.Gson;
import org.springframework.format.Printer;

import java.util.Locale;

public class JsonPrinter implements Printer<Object> {

    @Override
    public String print(Object object, Locale locale) {
        return new Gson().toJson(object);
    }
}
