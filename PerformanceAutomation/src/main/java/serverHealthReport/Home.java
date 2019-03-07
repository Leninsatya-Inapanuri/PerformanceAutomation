package serverHealthReport;


public class Home {

	public static void main(String[] args) throws Exception {
		Thread.sleep(1000);
		HealthChart healthChart = new HealthChart();
		healthChart.prepareChartDataSeries();
	}
}
