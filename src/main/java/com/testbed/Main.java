package com.testbed;

import com.testbed.interactors.Interactor;
import com.testbed.interactors.InteractorFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private static final String DEFAULT_PIPELINE_PATH = "parameters/pipeline.json";
    private static final String DEFAULT_OPERATION_INSTRUMENTATIONS_PATH = "output/operation_instrumentations.csv";
    private static final String DEFAULT_TOLERABLE_ERROR_PERCENTAGE = "5.0";

    public static void main(final String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("InstrumentedMapReduceComponentScan.xml");
        InteractorFactory interactorFactory = context.getBean(InteractorFactory.class);
        System.out.printf("Introduce JSON pipeline file path (default: %s):%n", DEFAULT_PIPELINE_PATH);
        String pipelinePath = StringUtils.defaultIfBlank(readLine(), DEFAULT_PIPELINE_PATH);
        System.out.printf("Introduce CSV operation instrumentations output file path (default: %s):%n", DEFAULT_OPERATION_INSTRUMENTATIONS_PATH);
        String operationInstrumentationsPath = StringUtils.defaultIfBlank(readLine(), DEFAULT_OPERATION_INSTRUMENTATIONS_PATH);
        System.out.printf("Introduce the tolerable error percentage (default: %s):%n", DEFAULT_TOLERABLE_ERROR_PERCENTAGE);
        double tolerableErrorPercentage = Double.parseDouble(StringUtils.defaultIfBlank(readLine(), DEFAULT_TOLERABLE_ERROR_PERCENTAGE));
        Interactor interactor = interactorFactory.getReadJsonAndPrintContent(pipelinePath,
                operationInstrumentationsPath,
                tolerableErrorPercentage);
        interactor.execute();
    }

    private static String readLine() throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        return bufferedReader.readLine();
    }
}
