package dev.gsitgithub.webapp.config.format.json;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.format.Parser;

import java.text.ParseException;
import java.util.Locale;

@AllArgsConstructor
public class JsonParser implements Parser<Object> {

    private final Class<?> fieldType;

    @Override
    public Object parse(String text, Locale locale) throws ParseException {
        return new Gson().fromJson(text, fieldType);
    }

}
