package com.testbed.boundary.writers;

import com.testbed.entities.parameters.OutputParameters;
import lombok.RequiredArgsConstructor;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class XLSXSpreadsheetWriter implements SpreadsheetWriter {
    private static final int FIRST_ROW = 0;

    private final FileSystem fileSystem;

    @Override
    public void addHeaders(OutputParameters outputParameters, List<String> headers, String colorName) {
        Workbook workbook = tryGetWorkbook(outputParameters);
        Sheet sheet = getOrCreateSheet(outputParameters, workbook);
        Row row = getOrCreateRow(sheet, FIRST_ROW);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.valueOf(colorName).getIndex());
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        for (int column = 0; column < headers.size(); ++column) {
            Cell cell = row.createCell(column);
            cell.setCellValue(headers.get(column));
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(column);
        }
        tryWriteWorkbook(outputParameters, workbook);
    }

    @Override
    public void addFormula(OutputParameters outputParameters, Position position, String formula) {
        Workbook workbook = tryGetWorkbook(outputParameters);
        Sheet sheet = getOrCreateSheet(outputParameters, workbook);
        Row row = getOrCreateRow(sheet, position.getRow());
        Cell cell = row.createCell(position.getColumn());
        cell.setCellFormula(formula);
        sheet.autoSizeColumn(position.getColumn());
        tryWriteWorkbook(outputParameters, workbook);
    }

    @Override
    public void write(OutputParameters outputParameters, Position position, String value) {
        Workbook workbook = tryGetWorkbook(outputParameters);
        Sheet sheet = getOrCreateSheet(outputParameters, workbook);
        Row row = getOrCreateRow(sheet, position.getRow());
        Cell cell = row.createCell(position.getColumn());
        cell.setCellValue(value);
        sheet.autoSizeColumn(position.getColumn());
        tryWriteWorkbook(outputParameters, workbook);
    }

    @Override
    public boolean isEmpty(OutputParameters outputParameters, Position position) {
        Workbook workbook = tryGetWorkbook(outputParameters);
        Sheet sheet = getOrCreateSheet(outputParameters, workbook);
        Row row = getOrCreateRow(sheet, position.getRow());
        Cell cell = row.getCell(position.getColumn());
        return cell.getStringCellValue().isEmpty();
    }

    @Override
    public int getFirstUnwrittenRow(OutputParameters outputParameters, int column) {
        Stream<Integer> integerStream = IntStream.iterate(0, i -> i + 1).boxed();
        Stream<Position> positionStream = integerStream.map(i -> Position.builder().row(i).column(column).build());
        return getFirstUnwrittenPosition(outputParameters, positionStream).getRow();
    }

    @Override
    public int getFirstUnwrittenColumn(OutputParameters outputParameters, int row) {
        Stream<Integer> integerStream = IntStream.iterate(0, i -> i + 1).boxed();
        Stream<Position> positionStream = integerStream.map(i -> Position.builder().row(row).column(i).build());
        return getFirstUnwrittenPosition(outputParameters, positionStream).getColumn();
    }

    private Sheet getOrCreateSheet(OutputParameters outputParameters, Workbook workbook) {
        Sheet sheet = workbook.getSheet(outputParameters.getSheetName());
        if (sheet == null) {
            return workbook.createSheet(outputParameters.getSheetName());
        }
        return sheet;
    }

    private Row getOrCreateRow(Sheet sheet, int position) {
        Row row = sheet.getRow(position);
        if (row == null) {
            return sheet.createRow(position);
        }
        return row;
    }

    private Workbook tryGetWorkbook(OutputParameters outputParameters) {
        try {
            FSDataInputStream inputStream = fileSystem.open(new Path(outputParameters.getOutputPath()));
            return new HSSFWorkbook(inputStream);
        } catch (FileNotFoundException fileNotFoundException) {
            return new HSSFWorkbook();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void tryWriteWorkbook(OutputParameters outputParameters, Workbook workbook) {
        try {
            writeWorkbook(outputParameters, workbook);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void writeWorkbook(OutputParameters outputParameters, Workbook workbook) throws IOException {
        FSDataOutputStream outputStream = fileSystem.create(new Path(outputParameters.getOutputPath()));
        workbook.write(outputStream);
        outputStream.close();
    }

    public Position getFirstUnwrittenPosition(OutputParameters outputParameters, Stream<Position> positionStream) {
        return positionStream.dropWhile(position -> !isEmpty(outputParameters, position)).findFirst().get();
    }
}
