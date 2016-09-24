package dev.gsitgithub.webapp.config.format.json;

import dev.gsitgithub.webapp.config.format.annotation.JsonFormat;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.util.*;

public class JsonFormatAnnotationFormatterFactory
        implements AnnotationFormatterFactory<JsonFormat> {

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
    public Printer<?> getPrinter(JsonFormat annotation, Class<?> fieldType) {
        return new JsonPrinter();
    }

    @Override
    public Parser<Object> getParser(JsonFormat annotation, Class<?> fieldType) {
        return new JsonParser(fieldType);
    }
}
