package com.testbed.boundary.serializers;

import com.google.common.base.CaseFormat;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
@RequiredArgsConstructor
public class CSVSerializer<T> implements Serializer<List<T>> {
    private static final char UNDERSCORE = '_';
    private static final char SPACE = ' ';
    private final Class<T> tType;

    @Override
    public void serialize(String path, List<T> elements) {
        try (FileWriter fileWriter = new FileWriter(path)) {
            tryPrintElements(fileWriter, elements);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void tryPrintElements(FileWriter fileWriter, List<T> elements) {
        try (CSVPrinter printer = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withHeader(getHeaders()))) {
            elements.forEach(element -> tryPrintElement(printer, element));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private String[] getHeaders() {
        return Arrays.stream(tType.getDeclaredFields())
                .map(Field::getName)
                .map(fieldName -> CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName))
                .map(fieldName -> fieldName.replace(UNDERSCORE, SPACE))
                .map(StringUtils::capitalize)
                .toArray(String[]::new);
    }

    private void tryPrintElement(CSVPrinter csvPrinter, T element) {
        try {
            printElement(csvPrinter, element);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void printElement(CSVPrinter csvPrinter, T element) throws IOException {
        Arrays.stream(tType.getDeclaredFields()).forEach(ReflectionUtils::makeAccessible);
        Object[] fieldsContents = Arrays.stream(tType.getDeclaredFields())
                .map(field->tryGetFieldsContent(element, field))
                .toArray(Object[]::new);
        csvPrinter.printRecord(fieldsContents);
    }

    private Object tryGetFieldsContent(T element, Field field) {
        try {
            return field.get(element);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }
}
