package serverHealthReport;

import java.util.HashMap;
import java.util.List;

public class CSVRow {

	private HashMap<String, String> rowData = new HashMap<String, String>();

	public void addAll(List<String> asList) {
	}

	public String getValue(String key) {
		if (rowData.containsKey(key))
			return getRowData().get(key);
		else
			return "";
	}

	public int getIntegerValue(String key) {
		if (rowData.containsKey(key))
			return Integer.parseInt(getRowData().get(key));
		else
			return 0;
	}

	public void addValue(String key, String value) {
		rowData.put(key, value);
	}

	/**
	 * @return the rowData
	 */
	public HashMap<String, String> getRowData() {
		return rowData;
	}

	/**
	 * @param rowData the rowData to set
	 */
	public void setRowData(HashMap<String, String> rowData) {
		this.rowData = rowData;
	}

}
