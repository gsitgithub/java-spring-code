package dev.gsitgithub.webapp.config.format.list;

import dev.gsitgithub.webapp.config.format.annotation.ListFormat;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.util.*;

public class ListFormatAnnotationFormatterFactory
        implements AnnotationFormatterFactory<ListFormat> {

    private static final Set<Class<?>> FIELD_TYPES;

    static {
        Set<Class<?>> fieldTypes = new HashSet<>(7);
        fieldTypes.add(Object.class);
        fieldTypes.add(Map.class);
        fieldTypes.add(Collection.class);
        FIELD_TYPES = Collections.unmodifiableSet(fieldTypes);

    }

    @Override
    public final Set<Class<?>> getFieldTypes() {
        return FIELD_TYPES;
    }

    @Override
    public Printer<?> getPrinter(ListFormat annotation, Class<?> fieldType) {
        return new ListPrinter(annotation.prefix(), annotation.separator(), annotation.suffix());
    }

    @Override
    public Parser<Object> getParser(ListFormat annotation, Class<?> fieldType) {
        return new ListParser();
    }
}