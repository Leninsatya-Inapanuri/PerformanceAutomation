package serverHealthReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * 
 * @author Leninkumar.Inapanuri
 *
 */
/*
 * Constructor : requirements : csv File Path. Methods available :
 * getValues(String columnNameForSorting,String columnNameOfValues) returns
 * HashMap<String,Integer> printCSVData() : prints the data of CSV File.
 * getCSVData() : returns ArrayList<CSVRow> getRowsSize() : returns the number
 * of lines/rows in csv. getRow(int RowIndex) : returns the CSVRow object of
 * RowIndex'th row in csv file.
 * 
 */
public class CSVTable {

	private ArrayList<String> headers = new ArrayList<String>();
	private ArrayList<CSVRow> csvData = new ArrayList<CSVRow>();
	private Scanner csvFile;

	/**
	 * 
	 * @param csvFileLocation
	 * @throws FileNotFoundException
	 */
	public CSVTable(String csvFileLocation) throws FileNotFoundException {
		File csvFileObject = new File(csvFileLocation);
		setCsvFile(new Scanner(csvFileObject));
		ArrayList<String> readHeaders = new ArrayList<String>();
		for (String s : csvFile.nextLine().toString().split(",")) {
			readHeaders.add(s);
		}
		setHeaders((ArrayList<String>) readHeaders);
//		 Iterating over all available rows in csv file 
		while (getCsvFile().hasNextLine()) {
			CSVRow currentRow = new CSVRow();
			int index = 0;
			List<String> currentCsvLine = Arrays.asList(csvFile.nextLine().split(","));
//			 Adding each value to the row 
			for (String key : getHeaders()) {
				currentRow.addValue(key, currentCsvLine.get(index));
				index++;
			}
//			 Adding current row to the csv table 
			csvData.add(currentRow);
		}
		getCsvFile().close();
	}

	/**
	 * Prints the csv data
	 */
	public void printCSVData() {

		for (CSVRow currentRow : getCsvData()) {
			HashMap<String, String> s = currentRow.getRowData();
			for (String key : s.keySet()) {
				System.out.println(key + "->" + s.get(key));
			}
			System.out.println("");
		}
	}

	/**
	 * 
	 * @return numberOfRows in CSV File excluding the headers row.
	 */
	public int getRowsSize() {
		return getCsvData().size();
	}

	/**
	 * 
	 * @param rowIndex
	 * @return corresponding row of rowIndex parameter.
	 */
	public CSVRow getRow(int rowIndex) {
		return getCsvData().get(rowIndex);
	}

	/**
	 * 
	 * @param nameColumn
	 * @param valueColumn
	 * @return AverageValuesOf valueColumn column based on nameColumn column.
	 */
	public HashMap<String, Integer> getAverageValues(String nameColumn, String valueColumn) {

		HashMap<String, Integer> averageValues = new HashMap<String, Integer>();
		HashMap<String, List<Integer>> labelValues = new HashMap<String, List<Integer>>();
		for (String labelName : getUniqueValuesOfColumn(nameColumn)) {
			labelValues.put(labelName, new ArrayList<Integer>());
		}
		for (CSVRow currentRow : getCsvData()) {
			labelValues.get(currentRow.getValue(nameColumn)).add(currentRow.getIntegerValue(valueColumn));
		}
		for (String labelName : labelValues.keySet()) {
			int valueSum = 0;
			for (Integer value : labelValues.get(labelName)) {
				valueSum += value;
			}
			int averageValue = valueSum / labelValues.get(labelName).size();
			averageValues.put(labelName, averageValue);
		}
		return averageValues;
	}

