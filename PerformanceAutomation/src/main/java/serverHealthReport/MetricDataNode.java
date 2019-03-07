package serverHealthReport;

import java.util.HashMap;

public class MetricDataNode {

	private HashMap<String, String> metricDataNode = new HashMap<String, String>();

	public void addToMetricDataNode(String key, String value) {
		this.metricDataNode.put(key, value);
	}

	public void updateMetricDataNode(String key, String value) {
		addToMetricDataNode(key, value);
	}

	public void removeFromMetricDataNode(String key) {
		this.metricDataNode.remove(key);
	}

	public String getMetric(String metricKey) {
		return this.metricDataNode.get(metricKey);
	}

}
