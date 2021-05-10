package com.testbed.interactors.viewers;

import com.google.common.collect.Streams;
import com.testbed.boundary.writers.Position;
import com.testbed.boundary.writers.SpreadsheetWriter;
import com.testbed.entities.parameters.OutputParameters;
import com.testbed.interactors.monitors.MonitoringInformation;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
@RequiredArgsConstructor
public class MonitoringInformationViewer {
    private static final int MONITORING_INFORMATION_PER_INVOCATION_RIGHTMOST_COLUMN = 3;
    private static final int MONITORING_INFORMATION_PER_FRAMEWORK_COLUMN = 5;

    private final SpreadsheetWriter spreadsheetWriter;

    public void view(OutputParameters outputParameters, MonitoringInformation monitoringInformation, int row) {
        writePerInvocationColumns(outputParameters, monitoringInformation, row);
        writePerFrameworkColumns(outputParameters, monitoringInformation, row);
    }

    private void writePerInvocationColumns(OutputParameters outputParameters,
                                           MonitoringInformation monitoringInformation,
                                           int row) {
        int firstUnwrittenColumn = spreadsheetWriter.getFirstUnwrittenColumn(outputParameters, row);
        Stream<Position> perInvocationPositionStream = IntStream.iterate(firstUnwrittenColumn, i -> i + 1)
                .boxed()
                .takeWhile(columnIndex -> columnIndex <= MONITORING_INFORMATION_PER_INVOCATION_RIGHTMOST_COLUMN)
                .map(column -> Position.builder().row(row).column(column).build());
        Stream<String> perInvocationEnumerationNamesStream = Arrays.stream(MonitoringInformationPerInvocationColumnNames.values()).map(Enum::name);
        writeToPositionsStream(perInvocationEnumerationNamesStream,
                outputParameters,
                monitoringInformation,
                perInvocationPositionStream);
    }

    private void writeToPositionsStream(Stream<String> enumerationValuesStream,
                                        OutputParameters outputParameters,
                                        MonitoringInformation monitoringInformation,
                                        Stream<Position> positionStream) {
        Stream<String> columnValuesStream = enumerationValuesStream
                .map(Objects::toString)
                .map(monitoringInformation.getResult()::get);
        Streams.forEachPair(columnValuesStream,
                positionStream,
                (columnValue, position) -> spreadsheetWriter.write(outputParameters, position, columnValue));
    }

    private void writePerFrameworkColumns(OutputParameters outputParameters,
                                          MonitoringInformation monitoringInformation,
                                          int row) {
        Stream<Position> perFrameworkPositionStream = IntStream.iterate(MONITORING_INFORMATION_PER_FRAMEWORK_COLUMN, i -> i + 1)
                .boxed()
                .map(column -> Position.builder().row(row).column(column).build());
        Stream<String> perFrameworkEnumerationNamesStream = Arrays.stream(MonitoringInformationPerFrameworkColumnNames.values()).map(Enum::name);
        writeToPositionsStream(perFrameworkEnumerationNamesStream,
                outputParameters,
                monitoringInformation,
                perFrameworkPositionStream);
    }

    @RequiredArgsConstructor
    @ToString
    private enum MonitoringInformationPerInvocationColumnNames {
        invocationTimeInNanoseconds,
    }

    @RequiredArgsConstructor
    @ToString
    private enum MonitoringInformationPerFrameworkColumnNames {
        distributedFileSystemSizeWithReplication,
        distributedFileSystemSizeWithoutReplication
    }
}
