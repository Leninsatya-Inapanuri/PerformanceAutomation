package serverHealthReport;

public class Constants {

	// jmeter output files directory , if we run with jar
//	public static final String jmeter_output_directory = System.getProperty("user.dir")+"\\jmeter_output\\";
	// jmeter output files directory , if we run with eclipse ide
	// public static final String jmeter_output_directory =
	// "E:\\apache-jmeter-5.0\\bin\\jmeter_output\\";
	public static final String metricsReportName = "metrics_report.csv";

	public static final String presentWorkingDirectory = System.getProperty("user.dir") + "\\";

//	public static final String 
	public static final String delimiter = ",";
//	
//	
//	indices for metric data;
	public static final int metricTimeStamp = 0;
	public static final int metricValue = 1;
	public static final int metricLabel = 2;
	public static final int metricSuccess = 7;

	public enum metricKeys {
		metricTimeStamp, metricLabel, MetricValue, metricSuccess;
	}

//	
//	
//	please refer ./templates/graph_serverHealth.vm file for original Script of javascriptCodeBeforeScript,javascriptCodeAfterScript.
	public static final String javascriptCodeBeforeScript = "/*\r\n"
			+ "   Licensed to the Apache Software Foundation (ASF) under one or more\r\n"
			+ "   contributor license agreements.  See the NOTICE file distributed with\r\n"
			+ "   this work for additional information regarding copyright ownership.\r\n"
			+ "   The ASF licenses this file to You under the Apache License, Version 2.0\r\n"
			+ "   (the \"License\"); you may not use this file except in compliance with\r\n"
			+ "   the License.  You may obtain a copy of the License at\r\n" + "\r\n"
			+ "       http://www.apache.org/licenses/LICENSE-2.0\r\n" + "\r\n"
			+ "   Unless required by applicable law or agreed to in writing, software\r\n"
			+ "   distributed under the License is distributed on an \"AS IS\" BASIS,\r\n"
			+ "   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\r\n"
			+ "   See the License for the specific language governing permissions and\r\n"
			+ "   limitations under the License.\r\n" + "*/\r\n" + "$(document).ready(function() {\r\n" + "\r\n"
			+ "});\r\n" + "\r\n" + "\r\n";

	public static final String javascriptCodeAfterTestData = " /* @param elementId Id of element where we display message\r\n"
			+ " */\r\n" + "function setEmptyGraph(elementId) {\r\n" + "	$(function() {\r\n"
			+ "		$(elementId).text(\"No graph series with filter=\" + seriesFilter);\r\n" + "	});\r\n" + "}\r\n"
			+ "var latenciesOverTimeInfos = {\r\n" + "	data: {\r\n" + "		\"result\": {\r\n"
			+ "			\"minY\": 0.0,\r\n" + "			\"minX\": 0,\r\n" + "			\"series\": [";

