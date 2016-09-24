package dev.gsitgithub.webapp.config.format.list;

import org.springframework.format.Printer;

import java.util.Collection;
import java.util.Locale;


public class ListPrinter implements Printer<Object> {

    private final String prefix;
    private final String separator;
    private final String suffix;

    public ListPrinter(String prefix, String separator, String suffix) {
        this.prefix = prefix;
        this.separator = separator;
        this.suffix = suffix;
    }

    @Override
    public String print(Object object, Locale locale) {
        Collection list = (Collection) object;

        StringBuilder builder = new StringBuilder();
        builder.append(prefix);

        int itemNumber = 1;
        for (Object item : list) {
            if (itemNumber >= 2) {
                builder.append(separator);
            }
            builder.append(item.toString());
            itemNumber++;
        }
        builder.append(suffix);
        return builder.toString();
    }
}
