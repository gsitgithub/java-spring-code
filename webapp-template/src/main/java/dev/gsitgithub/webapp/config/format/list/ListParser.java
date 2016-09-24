package dev.gsitgithub.webapp.config.format.list;

import lombok.AllArgsConstructor;
import org.springframework.format.Parser;

import java.text.ParseException;
import java.util.Locale;

@AllArgsConstructor
public class ListParser implements Parser<Object> {

    @Override
    public Object parse(String text, Locale locale) throws ParseException {
        return null;
    }

}
