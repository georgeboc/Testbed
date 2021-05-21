package com.testbed.interactors.viewers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Streams;
import com.testbed.boundary.invocations.instrumentation.OperationInstrumentation;
import com.testbed.boundary.writers.Position;
import com.testbed.boundary.writers.SpreadsheetWriter;
import com.testbed.entities.parameters.OutputParameters;
import com.testbed.views.InvocationInstrumentationView;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
@RequiredArgsConstructor
public class InstrumentatedInvocationsViewer {
    private static final char UNDERSCORE = '_';
    private static final char SPACE = ' ';
    private static final int FIRST_ROW = 0;
    private static final int SECOND_ROW = 1;
    private static final int FIRST_COLUMN = 0;
    private static final String HEADER_COLOR_NAME = "LIME";

    private final SpreadsheetWriter spreadsheetWriter;
    private final ObjectMapper objectMapper;

    public void view(final OutputParameters outputParameters,
                     final List<OperationInstrumentation> operationInstrumentations) {
        removeSheetIfOverwriteIsEnabled(outputParameters);
        LongStream naturalSuccession = LongStream.iterate(1, i -> i + 1);
        List<InvocationInstrumentationView> invocationInstrumentationViews =
                Streams.zip(naturalSuccession.boxed(),
                        operationInstrumentations.stream(),
                        this::getInvocationInstrumentationView)
                .collect(Collectors.toList());
        writeViews(outputParameters, invocationInstrumentationViews);
    }

    private void removeSheetIfOverwriteIsEnabled(final OutputParameters outputParameters) {
        if (outputParameters.isOverwriteSheetEnabled()) {
            spreadsheetWriter.removeSheet(outputParameters);
        }
    }

    private void writeViews(final OutputParameters outputParameters, final List<InvocationInstrumentationView> views) {
        writeHeaders(outputParameters);
        IntStream rowsSuccession = IntStream.iterate(SECOND_ROW, i -> i + 1);
        Streams.forEachPair(rowsSuccession.boxed(), views.stream(),
                (row, view) -> writeView(outputParameters, view, row));
    }

    private void writeHeaders(final OutputParameters outputParameters) {
        IntStream columnsSuccession = IntStream.iterate(FIRST_COLUMN, i -> i + 1);
        Stream<Position> positionStream = columnsSuccession.mapToObj(column -> Position.builder()
                .column(column).row(FIRST_ROW).build());
        List<String> headers = getHeaders();
        Streams.forEachPair(positionStream, headers.stream(),
                (position, header) -> spreadsheetWriter.writeWithColor(outputParameters, position, header, HEADER_COLOR_NAME));
    }

    private void writeView(final OutputParameters outputParameters,
                           final InvocationInstrumentationView view,
                           final int row) {
        IntStream columnsSuccession = IntStream.iterate(FIRST_COLUMN, i -> i + 1);
        Stream<Position> positionStream = columnsSuccession.mapToObj(column -> Position.builder().row(row).column(column).build());
        Stream<String> values = Arrays.stream(InvocationInstrumentationView.class.getDeclaredFields())
                .map(field -> tryGetFieldsContent(view, field).toString());
        Streams.forEachPair(positionStream, values,
                (position, value) -> spreadsheetWriter.write(outputParameters, position, value));
    }

    private Object tryGetFieldsContent(final Object object, final Field field) {
        try {
            return field.get(object);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    private List<String> getHeaders() {
        return Arrays.stream(InvocationInstrumentationView.class.getDeclaredFields())
                .map(Field::getName)
                .map(fieldName -> CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName))
                .map(fieldName -> fieldName.replace(UNDERSCORE, SPACE))
                .map(StringUtils::capitalize)
                .collect(Collectors.toList());
    }

    public InvocationInstrumentationView getInvocationInstrumentationView(final long invocationOrder,
                                                                          final OperationInstrumentation operationInstrumentation) {
        InvocationInstrumentationView invocationInstrumentationView = objectMapper.convertValue(operationInstrumentation,
                InvocationInstrumentationView.class);
        invocationInstrumentationView.invocationOrder = invocationOrder;
        return invocationInstrumentationView;
    }
}