	/**
	 * 
	 * @param labelColumnName
	 * @param valueColumnName
	 * @param numberOfRowsRequired
	 * @return listedValues
	 */
	public HashMap<String, List<String>> getMinimizedvalues(String labelColumnName, String valueColumnName,
			int numberOfRowsRequired) {
		HashMap<String, List<CSVRow>> minimizedRows = getMinimizedRows(labelColumnName, numberOfRowsRequired);
		HashMap<String, List<String>> listedValues = new HashMap<String, List<String>>();

		for (String currentLabelName : getUniqueValuesOfColumn(labelColumnName)) {
			ArrayList<String> currentValues = new ArrayList<String>();
			for (CSVRow currentRow : minimizedRows.get(currentLabelName)) {
				currentValues.add(currentRow.getValue(valueColumnName));
			}
			listedValues.put(currentLabelName, currentValues);
			System.out.println(currentLabelName + " -> " + currentValues.size());
		}
//		System.out.println(listedValues);
		return listedValues;
	}

	/**
	 * 
	 * @param labelColumnName
	 * @param numberOfRowsRequired
	 * @return minimizedRows
	 */
	public HashMap<String, List<CSVRow>> getMinimizedRows(String labelColumnName, int numberOfRowsRequired) {
		HashMap<String, List<CSVRow>> minimizedRows = new HashMap<String, List<CSVRow>>();
		for (String currentLabelName : getUniqueValuesOfColumn(labelColumnName)) {
			ArrayList<CSVRow> matchedRows = getMatchingRowsForAColumnValue(labelColumnName, currentLabelName);
			int numberOfRowsAvailable = matchedRows.size();
			if (numberOfRowsAvailable < numberOfRowsRequired) {
				minimizedRows.put(currentLabelName, matchedRows);
			} else {
				Double difference = ((double) numberOfRowsAvailable) / numberOfRowsRequired;
				ArrayList<CSVRow> listOfRows = new ArrayList<CSVRow>();
				for (int entryCount = 0; entryCount < numberOfRowsRequired; entryCount++) {
					int rowNumber = (int) (difference * entryCount);
					listOfRows.add(matchedRows.get(rowNumber));
				}
				minimizedRows.put(currentLabelName, listOfRows);
			}
		}
		return minimizedRows;
	}

	/**
	 * 
	 * @param nameColumn
	 * @param value
	 * @return MatchingRowsOfAValueForAColumn
	 */
	public ArrayList<CSVRow> getMatchingRowsForAColumnValue(String nameColumn, String value) {
		ArrayList<CSVRow> rowsMatched = new ArrayList<CSVRow>();
		for (CSVRow currentRow : getCsvData()) {
			if (currentRow.getValue(nameColumn).equals(value)) {
				rowsMatched.add(currentRow);
			}
		}
		return rowsMatched;
	}

	/**
	 * 
	 * @param columnName
	 * @return uniqueValuesOfAnyColumn
	 */
	public List<String> getUniqueValuesOfColumn(String columnName) {
		ArrayList<String> columnValues = new ArrayList<String>();
		for (CSVRow currentRow : getCsvData()) {
			String currentRowValue = currentRow.getValue(columnName);
			if (!(columnValues.contains(currentRowValue))) {
				columnValues.add(currentRowValue);
			}
		}
//		System.out.println("Unique column Values : " + columnValues.toString());
		return columnValues;
	}

	/**
	 * 
	 * @param columnName
	 * @return allValuesOfAnyColumn
	 */
	public List<String> getValuesOfColumn(String columnName) {
		ArrayList<String> columnValues = new ArrayList<String>();
		for (CSVRow currentRow : getCsvData()) {
			columnValues.add(currentRow.getValue(columnName));
		}
		return columnValues;
	}

	/**
	 * @return the headers
	 */
	public ArrayList<String> getHeaders() {
		return headers;
	}

	/**
	 * @param headers the headers to set
	 */
	private void setHeaders(ArrayList<String> headers) {
		this.headers = headers;
	}

	/**
	 * @return the csvFile
	 */
	private Scanner getCsvFile() {
		return csvFile;
	}

	/**
	 * @param csvFile the csvFile to set
	 */
	private void setCsvFile(Scanner csvFile) {
		this.csvFile = csvFile;
	}

	/**
	 * @return the csvTable
	 */
	public ArrayList<CSVRow> getCsvData() {
		return csvData;
	}

	/**
	 * @param csvTable the csvTable to set
	 */
	public void setCsvData(ArrayList<CSVRow> csvTable) {
		this.csvData = csvTable;
	}
}
