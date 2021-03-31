package com.testbed.interactors.viewers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Streams;
import com.testbed.boundary.invocations.OperationInstrumentation;
import com.testbed.boundary.serializers.CSVSerializer;
import com.testbed.views.InvocationInstrumentationView;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@SuppressWarnings("UnstableApiUsage")
@RequiredArgsConstructor
public class InvocationInstrumentationViewer {
    private final CSVSerializer<InvocationInstrumentationView> invocationInstrumentationViewCSVSerializer;
    private final ObjectMapper objectMapper;

    public void view(String path, List<OperationInstrumentation> operationInstrumentations) {
        LongStream naturalSuccession = LongStream.iterate(1, i -> i + 1);
        List<InvocationInstrumentationView> invocationInstrumentationViews =
                Streams.zip(naturalSuccession.boxed(), operationInstrumentations.stream(), this::getInvocationInstrumentationView)
                .collect(Collectors.toList());
        invocationInstrumentationViewCSVSerializer.serialize(path, invocationInstrumentationViews);
    }

    public InvocationInstrumentationView getInvocationInstrumentationView(long invocationOrder,
                                                                          OperationInstrumentation operationInstrumentation) {
        InvocationInstrumentationView invocationInstrumentationView = objectMapper.convertValue(operationInstrumentation,
                InvocationInstrumentationView.class);
        invocationInstrumentationView.invocationOrder = invocationOrder;
        return invocationInstrumentationView;
    }
}
