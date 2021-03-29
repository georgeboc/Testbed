package com.testbed.boundary.serializers;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Streams;
import com.testbed.entities.instrumentation.OperationInstrumentation;
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
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class CSVOperationInstrumentationsSerializer implements Serializer<List<OperationInstrumentation>> {
    private static final String OPERATION_NUMBER_FIELD = "Operation #";
    private static final String[] HEADERS = Stream.concat(Stream.of(OPERATION_NUMBER_FIELD),
            Arrays.stream(OperationInstrumentation.class.getDeclaredFields())
            .map(Field::getName)
            .map(fieldName -> CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName))
            .map(fieldName -> fieldName.replace('_', ' '))
            .map(StringUtils::capitalize))
            .toArray(String[]::new);

    @Override
    public void serialize(String path, List<OperationInstrumentation> operationInstrumentations) {
        try (FileWriter fileWriter = new FileWriter(path)) {
            tryPrintOperationInstrumentations(fileWriter, operationInstrumentations);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void tryPrintOperationInstrumentations(FileWriter fileWriter, List<OperationInstrumentation> operationInstrumentations) {
        try (CSVPrinter printer = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withHeader(HEADERS))) {
            LongStream naturalSuccession = LongStream.iterate(1, i -> i + 1);
            Streams.zip(naturalSuccession.boxed(), operationInstrumentations.stream(), Record::new)
                    .forEach(record -> tryPrintRecord(printer, record));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void tryPrintRecord(CSVPrinter csvPrinter, Record record) {
        try {
            Object[] fieldsContents = Streams.concat(Stream.of(record.index),
                    Arrays.stream(OperationInstrumentation.class.getDeclaredFields())
                    .peek(ReflectionUtils::makeAccessible)
                    .map(field->tryGetFieldsContent(record.operationInstrumentation, field)))
                    .toArray(Object[]::new);
            csvPrinter.printRecord(fieldsContents);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private Object tryGetFieldsContent(OperationInstrumentation operationInstrumentation, Field field) {
        try {
            return field.get(operationInstrumentation);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    @RequiredArgsConstructor
    private static class Record  {
        private final long index;
        private final OperationInstrumentation operationInstrumentation;
    }
}
