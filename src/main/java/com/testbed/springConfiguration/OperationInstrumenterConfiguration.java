package com.testbed.springConfiguration;

import com.testbed.boundary.invocations.Operation;
import com.testbed.boundary.invocations.instrumentation.OperationInstrumentation;
import com.testbed.boundary.invocations.instrumentation.OperationInstrumenter;
import com.testbed.boundary.invocations.intermediateDatasets.instrumentation.IntermediateDatasetInstrumentation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.inject.Inject;
import java.util.List;

@Configuration
public class OperationInstrumenterConfiguration {
    private static final String WITH_LATE_INITIALIZATION = "prototype";

    @Inject
    private List<OperationInstrumentation> operationInstrumentations;

    @Inject
    private IntermediateDatasetInstrumentation intermediateDatasetInstrumentation;

    @Bean
    @Scope(value = WITH_LATE_INITIALIZATION)
    public OperationInstrumenter operationInstrumenter(Operation wrappedOperation) {
        return new OperationInstrumenter(wrappedOperation, intermediateDatasetInstrumentation, operationInstrumentations);
    }
}
