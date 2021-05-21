package com.testbed.interactors.viewers;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Streams;
import com.testbed.boundary.writers.Position;
import com.testbed.boundary.writers.SpreadsheetWriter;
import com.testbed.entities.parameters.OutputParameters;
import com.testbed.interactors.monitors.MonitoringInformation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
@RequiredArgsConstructor
public class MonitoringInformationViewer {
    private static final String FORMULA_FORMAT = "IF(ISERROR(MEDIAN(VALUE(%2$s%1$s),VALUE(%3$s%1$s)," +
            "VALUE(%4$s%1$s))), \"-\", TEXT(MEDIAN(VALUE(%2$s%1$s),VALUE(%3$s%1$s),VALUE(%4$s%1$s)), \"0\"))";
    private static final int LATERAL_HEADER_COLUMN = 0;
    private static final int FIRST_MONITORING_INFORMATION_ROW = 2;
    private static final int MEDIAN_RELATIVE_COLUMN = 3;
    private static final String TOP_HEADER_COLOR_NAME = "LIME";
    private static final char UNDERSCORE = '_';
    private static final char SPACE = ' ';
    private static final int ONE_BASED = 1;

    private final SpreadsheetWriter spreadsheetWriter;

    public void view(final OutputParameters outputParameters,
                     final MonitoringInformation monitoringInformation,
                     final int firstColumn) {
        writeLateralColumnHeaders(outputParameters, monitoringInformation);
        int rowsCount = monitoringInformation.getResult().size();
        Stream<Position> columnPositionsStream = getColumnPositionStream(outputParameters, firstColumn, rowsCount);
        writeToPositionsStream(outputParameters, monitoringInformation, columnPositionsStream);
        writeMedianFormulas(outputParameters, firstColumn, rowsCount);
    }

    private Stream<Position> getColumnPositionStream(final OutputParameters outputParameters,
                                                     final int firstColumn,
                                                     final int rowsCount) {
        int firstUnwrittenColumn = spreadsheetWriter.getFirstUnwrittenColumn(outputParameters,
                FIRST_MONITORING_INFORMATION_ROW,
                firstColumn);
        if (firstUnwrittenColumn >= firstColumn + MEDIAN_RELATIVE_COLUMN) {
            return Stream.empty();
        }
        return IntStream.iterate(FIRST_MONITORING_INFORMATION_ROW, i -> i + 1)
                .boxed()
                .limit(rowsCount)
                .map(row -> Position.builder().row(row).column(firstUnwrittenColumn).build());
    }

    private void writeLateralColumnHeaders(final OutputParameters outputParameters,
                                           final MonitoringInformation monitoringInformation) {
        Stream<String> monitoringInformationHeadersStream = monitoringInformation.getResult().keySet().stream().sorted();
        Stream<Position> positionStream = IntStream.iterate(FIRST_MONITORING_INFORMATION_ROW, i -> i + 1)
                .mapToObj(row -> Position.builder().column(LATERAL_HEADER_COLUMN).row(row).build());
        Streams.forEachPair(positionStream, monitoringInformationHeadersStream,
                (position, monitoringInformationHeaders) -> spreadsheetWriter.writeWithColor(outputParameters,
                        position,
                        getPrettyHeader(monitoringInformationHeaders),
                        TOP_HEADER_COLOR_NAME));
    }

    private String getPrettyHeader(final String monitoringInformationHeader) {
        String headerWithUnderscores = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, monitoringInformationHeader);
        String headerWithSpaces = headerWithUnderscores.replace(UNDERSCORE, SPACE);
        return StringUtils.capitalize(headerWithSpaces);
    }

    private void writeToPositionsStream(final OutputParameters outputParameters,
                                        final MonitoringInformation monitoringInformation,
                                        final Stream<Position> positionStream) {
        Stream<String> columnValuesStream = monitoringInformation.getResult().entrySet().stream()
                .sorted(Map.Entry.comparingByKey(String::compareTo))
                .map(Map.Entry::getValue);
        Streams.forEachPair(columnValuesStream,
                positionStream,
                (columnValue, position) -> spreadsheetWriter.write(outputParameters, position, columnValue));
    }

    private void writeMedianFormulas(final OutputParameters outputParameters,
                                     final int firstColumn,
                                     final int rowsCount) {
        Stream<Position> positionStream = IntStream.iterate(FIRST_MONITORING_INFORMATION_ROW, i -> i + 1)
                .mapToObj(row -> Position.builder().column(firstColumn + MEDIAN_RELATIVE_COLUMN).row(row).build());
        Stream<String> formulas = IntStream.iterate(FIRST_MONITORING_INFORMATION_ROW + ONE_BASED, i -> i + 1)
                .limit(rowsCount)
                .mapToObj(row -> String.format(FORMULA_FORMAT,
                        row,
                        getLexicographicalColumn(firstColumn),
                        getLexicographicalColumn(firstColumn + 1),
                        getLexicographicalColumn(firstColumn + 2)));
        Streams.forEachPair(positionStream,
                formulas,
                (position, formula) -> spreadsheetWriter.addFormula(outputParameters, position, formula));
    }

    private char getLexicographicalColumn(final int column) {
        return (char) ('A' + column);
    }
}
