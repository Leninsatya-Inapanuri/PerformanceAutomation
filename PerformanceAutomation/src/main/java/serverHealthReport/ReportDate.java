package serverHealthReport;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportDate {

	public static Timestamp timeStamp;
	public static Date date;
	public static DateFormat dateFormat;

	public String formatDate(String currentTimeStamp) {
		return formatDate(currentTimeStamp, "ddMMMyyyy");
	}

	public String formatDate(String currentTimeStamp, String format) {
		if (format.equals(""))
			format = "ddMMMyyyy";
		date = new Date(Long.parseLong(currentTimeStamp));
		dateFormat = new SimpleDateFormat(format);
		System.out.println(dateFormat.format(date));
		return dateFormat.format(date);
	}

}
