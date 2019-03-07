package serverHealthReport;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/*
 * Structure of Execution.
 * 1.Reading Properties which are specified in properties file.
 * 2.Creating an object for Template Writer.
 * 3.Storing Metrics Data in metricsData Object.
 * 4.Preparing Chart Data Series for javascript file.
 * 5.Preparing required Template Parameters.
 * 6.Creating Files/Templates and writing data to Files/templates.
 */

public class HealthChart {
	private static CSVReader metricsFile;
	private Timestamp timeStamp;
	HashMap<String, String> htmlFileParameters = new HashMap<String, String>();
	PropertiesReader properties = new PropertiesReader();
	private String serverHealthSeriesData = "";

	HealthChart() throws IOException {
		metricsFile = new CSVReader(properties.getProperty(PropertyNames.metricsFileLocation.toString()));
	}

	public void prepareChartDataSeries() throws Exception {
		TemplateWriter templateWriter = new TemplateWriter();
		HashMap<String, ArrayList<MetricDataNode>> metricsData = metricsFile.getMetricData();

//		Preparing chart data series
		for (String metricLabel : metricsFile.getMetricLabels()) {
			double metricDivider = getMetricDivider(metricLabel);
			String seriesStartString = "{\"data\": [";
			String seriesEndString = "], \"isOverall\": false, \"label\": \"" + metricLabel + " ( "
					+ getMetricUnit(metricLabel) + " ) " + "\", \"isController\": false,\"metricUnit\":\""
					+ getMetricUnit(metricLabel) + "\"},";

			serverHealthSeriesData += seriesStartString;

			ArrayList<MetricDataNode> currentMetricData = metricsData.get(metricLabel);
			for (MetricDataNode metricDataNode : currentMetricData) {
				serverHealthSeriesData += getCurrentCoordinateData(
						metricDataNode.getMetric(Constants.metricKeys.metricTimeStamp.name()),
						metricDataNode.getMetric(Constants.metricKeys.MetricValue.name()), metricDivider);
			}
			serverHealthSeriesData += seriesEndString;
		}
//		adding required parameters for template creation.
		prepareTemplateParameters();

//		Preparing HTML File for chart.
		System.out.println();
		File htmlFile = new File(properties.getProperty(PropertyNames.dashboardRootDirectory.toString())
				+ "content\\pages\\serverHealthChart.html");
		htmlFile.createNewFile();
		FileWriter fileWriter = new FileWriter(htmlFile, false);
		fileWriter.write(templateWriter.getTemplate(
				properties.getProperty(PropertyNames.healthChartVelocityFile.toString()), htmlFileParameters));
		fileWriter.close();

//		Preparing Javascript file for chart.
		File javaScriptFile = new File(properties.getProperty(PropertyNames.dashboardRootDirectory.toString())
				+ "content\\js\\graph_serverHealth.js");
		javaScriptFile.createNewFile();
		fileWriter = new FileWriter(javaScriptFile);

		String testRunData = "$(function(){\r\n" + "			$(\"#testStartTime\").html(\""
				+ getDateFromTimeStamp(metricsFile.getTestStartTime(), "MM-dd-yyyy HH:mm:ss") + "\");\r\n" + "			$(\"#testEndTime\").html(\""
				+ getDateFromTimeStamp(metricsFile.getTestEndTime(), "MM-dd-yyyy HH:mm:ss") + "\");\r\n" + "			$(\"#testRunTime\").html(\""
				+ metricsFile.getTestRunTime() + "\");\r\n" + "		});";
		fileWriter.write(Constants.javascriptCodeBeforeScript + testRunData + Constants.javascriptCodeAfterTestData
				+ serverHealthSeriesData + Constants.javascriptCodeAfterScript);
		fileWriter.close();
	}

	public double getMetricDivider(String metricLabel) {
		if (metricLabel.contains("CPU"))
			return 1000;
		else if (metricLabel.contains("Network"))
			return 1;
		else if (metricLabel.contains("Memory"))
			return 1000000;
		return 1;
	}

	public String getMetricUnit(String metricLabel) {
		if (metricLabel.contains("CPU"))
			return "%";
		else if (metricLabel.contains("Network"))
			return "mb";
		else if (metricLabel.contains("Memory"))
			return "gb~";
		return "";
	}

	public String getCurrentCoordinateData(String key, String value, double divider) {
		return "[" + key + ", " + ((Double.parseDouble(value)) / divider) + "],";
//		return "{x:new Date(" + key + "),y:" + value + "},";
	}

	public String getDateFromTimeStamp(String inputTimeStamp, String dateFormat) {
		timeStamp = new Timestamp(Long.parseLong(inputTimeStamp));
		Date date = new Date(timeStamp.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(date);
	}

	public void prepareTemplateParameters() {
		htmlFileParameters.put("performanceServer", "Shivalik");
		htmlFileParameters.put("performanceTestStartTime",
				getDateFromTimeStamp(metricsFile.getTestStartTime(), "MM-dd-yyyy HH:mm:ss"));
		htmlFileParameters.put("performanceTestEndTime",
				getDateFromTimeStamp(metricsFile.getTestEndTime(), "MM-dd-yyyy HH:mm:ss"));
		htmlFileParameters.put("performanceTestRunTime", metricsFile.getTestRunTime());
	}
}
