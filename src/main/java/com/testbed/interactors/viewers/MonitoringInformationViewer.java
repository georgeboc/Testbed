package com.testbed.interactors.viewers;

import com.google.common.collect.Streams;
import com.testbed.boundary.writers.Position;
import com.testbed.boundary.writers.SpreadsheetWriter;
import com.testbed.entities.parameters.OutputParameters;
import com.testbed.interactors.monitors.MonitoringInformation;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
@RequiredArgsConstructor
public class MonitoringInformationViewer {
    private static final int MONITORING_INFORMATION_RIGHTMOST_COLUMN = 3;

    private final SpreadsheetWriter spreadsheetWriter;

    public void view(OutputParameters outputParameters, MonitoringInformation monitoringInformation, int row) {
        int firstUnwrittenColumn = spreadsheetWriter.getFirstUnwrittenColumn(outputParameters, row);
        Map<String, String> monitoringInformationResult =  monitoringInformation.getResult();
        Stream<Position> positionStream = IntStream.iterate(firstUnwrittenColumn, i -> i + 1).boxed()
                .takeWhile(columnIndex -> columnIndex <= MONITORING_INFORMATION_RIGHTMOST_COLUMN)
                .map(column -> Position.builder().row(row).column(column).build());
        Stream<String> columnValuesStream = Arrays.stream(MonitoringInformationColumnNames.values()).sequential()
                .map(Enum::name)
                .map(Objects::toString)
                .map(monitoringInformationResult::get);
        Streams.forEachPair(columnValuesStream,
                positionStream,
                (columnValue, position) -> spreadsheetWriter.write(outputParameters, position, columnValue));
    }

    @RequiredArgsConstructor
    @ToString
    private enum MonitoringInformationColumnNames {
        invocationTimeInNanoseconds
    }
}
