package performanceUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class TestContext {

	private HashMap<String, String> values = new HashMap<String, String>();

	public TestContext() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(AutomationConstants.defaultDateTimeFormat);
		values.put(AutomationConstants.currentDateTime, formatter.format(date));
	}

	public void put(String key, String value) {
		values.put(key, value);
	}

	public String get(String key) {
		return values.get(key);
	}

	public HashMap<String, String> getValues() {
		return values;
	}
}
