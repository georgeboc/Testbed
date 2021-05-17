package com.testbed.interactors.viewers;

import com.google.common.collect.Streams;
import com.testbed.boundary.invocations.frameworks.FrameworkName;
import com.testbed.boundary.writers.Position;
import com.testbed.boundary.writers.SpreadsheetWriter;
import com.testbed.entities.parameters.OutputParameters;
import com.testbed.interactors.monitors.MonitoringInformation;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Math.min;

@SuppressWarnings("UnstableApiUsage")
@RequiredArgsConstructor
public class TimedInvocationsViewer {
    private static final String EMPTY = " ";
    private static final String[] FRAMEWORKS_HEADERS = {EMPTY,
            FrameworkName.Spark.name(), EMPTY, EMPTY, EMPTY,
            FrameworkName.MapReduce.name(), EMPTY, EMPTY, EMPTY};
    private static final String FIRST_INVOCATION = "First Invocation";
    private static final String SECOND_INVOCATION = "Second Invocation";
    private static final String THIRD_INVOCATION = "Third Invocation";
    private static final String MEDIAN = "Median";
    private static final String[] INVOCATIONS_HEADERS = {EMPTY, FIRST_INVOCATION, SECOND_INVOCATION, THIRD_INVOCATION,
    MEDIAN, FIRST_INVOCATION, SECOND_INVOCATION, THIRD_INVOCATION, MEDIAN};
    private static final int FIRST_COLUMN = 0;
    private static final int FIRST_ROW_TOP_HEADERS = 0;
    private static final int SECOND_ROW_TOP_HEADERS = 1;
    private static final int SPARK_FIRST_COLUMN = EMPTY.length();
    private static final int SPARK_LAST_COLUMN = SPARK_FIRST_COLUMN + EMPTY.length()*3;
    private static final int MAPREDUCE_FIRST_COLUMN = SPARK_LAST_COLUMN + 1;
    private static final int MAPREDUCE_LAST_COLUMN = MAPREDUCE_FIRST_COLUMN + EMPTY.length()*3;
    private static final String TOP_HEADER_COLOR_NAME = "LIME";

    private final SpreadsheetWriter spreadsheetWriter;
    private final MonitoringInformationViewer monitoringInformationViewer;

    public void view(final OutputParameters outputParameters,
                     final FrameworkName frameworkName,
                     final MonitoringInformation monitoringInformation) {
        removeSheetIfOverwriteIsEnabled(outputParameters);
        writeTopHeaders(outputParameters);
        int frameworkColumn = FrameworksPosition.valueOf(frameworkName.name()).column;
        monitoringInformationViewer.view(outputParameters, monitoringInformation, frameworkColumn);
    }

    private void removeSheetIfOverwriteIsEnabled(OutputParameters outputParameters) {
        if (outputParameters.isOverwriteSheetEnabled()) {
            spreadsheetWriter.removeSheet(outputParameters);
        }
    }

    private void writeTopHeaders(final OutputParameters outputParameters) {
        writeTopHeaders(outputParameters, FRAMEWORKS_HEADERS, FIRST_ROW_TOP_HEADERS);
        Position sparkFirstPosition = Position.builder().column(SPARK_FIRST_COLUMN).row(FIRST_ROW_TOP_HEADERS).build();
        Position sparkLastPosition = Position.builder().column(SPARK_LAST_COLUMN).row(FIRST_ROW_TOP_HEADERS).build();
        spreadsheetWriter.makeMergedRegion(outputParameters, sparkFirstPosition, sparkLastPosition);
        Position mapReduceFirstPosition = Position.builder().column(MAPREDUCE_FIRST_COLUMN).row(FIRST_ROW_TOP_HEADERS).build();
        Position mapReduceLastPosition = Position.builder().column(MAPREDUCE_LAST_COLUMN).row(FIRST_ROW_TOP_HEADERS).build();
        spreadsheetWriter.makeMergedRegion(outputParameters, mapReduceFirstPosition, mapReduceLastPosition);
        writeTopHeaders(outputParameters, INVOCATIONS_HEADERS, SECOND_ROW_TOP_HEADERS);
    }

    private void writeTopHeaders(final OutputParameters outputParameters, String[] headers, int row) {
        Stream<Position> positionStream = IntStream.iterate(FIRST_COLUMN, i -> i + 1)
                .mapToObj(column -> Position.builder().column(column).row(row).build());
        Streams.forEachPair(positionStream, Arrays.stream(headers),
                (position, header) -> spreadsheetWriter.writeWithColor(outputParameters,
                        position,
                        header,
                        TOP_HEADER_COLOR_NAME));
    }

    @RequiredArgsConstructor
    @ToString
    private enum FrameworksPosition {
        Spark(SPARK_FIRST_COLUMN),
        MapReduce(MAPREDUCE_FIRST_COLUMN);

        private final int column;
    }
}
