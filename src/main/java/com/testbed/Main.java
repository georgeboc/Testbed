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
    private static final String DEFAULT_CALL_INSTRUMENTATIONS_PATH = "output/calls_instrumentation.csv";

    public static void main(String[] args) throws Exception {
            System.out.println("Calling Read Json And Print Content Interactor");
        InteractorFactory interactorFactory = getReadJsonAndPrintContentFactory();

        System.out.printf("Introduce JSON pipeline file path (default: %s):%n", DEFAULT_PIPELINE_PATH);
        String pathOrDefault = readLineOrDefault(DEFAULT_PIPELINE_PATH);
        System.out.printf("Introduce CSV call instrumentations output file path (default: %s):%n", DEFAULT_CALL_INSTRUMENTATIONS_PATH);
        String callsInstrumentationOrDefault = readLineOrDefault(DEFAULT_CALL_INSTRUMENTATIONS_PATH);
        Interactor interactor = interactorFactory.getReadJsonAndPrintContent(pathOrDefault, callsInstrumentationOrDefault);
        interactor.execute();
    }

    private static String readLineOrDefault(String defaultCallInstrumentationsPath) throws IOException {
        String lineRead = getBufferedReader().readLine();
        if (lineRead.equals("")) {
            return defaultCallInstrumentationsPath;
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
