package com.testbed.springConfiguration;

import com.testbed.boundary.invocations.InstrumentInvokable;
import com.testbed.boundary.invocations.Invokable;
import com.testbed.boundary.invocations.OperationInstrumentation;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;
import java.util.List;

@Configuration
public class InvocablesConfigurationCommons {
    public static final String PHYSICAL_LOAD = "PhysicalLoad";
    public static final String PHYSICAL_SELECT = "PhysicalSelect";
    public static final String PHYSICAL_PROJECT = "PhysicalProject";
    public static final String PHYSICAL_JOIN = "PhysicalJoin";
    public static final String PHYSICAL_GROUP_BY = "PhysicalGroupBy";
    public static final String PHYSICAL_AGGREGATE = "PhysicalAggregate";
    public static final String PHYSICAL_UNION = "PhysicalUnion";
    public static final String PHYSICAL_SINK = "PhysicalSink";

    public static Invokable instrumentInvokable(Invokable wrappedInvokable, List<OperationInstrumentation> operationInstrumentations) {
        return new InstrumentInvokable(wrappedInvokable, operationInstrumentations);
    }
}
