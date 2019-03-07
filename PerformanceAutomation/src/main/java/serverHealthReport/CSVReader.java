package serverHealthReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class CSVReader {

//			HashMap<MetricName <Metric Data Node>>
	private HashMap<String, ArrayList<MetricDataNode>> metricData = new HashMap<String, ArrayList<MetricDataNode>>();
	private static ArrayList<String> metricsHeaders = new ArrayList<String>(), metricLabels = new ArrayList<String>();
	private Scanner metricsFile;
	private String testStartTime, testEndTime;
	int number_of_metrics_read = 0;
//	private ReportDate reportDate = new ReportDate();

	CSVReader(String metricsFileLocation) throws FileNotFoundException {
		File metricsReportFile = new File(metricsFileLocation);
		metricsFile = new Scanner(metricsReportFile);
		metricsHeaders.addAll(Arrays.asList(metricsFile.nextLine().toString().split(",")));
		while (metricsFile.hasNextLine()) {
			List<String> currentMetricData = Arrays.asList(metricsFile.nextLine().split(Constants.delimiter));

//			Current metric Data Label Name i.e Metric Type
			String currentMetricDataLabel = currentMetricData.get(Constants.metricLabel);

//			add the current metric label to list of labels if not scanned yet.
			if (!(metricLabels.contains(currentMetricDataLabel))) {
				metricLabels.add(currentMetricDataLabel);
				metricData.put(currentMetricDataLabel, new ArrayList<MetricDataNode>());
			}

//			Adding the required fields from csv file to Metric Data Node Object
			MetricDataNode currentMetricDataNode = new MetricDataNode();
			currentMetricDataNode.addToMetricDataNode(Constants.metricKeys.metricLabel.name(),
					currentMetricData.get(Constants.metricLabel));
			currentMetricDataNode.addToMetricDataNode(Constants.metricKeys.metricTimeStamp.name(),
					currentMetricData.get(Constants.metricTimeStamp));
			currentMetricDataNode.addToMetricDataNode(Constants.metricKeys.MetricValue.name(),
					currentMetricData.get(Constants.metricValue));
			currentMetricDataNode.addToMetricDataNode(Constants.metricKeys.metricSuccess.name(),
					currentMetricData.get(Constants.metricSuccess));
			if (number_of_metrics_read == 0) {
				setTestStartTime(currentMetricData.get(Constants.metricTimeStamp));
				number_of_metrics_read = 1;
			}
			setTestEndTime(currentMetricData.get(Constants.metricTimeStamp));

//			Adding Metric Data Node Object to The List of Metric Data of Current metric  Type
			metricData.get(currentMetricDataLabel).add(currentMetricDataNode);
		} // End of Reading Metric File
	}

	/**
	 * prints the Metrics Data Scanned from the source file.
	 */
	public void printMetricData() {
		for (String key : metricData.keySet()) {
			for (MetricDataNode metricDataNode : metricData.get(key)) {
				System.out.println(metricDataNode.getMetric(Constants.metricKeys.MetricValue.name()));
			}
		}
	}

	/**
	 * @return the metricData
	 */
	public HashMap<String, ArrayList<MetricDataNode>> getMetricData() {
		return metricData;
	}

	/**
	 * @return the metricLabels
	 */
	public ArrayList<String> getMetricLabels() {
		return metricLabels;
	}

	/**
	 * @return the testStartTime
	 */
	public String getTestStartTime() {
		return testStartTime;
	}

	/**
	 * @param testStartTime the testStartTime to set
	 */
	public void setTestStartTime(String testStartTime) {
		this.testStartTime = testStartTime;
	}

	/**
	 * @return the testEndTime
	 */
	public String getTestEndTime() {
		return testEndTime;
	}

	/**
	 * @param testEndTime the testEndTime to set
	 */
	public void setTestEndTime(String testEndTime) {
		this.testEndTime = testEndTime;
	}

	/**
	 * @return the testRunTime
	 */
	public String getTestRunTime() {

//		return testRunTime;
		double secs = calculateTestRunTimeDifference();
		String hours = ((int) secs / 3600) + "H - ";
		secs %= 3600;
		String minutes = ((int) secs / 60) + "M - ";
		secs %= 60;
		return hours + minutes + String.format("%.2f", secs) + "Seconds";
	}

	public double calculateTestRunTimeDifference() {
		Double tstime = Double.valueOf(getTestEndTime()) - (Double.valueOf(getTestStartTime()));
		tstime = tstime / 1000;
		return tstime;
	}
}
