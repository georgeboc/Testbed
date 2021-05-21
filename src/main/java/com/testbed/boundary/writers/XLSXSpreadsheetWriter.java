package com.testbed.boundary.writers;

import com.testbed.entities.parameters.OutputParameters;
import lombok.RequiredArgsConstructor;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
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
    private static final String DEFAULT_FONT_NAME = "Verdana";
    private static final short DEFAULT_FONT_HEIGHT = 10;
    private static final short DEFAULT_FONT_COLOR_INDEX = IndexedColors.BLACK.getIndex();


    private final FileSystem fileSystem;

    @Override
    public void write(final OutputParameters outputParameters, final Position position, final String value) {
        Workbook workbook = tryGetWorkbook(outputParameters);
        Sheet sheet = getOrCreateSheet(outputParameters, workbook);
        Row row = getOrCreateRow(sheet, position.getRow());
        Cell cell = CellUtil.createCell(row, position.getColumn(), value);
        setDefaultFont(workbook, cell);
        sheet.autoSizeColumn(position.getColumn());
        tryWriteWorkbook(outputParameters, workbook);
    }

    @Override
    public void writeWithColor(final OutputParameters outputParameters,
                               final Position position,
                               final String value,
                               final String colorName) {
        Workbook workbook = tryGetWorkbook(outputParameters);
        Sheet sheet = getOrCreateSheet(outputParameters, workbook);
        Row row = getOrCreateRow(sheet, position.getRow());
        Cell cell = CellUtil.createCell(row, position.getColumn(), value);
        CellUtil.setCellStyleProperty(cell, workbook, CellUtil.FILL_PATTERN, CellStyle.SOLID_FOREGROUND);
        CellUtil.setCellStyleProperty(cell, workbook, CellUtil.FILL_FOREGROUND_COLOR, IndexedColors.valueOf(colorName).getIndex());
        setDefaultFont(workbook, cell);
        sheet.autoSizeColumn(position.getColumn());
        tryWriteWorkbook(outputParameters, workbook);
    }

    @Override
    public void addFormula(final OutputParameters outputParameters, final Position position, final String formula) {
        Workbook workbook = tryGetWorkbook(outputParameters);
        Sheet sheet = getOrCreateSheet(outputParameters, workbook);
        Row row = getOrCreateRow(sheet, position.getRow());
        Cell cell = CellUtil.createCell(row, position.getColumn(), EMPTY);
        cell.setCellFormula(formula);
        HSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
        setDefaultFont(workbook, cell);
        sheet.autoSizeColumn(position.getColumn(), USE_UNMERGED_CELLS);
        tryWriteWorkbook(outputParameters, workbook);
    }

    @Override
    public boolean isEmpty(final OutputParameters outputParameters, final Position position) {
        Workbook workbook = tryGetWorkbook(outputParameters);
        Sheet sheet = getOrCreateSheet(outputParameters, workbook);
        Row row = getOrCreateRow(sheet, position.getRow());
        Cell cell = row.getCell(position.getColumn());
        return cell == null;
    }

    @Override
    public int getFirstUnwrittenColumn(final OutputParameters outputParameters, final int row, final int columnOffset) {
        int i = columnOffset;
        while (!isEmpty(outputParameters, Position.builder().row(row).column(i).build())) {
            ++i;
        }
        return i;
    }

    @Override
    public void makeMergedRegion(final OutputParameters outputParameters, final Position startPosition, final Position endPosition) {
        Workbook workbook = tryGetWorkbook(outputParameters);
        Sheet sheet = getOrCreateSheet(outputParameters, workbook);
        sheet.addMergedRegion(new CellRangeAddress(startPosition.getRow(),
                endPosition.getRow(),
                startPosition.getColumn(),
                endPosition.getColumn()));
        Row row = sheet.getRow(startPosition.getRow());
        Cell cell = CellUtil.getCell(row, startPosition.getColumn());
        CellUtil.setCellStyleProperty(cell, workbook, CellUtil.ALIGNMENT, CellStyle.ALIGN_CENTER);
        setDefaultFont(workbook, cell);
        tryWriteWorkbook(outputParameters, workbook);
    }

    @Override
    public void removeSheet(final OutputParameters outputParameters) {
        Workbook workbook = tryGetWorkbook(outputParameters);
        Optional<Sheet> optionalSheet = Optional.ofNullable(workbook.getSheet(outputParameters.getSheetName()));
        optionalSheet.ifPresent(sheet -> workbook.removeSheetAt(workbook.getSheetIndex(sheet)));
        tryWriteWorkbook(outputParameters, workbook);
    }

    private void setDefaultFont(final Workbook workbook, final Cell cell) {
        Font font = workbook.getFontAt(cell.getCellStyle().getFontIndex());
        font.setFontName(DEFAULT_FONT_NAME);
        font.setFontHeightInPoints(DEFAULT_FONT_HEIGHT);
        font.setColor(DEFAULT_FONT_COLOR_INDEX);
        CellUtil.setFont(cell, workbook, font);
    }

    private Sheet getOrCreateSheet(final OutputParameters outputParameters, final Workbook workbook) {
        return Optional.ofNullable(workbook.getSheet(outputParameters.getSheetName()))
                .orElseGet(() -> workbook.createSheet(outputParameters.getSheetName()));
    }

    private Row getOrCreateRow(final Sheet sheet, final int position) {
        return Optional.ofNullable(sheet.getRow(position)).orElseGet(() -> sheet.createRow(position));
    }

    private Workbook tryGetWorkbook(final OutputParameters outputParameters) {
        try {
            return getWorkbook(outputParameters);
        } catch (FileNotFoundException fileNotFoundException) {
            return new XSSFWorkbook();
        } catch (IOException | InvalidFormatException exception) {
            throw new RuntimeException(exception);
        }
    }

    private Workbook getWorkbook(final OutputParameters outputParameters) throws IOException, InvalidFormatException {
        FSDataInputStream inputStream = fileSystem.open(new Path(outputParameters.getOutputPath()));
        Workbook workbook = WorkbookFactory.create(inputStream);
        inputStream.close();
        return workbook;
    }

    private void tryWriteWorkbook(final OutputParameters outputParameters, final Workbook workbook) {
        try {
            writeWorkbook(outputParameters, workbook);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void writeWorkbook(final OutputParameters outputParameters, final Workbook workbook) throws IOException {
        FSDataOutputStream outputStream = fileSystem.create(new Path(outputParameters.getOutputPath()));
        workbook.write(outputStream);
        outputStream.close();
    }
}
