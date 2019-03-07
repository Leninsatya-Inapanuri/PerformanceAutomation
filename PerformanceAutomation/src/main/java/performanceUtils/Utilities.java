package performanceUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {

	Utilities() {

	}

	/**
	 * 
	 * @param stringTimeStamp
	 * @param format
	 * @return date in requiredFormat format
	 */
	public static String getDateFromTimeStamp(String stringTimeStamp, String requiredFormat) {
		return getDateFromTimeStamp(Long.parseLong(stringTimeStamp), requiredFormat);
	}

	/**
	 * 
	 * @param timeStamp
	 * @param requiredFormat
	 * @return date in requiredFormat format
	 */
	public static String getDateFromTimeStamp(long timeStamp, String requiredFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat(requiredFormat);
		Date date = new Date(timeStamp);
		return formatter.format(date);
	}

	/**
	 * 
	 * @return currentDateTime
	 */
	public static String getCurrentDateAndTime() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(AutomationConstants.defaultDateTimeFormat);
		return formatter.format(date);
	}
}
