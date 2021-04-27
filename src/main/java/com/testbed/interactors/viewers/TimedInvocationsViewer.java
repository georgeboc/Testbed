package com.testbed.interactors.viewers;

import com.google.common.collect.Streams;
import com.testbed.boundary.invocations.frameworks.FrameworkName;
import com.testbed.boundary.writers.Position;
import com.testbed.boundary.writers.SpreadsheetWriter;
import com.testbed.entities.parameters.OutputParameters;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Math.min;

@SuppressWarnings("UnstableApiUsage")
@RequiredArgsConstructor
public class TimedInvocationsViewer {
    private static final String EMPTY = "";
    private static final String FIRST_INVOCATION_TIME = "First invocation time (ns)";
    private static final String SECOND_INVOCATION_TIME = "Second invocation time (ns)";
    private static final String THIRD_INVOCATION_TIME = "Third invocation time (ns)";
    private static final String MEDIAN_INVOCATION_TIME = "Median invocation time (ns)";
    private static final String[] HEADERS = {EMPTY,
            FIRST_INVOCATION_TIME, SECOND_INVOCATION_TIME, THIRD_INVOCATION_TIME, MEDIAN_INVOCATION_TIME};
    private static final int FIRST_COLUMN = 0;
    private static final int LATERAL_HEADER_COLUMN = 0;
    private static final int THIRD_INVOCATION_COLUMN = 3;
    private static final int MEDIAN_COLUMN = 4;
    private static final int TOP_HEADERS_ROW = 0;
    private static final int MAPREDUCE_ROW = 1;
    private static final int SPARK_ROW = 2;
    private static final String TOP_HEADER_COLOR_NAME = "LIME";
    private static final String LATERAL_HEADER_COLOR_NAME = "LIME";
    private static final String FORMULA_FORMAT = "MEDIAN(VALUE(B%1$s),VALUE(C%1$s),VALUE(D%1$s))";

    private final SpreadsheetWriter spreadsheetWriter;

    public void view(final OutputParameters outputParameters,
                     final FrameworkName frameworkName,
                     final long durationInNanoseconds) {
        writeTopHeaders(outputParameters);
        writeFrameworkName(outputParameters, frameworkName);
        int frameworkRow = FrameworksPosition.valueOf(frameworkName.name()).row;
        writeDurationInNanoseconds(outputParameters, frameworkRow, durationInNanoseconds);
        writeMedianFormula(outputParameters, frameworkRow);
    }

    private void writeTopHeaders(final OutputParameters outputParameters) {
        IntStream columnsSuccession = IntStream.iterate(FIRST_COLUMN, i -> i + 1);
        Stream<Position> positionStream = columnsSuccession.mapToObj(column -> Position.builder()
                .column(column).row(TOP_HEADERS_ROW).build());
        Streams.forEachPair(positionStream, Arrays.stream(HEADERS),
                (position, header) -> spreadsheetWriter.writeWithColor(outputParameters, position, header, TOP_HEADER_COLOR_NAME));
    }

    private void writeFrameworkName(final OutputParameters outputParameters,
                                    final FrameworkName frameworkName) {
        Position lateralHeaderPosition = Position.builder()
                .column(LATERAL_HEADER_COLUMN)
                .row(FrameworksPosition.valueOf(frameworkName.name()).row)
                .build();
        spreadsheetWriter.writeWithColor(outputParameters,
                lateralHeaderPosition,
                frameworkName.name(),
                LATERAL_HEADER_COLOR_NAME);
    }

    private void writeDurationInNanoseconds(OutputParameters outputParameters, int row, long durationInNanoseconds) {
        int firstUnwrittenColumn = spreadsheetWriter.getFirstUnwrittenColumn(outputParameters, row);
        int boundedFirstUnwrittenColumn = min(firstUnwrittenColumn, THIRD_INVOCATION_COLUMN);
        Position position = Position.builder().row(row).column(boundedFirstUnwrittenColumn).build();
        spreadsheetWriter.write(outputParameters, position, String.valueOf(durationInNanoseconds));
    }

    private void writeMedianFormula(OutputParameters outputParameters, int row) {
        Position position = Position.builder().row(row).column(MEDIAN_COLUMN).build();
        int spreadsheetRow = row + 1;
        String formula = String.format(FORMULA_FORMAT, spreadsheetRow);
        spreadsheetWriter.addFormula(outputParameters, position, formula);
    }

    @RequiredArgsConstructor
    @ToString
    private enum FrameworksPosition {
        MapReduce(MAPREDUCE_ROW),
        Spark(SPARK_ROW);

        private final int row;
    }
}
