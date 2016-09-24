package dev.gsitgithub.webapp.config.format.custom;

import lombok.AllArgsConstructor;
import org.springframework.format.Parser;

import java.text.ParseException;
import java.util.Locale;

@AllArgsConstructor
public class CustomParser implements Parser<Object> {

    @Override
    public Object parse(String text, Locale locale) throws ParseException {
        return null;
    }

}