	public static final String javascriptCodeAfterScript = "],\r\n"
			+ "			\"supportsControllersDiscrimination\": true,\r\n" + "			\"granularity\": 1000000,\r\n"
			+ "			\"title\": \"Server Health Metrics\"\r\n" + "		}\r\n" + "	},\r\n"
			+ "	getOptions: function() {\r\n" + "		return {\r\n" + "			series: {\r\n"
			+ "				lines: {\r\n" + "					show: true\r\n" + "				},\r\n"
			+ "				points: {\r\n" + "					show: true\r\n" + "				}\r\n"
			+ "			},\r\n" + "			xaxis: {\r\n" + "				mode: \"time\",\r\n"
			+ "				timeformat: getTimeFormat(this.data.result.granularity),\r\n"
			+ "				axisLabel: \"Time (Granualarity : \" + this.data.result.granularity + \")\",\r\n"
			+ "				axisLabelUseCanvas: true,\r\n" + "				axisLabelFontSizePixels: 12,\r\n"
			+ "				axisLabelFontFamily: 'Verdana, Arial',\r\n" + "				axisLabelPadding: 20,\r\n"
			+ "			},\r\n" + "			yaxis: {\r\n" + "				axisLabel: \"Metric Values\",\r\n"
			+ "				axisLabelUseCanvas: true,\r\n" + "				axisLabelFontSizePixels: 12,\r\n"
			+ "				axisLabelFontFamily: 'Verdana, Arial',\r\n" + "				tickDecimals: 2,\r\n"
			+ "				axisLabelPadding: 20,\r\n" + "			},\r\n" + "			legend: {\r\n"
			+ "				noColumns: 2,\r\n" + "				show: true,\r\n"
			+ "				container: '#legendLatenciesOverTime'\r\n" + "			},\r\n"
			+ "			selection: {\r\n" + "				mode: 'xy'\r\n" + "			},\r\n" + "			grid: {\r\n"
			+ "				hoverable: true // IMPORTANT! this is needed for tooltip to\r\n"
			+ "				// work\r\n" + "			},\r\n" + "			tooltip: true,\r\n"
			+ "			tooltipOpts: {\r\n" + "				content: getTooltip,\r\n"
			+ "				xDateFormat: \"%Y-%m-%d %H:%M:%S\",\r\n" + "			}\r\n" + "		};\r\n" + "	},\r\n"
			+ "	createGraph: function() {\r\n" + "		var data = this.data;\r\n"
			+ "		var dataset = prepareData(data.result.series, $(\"#choicesLatenciesOverTime\"));\r\n"
			+ "		var options = this.getOptions();\r\n" + "		prepareOptions(options, data);\r\n"
			+ "		$.plot($(\"#flotLatenciesOverTime\"), dataset, options);\r\n" + "		// setup overview\r\n"
			+ "		$.plot($(\"#overviewLatenciesOverTime\"), dataset, prepareOverviewOptions(options));\r\n"
			+ "	}\r\n" + "};\r\n" + "// Latencies Over Time\r\n"
			+ "function refreshLatenciesOverTime(fixTimestamps) {\r\n" + "	var infos = latenciesOverTimeInfos;\r\n"
			+ "	prepareSeries(infos.data);\r\n" + "	if (infos.data.result.series.length == 0) {\r\n"
			+ "		setEmptyGraph(\"#bodyLatenciesOverTime\");\r\n" + "		return;\r\n" + "	}\r\n"
			+ "	if (fixTimestamps) {\r\n" + "		fixTimeStamps(infos.data.result.series, 19800000);\r\n" + "	}\r\n"
			+ "	if (isGraph($(\"#flotLatenciesOverTime\"))) {\r\n" + "		infos.createGraph();\r\n"
			+ "	} else {\r\n" + "		var choiceContainer = $(\"#choicesLatenciesOverTime\");\r\n"
			+ "		createLegend(choiceContainer, infos);\r\n" + "		infos.createGraph();\r\n"
			+ "		setGraphZoomable(\"#flotLatenciesOverTime\", \"#overviewLatenciesOverTime\");\r\n"
			+ "		$('#footerLatenciesOverTime .legendColorBox > div').each(function(i) {\r\n"
			+ "			$(this).clone().prependTo(choiceContainer.find(\"li\").eq(i));\r\n" + "		});\r\n"
			+ "	}\r\n" + "};\r\n" + "\r\n" + "\r\n"
			+ "// Collapse the graph matching the specified DOM element depending the collapsed\r\n" + "// status\r\n"
			+ "function collapse(elem, collapsed) {\r\n" + "	if (collapsed) {\r\n"
			+ "		$(elem).parent().find(\".fa-chevron-up\").removeClass(\"fa-chevron-up\").addClass(\"fa-chevron-down\");\r\n"
			+ "	} else {\r\n"
			+ "		$(elem).parent().find(\".fa-chevron-down\").removeClass(\"fa-chevron-down\").addClass(\"fa-chevron-up\");\r\n"
			+ "		if (elem.id == \"bodyLatenciesOverTime\") {\r\n"
			+ "			if (isGraph($(elem).find('.flot-chart-content')) == false) {\r\n"
			+ "				refreshLatenciesOverTime(true);\r\n" + "			}\r\n" + "		}\r\n" + "	}\r\n"
			+ "}\r\n" + "\r\n" + "/*\r\n"
			+ " * Activates or deactivates all series of the specified graph (represented by id parameter)\r\n"
			+ " * depending on checked argument.\r\n" + " */\r\n" + "function toggleAll(id, checked) {\r\n"
			+ "	var placeholder = document.getElementById(id);\r\n" + "\r\n"
			+ "	var cases = $(placeholder).find(':checkbox');\r\n" + "	cases.prop('checked', checked);\r\n"
			+ "	$(cases).parent().children().children().toggleClass(\"legend-disabled\", !checked);\r\n" + "\r\n"
			+ "	var choiceContainer;\r\n" + "	if (id == \"choicesLatenciesOverTime\") {\r\n"
			+ "		choiceContainer = $(\"#choicesLatenciesOverTime\");\r\n"
			+ "		refreshLatenciesOverTime(false);\r\n" + "	}\r\n"
			+ "	var color = checked ? \"black\" : \"#818181\";\r\n"
			+ "	choiceContainer.find(\"label\").each(function() {\r\n" + "		this.style.color = color;\r\n"
			+ "	});\r\n" + "}\r\n" + "\r\n" + "function getTooltip(metricLabel, x, y) {\r\n"
			+ "	return \" %x -> \" + metricLabel + \" -> %y\";\r\n" + "}";

}
