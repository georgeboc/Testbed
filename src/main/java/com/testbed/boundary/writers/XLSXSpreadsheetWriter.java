package com.testbed.boundary.writers;

import com.testbed.entities.parameters.OutputParameters;
import lombok.RequiredArgsConstructor;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class XLSXSpreadsheetWriter implements SpreadsheetWriter {
    private static final String EMPTY = "";
    private static final boolean USE_UNMERGED_CELLS = true;

    private final FileSystem fileSystem;

    @Override
    public void write(OutputParameters outputParameters, Position position, String value) {
        Workbook workbook = tryGetWorkbook(outputParameters);
        Sheet sheet = getOrCreateSheet(outputParameters, workbook);
        Row row = getOrCreateRow(sheet, position.getRow());
        CellUtil.createCell(row, position.getColumn(), value);
        sheet.autoSizeColumn(position.getColumn());
        tryWriteWorkbook(outputParameters, workbook);
    }

    @Override
    public void writeWithColor(OutputParameters outputParameters, Position position, String value, String colorName) {
        Workbook workbook = tryGetWorkbook(outputParameters);
        Sheet sheet = getOrCreateSheet(outputParameters, workbook);
        Row row = getOrCreateRow(sheet, position.getRow());
        Cell cell = CellUtil.createCell(row, position.getColumn(), value);
        CellUtil.setCellStyleProperty(cell, workbook, CellUtil.FILL_PATTERN, CellStyle.SOLID_FOREGROUND);
        CellUtil.setCellStyleProperty(cell, workbook, CellUtil.FILL_FOREGROUND_COLOR, IndexedColors.valueOf(colorName).getIndex());
        sheet.autoSizeColumn(position.getColumn());
        tryWriteWorkbook(outputParameters, workbook);
    }

    @Override
    public void addFormula(OutputParameters outputParameters, Position position, String formula) {
        Workbook workbook = tryGetWorkbook(outputParameters);
        Sheet sheet = getOrCreateSheet(outputParameters, workbook);
        Row row = getOrCreateRow(sheet, position.getRow());
        Cell cell = CellUtil.createCell(row, position.getColumn(), EMPTY);
        cell.setCellFormula(formula);
        HSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
        sheet.autoSizeColumn(position.getColumn(), USE_UNMERGED_CELLS);
        tryWriteWorkbook(outputParameters, workbook);
    }

    @Override
    public boolean isEmpty(OutputParameters outputParameters, Position position) {
        Workbook workbook = tryGetWorkbook(outputParameters);
        Sheet sheet = getOrCreateSheet(outputParameters, workbook);
        Row row = getOrCreateRow(sheet, position.getRow());
        Cell cell = row.getCell(position.getColumn());
        return cell == null;
    }

    @Override
    public int getFirstUnwrittenColumn(OutputParameters outputParameters, int row, int columnOffset) {
        int i = columnOffset;
        while (!isEmpty(outputParameters, Position.builder().row(row).column(i).build())) {
            ++i;
        }
        return i;
    }

    @Override
    public void makeMergedRegion(OutputParameters outputParameters, Position startPosition, Position endPosition) {
        Workbook workbook = tryGetWorkbook(outputParameters);
        Sheet sheet = getOrCreateSheet(outputParameters, workbook);
        sheet.addMergedRegion(new CellRangeAddress(startPosition.getRow(),
                endPosition.getRow(),
                startPosition.getColumn(),
                endPosition.getColumn()));
        Row row = sheet.getRow(startPosition.getRow());
        Cell cell = CellUtil.getCell(row, startPosition.getColumn());
        CellUtil.setCellStyleProperty(cell, workbook, CellUtil.ALIGNMENT, CellStyle.ALIGN_CENTER);
        tryWriteWorkbook(outputParameters, workbook);
    }

    @Override
    public void removeSheet(OutputParameters outputParameters) {
        Workbook workbook = tryGetWorkbook(outputParameters);
        Optional<Sheet> optionalSheet = Optional.ofNullable(workbook.getSheet(outputParameters.getSheetName()));
        optionalSheet.ifPresent(sheet -> workbook.removeSheetAt(workbook.getSheetIndex(sheet)));
        tryWriteWorkbook(outputParameters, workbook);
    }

    private Sheet getOrCreateSheet(OutputParameters outputParameters, Workbook workbook) {
        return Optional.ofNullable(workbook.getSheet(outputParameters.getSheetName()))
                .orElseGet(() -> workbook.createSheet(outputParameters.getSheetName()));
    }

    private Row getOrCreateRow(Sheet sheet, int position) {
        return Optional.ofNullable(sheet.getRow(position)).orElseGet(() -> sheet.createRow(position));
    }

    private Workbook tryGetWorkbook(OutputParameters outputParameters) {
        try {
            return getWorkbook(outputParameters);
        } catch (FileNotFoundException fileNotFoundException) {
            return new XSSFWorkbook();
        } catch (IOException | InvalidFormatException exception) {
            throw new RuntimeException(exception);
        }
    }

    private Workbook getWorkbook(OutputParameters outputParameters) throws IOException, InvalidFormatException {
        FSDataInputStream inputStream = fileSystem.open(new Path(outputParameters.getOutputPath()));
        Workbook workbook = WorkbookFactory.create(inputStream);
        inputStream.close();
        return workbook;
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
}
