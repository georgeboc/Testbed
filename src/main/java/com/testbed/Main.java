package com.testbed;

import com.testbed.configuration.SpringConfiguration;
import com.testbed.factories.InteractorFactory;
import com.testbed.interactors.Interactor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private static final String DEFAULT_PIPELINE_PATH = "parameters/pipeline.json";
    private static final String DEFAULT_OPERATION_INSTRUMENTATIONS_PATH = "output/operation_instrumentations.csv";
    private static final String DEFAULT_TOLERABLE_ERROR_PERCENTAGE = "5.0";

    public static void main(final String[] args) throws Exception {
        InteractorFactory interactorFactory = getReadJsonAndPrintContentFactory();
        System.out.printf("Introduce JSON pipeline file path (default: %s):%n", DEFAULT_PIPELINE_PATH);
        String pipelinePath = readLineOrDefault(DEFAULT_PIPELINE_PATH);
        System.out.printf("Introduce CSV operation instrumentations output file path (default: %s):%n", DEFAULT_OPERATION_INSTRUMENTATIONS_PATH);
        String operationInstrumentationsPath = readLineOrDefault(DEFAULT_OPERATION_INSTRUMENTATIONS_PATH);
        System.out.printf("Introduce the tolerable error percentage (default: %s):%n", DEFAULT_TOLERABLE_ERROR_PERCENTAGE);
        double tolerableErrorPercentage = Double.parseDouble(readLineOrDefault(DEFAULT_TOLERABLE_ERROR_PERCENTAGE));
        Interactor interactor = interactorFactory.getReadJsonAndPrintContent(pipelinePath,
                operationInstrumentationsPath,
                tolerableErrorPercentage);
        interactor.execute();
    }

    private static String readLineOrDefault(final String defaultValue) throws IOException {
        String lineRead = getBufferedReader().readLine();
        if (lineRead.isBlank()) {
            return defaultValue;
        }
        return lineRead;
    }

    private static BufferedReader getBufferedReader() {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        return new BufferedReader(inputStreamReader);
    }

    private static InteractorFactory getReadJsonAndPrintContentFactory() {
        AnnotationConfigApplicationContext configuration = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        return configuration.getBean(InteractorFactory.class);
    }
}
