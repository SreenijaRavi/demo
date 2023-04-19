package com.akatec.aps.reusablefunctions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

	private static XSSFSheet ExcelWSheet;
	private static XSSFWorkbook ExcelWBook;
	private static XSSFCell Cell;
	private static FileInputStream ExcelFileInput;
	private static FileOutputStream ExcelFileOutput;
	private static String FilePath;
	public static HashMap<String, String> dataMap = new HashMap<String, String>();

	public static void setExcelFile(String Path, String SheetName) throws Exception {
		try {
			// Open the Excel file
			ExcelFileInput = new FileInputStream(System.getProperty("user.dir") + Path);
			FilePath = System.getProperty("user.dir") + Path;
			// Access the required test data sheet
			ExcelWBook = new XSSFWorkbook(ExcelFileInput);
			ExcelWSheet = ExcelWBook.getSheet(SheetName);
		} catch (Exception e) {
			throw (e);
		}
	}

	public static String getTestCaseName(String sTestCase) throws Exception {
		String value = sTestCase;
		try {
			int posi = value.indexOf("@");
			value = value.substring(0, posi);
			posi = value.lastIndexOf(".");
			value = value.substring(posi + 1);
			return value;
		} catch (Exception e) {
			throw (e);
		}
	}

	public static int getRowUsed() throws Exception {
		try {
			int RowCount = ExcelWSheet.getLastRowNum();
			System.out.println(RowCount);
			return RowCount + 1;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw (e);
		}
	}

	public static String getDataBasedOnTestCase(String sTestCaseName, String colName) throws Exception {
		int i;
		int j;
		try {
			int rowCount = getRowUsed();
			int noOfColumns = ExcelWSheet.getRow(0).getPhysicalNumberOfCells();
			for (i = 0; i <= rowCount; i++) {
				if (((String) ExcelUtils.getCellData(i, 0)).equalsIgnoreCase(sTestCaseName)) {
					break;
				}
			}
			int rowvalue = i;
			for (j = 0; j <= noOfColumns; j++) {
				if (((String) ExcelUtils.getCellData(0, j)).equalsIgnoreCase(colName)) {
					break;
				}
			}
			int colvalue = j;
			String cellData;
			if (ExcelUtils.getCellData(rowvalue, colvalue) == null) {
				cellData = "";
			} else
				cellData = ExcelUtils.getCellData(rowvalue, colvalue);
			return cellData;
		} catch (Exception e) {
			throw (e);
		}
	}

	public static void getDataBasedOnTestCaseNew(String sTestCaseName) throws Exception {
		int i;
		int j;
		try {
			int rowCount = getRowUsed();
			int noOfColumns = ExcelWSheet.getRow(0).getPhysicalNumberOfCells();
			for (i = 0; i <= rowCount; i++) {
				if (((String) ExcelUtils.getCellData(i, 0)).equalsIgnoreCase(sTestCaseName)) {
					break;
				}
			}
			int rowvalue = i;
			for (j = 1; j < noOfColumns; j++) {
				dataMap.put(ExcelUtils.getCellData(0, j), ExcelUtils.getCellData(rowvalue, j));
			}
			System.out.println("map is " + dataMap);
			System.out.println("map is " + dataMap.size());
		} catch (Exception e) {
			throw (e);
		}
	}

	public static String[][] getTableArray(String FilePath, String SheetName, int iTestCaseRow) throws Exception {
		String[][] tabArray = null;
		try {
			FileInputStream ExcelFile = new FileInputStream(System.getProperty("user.dir") + FilePath);
			// Access the required test data sheet
			ExcelWBook = new XSSFWorkbook(ExcelFile);
			ExcelWSheet = ExcelWBook.getSheet(SheetName);
			int startCol = 1;
			int startRow = 1;
			int ci = 0, cj = 0;
			int totalRows = ExcelWSheet.getLastRowNum();
			int totalCols = 16;
			tabArray = new String[totalRows][totalCols];
			ci = 0;
			for (int i = startRow; i <= totalRows; i++, ci++) {
				cj = 0;
				for (int j = startCol; j <= totalCols; j++, cj++) {
					tabArray[ci][cj] = getCellData(i, j);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Could not read the Excel sheet");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Could not read the Excel sheet");
			e.printStackTrace();
		}
		return (tabArray);
	}

	public static String getCellData(int RowNum, int ColNum) throws Exception {
		try {
			Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

			System.out.println();
			if (Cell == null) {
				return "";

			} else {

				DataFormatter df = new DataFormatter();
				String cellData = df.formatCellValue(Cell);
				// switch (type) {
				// case STRING:
				// return Cell.getStringCellValue();
				// // case BOOLEAN:
				// // System.out.println("Boolean");
				// // return Cell.getBooleanCellValue();
				// //
				//
				// // case NUMERIC:
				// // return Cell.getNumericCellValue();
				// case BLANK:
				// break;
				// case ERROR:
				// break;
				// case FORMULA:
				// break;
				// case _NONE:
				// break;
				//
				// default:
				// break;
				// }
				return cellData;

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw (e);
		}
	}

	/**
	 * @author spathak Function is to Write data in TestData sheet within a column
	 *         for a specific test-case
	 *
	 */
	public static void UpdateDataBasedOnTestCase(String sTestCaseName, String colName, String Data) throws Exception {
		int i;
		int j;
		try {
			int rowCount = getRowUsed();
			int noOfColumns = ExcelWSheet.getRow(0).getPhysicalNumberOfCells();
			for (i = 0; i <= rowCount; i++) {
				if (((String) ExcelUtils.getCellData(i, 0)).equalsIgnoreCase(sTestCaseName)) {
					break;
				}
			}
			int rowvalue = i;
			for (j = 0; j <= noOfColumns; j++) {
				if (((String) ExcelUtils.getCellData(0, j)).equalsIgnoreCase(colName)) {
					break;
				}
			}
			int colvalue = j;
			Cell = null;
			Cell = ExcelWSheet.getRow(rowvalue).createCell(colvalue);

			Cell.setCellValue(Data);
			ExcelFileOutput = new FileOutputStream(new File(FilePath));
			ExcelWBook.write(ExcelFileOutput);
			ExcelFileOutput.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
