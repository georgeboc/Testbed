package com.testbed.boundary.serializers;

import com.testbed.entities.instrumentation.CallInstrumentation;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CSVCallInstrumentationsSerializer implements Serializer<List<CallInstrumentation>> {
    private static final List<String> HEADERS = Arrays.stream(CallInstrumentation.class.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());

    @Override
    public void serialize(String path, List<CallInstrumentation> callInstrumentations) {
        try (FileWriter fileWriter = new FileWriter(path)) {
            tryCsvPrinter(fileWriter, callInstrumentations);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void tryCsvPrinter(FileWriter fileWriter, List<CallInstrumentation> callInstrumentations) {
        try (CSVPrinter printer = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withHeader(String.valueOf(HEADERS)))) {
            callInstrumentations.forEach(callInstrumentation -> tryPrintRecord(printer, callInstrumentation));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void tryPrintRecord(CSVPrinter printer, CallInstrumentation callInstrumentation) {
        try {
            printer.printRecord(callInstrumentation.getClassName(),
                    callInstrumentation.getInputRowsCount(),
                    callInstrumentation.getOutputRowsCount(),
                    callInstrumentation.getInstantBeforeExecution(),
                    callInstrumentation.getInstantAfterExecution(),
                    callInstrumentation.getExecutionDuration());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
