package performanceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.engine.JMeterEngineException;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.threads.PostThreadGroup;
import org.apache.jmeter.threads.SetupThreadGroup;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.threads.gui.PostThreadGroupGui;
import org.apache.jmeter.threads.gui.SetupThreadGroupGui;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jmeter.timers.Timer;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.ListedHashTree;

import kg.apc.jmeter.perfmon.PerfMonCollector;
import kg.apc.jmeter.vizualizers.PerfMonGui;

public abstract class BaseTestPlan {

	protected StandardJMeterEngine jmeter = new StandardJMeterEngine();
	protected TestPlan testPlan = null;
	protected HashTree testPlanTree = new ListedHashTree(), setupThreadGroupTree = new ListedHashTree(),
			tearDownThreadGroupTree = new ListedHashTree();
	protected Properties properties = new Properties();

	protected String slash = "\\";
	private String underscore = "_";
	protected String outputFileNamePrefixPath = "";

	/**
	 * 
	 * @throws JMeterEngineException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected abstract void prepareTestPlan() throws JMeterEngineException, FileNotFoundException, IOException;

	/**
	 * 
	 * @throws InterruptedException
	 */
	protected abstract void postTestRun() throws InterruptedException;

	/**
	 * Override this method if you want to change anything in SetupThreadGroup
	 * .Otherwise leave it empty
	 */
	protected abstract void prepareSetupThreadGroup();

	/**
	 * Override this method if you want to change anything in TearDownThreadGroup
	 * .Otherwise leave it empty
	 */
	protected abstract void prepareTearDownThreadGroup();

	/**
	 * 
	 * @param testPlanName
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	protected BaseTestPlan(String propertiesFileName) throws IOException {
//		Loading Properties from properties file
		String propertiesFilePath = AutomationConstants.presentWorkingDirectory + slash + "TestResources" + slash
				+ propertiesFileName;
		FileReader propertiesReader = new FileReader(propertiesFilePath);
		properties.load(propertiesReader);
		properties.setProperty(AutomationConstants.currentDateTime, Utilities.getCurrentDateAndTime());
		properties.setProperty(AutomationConstants.currentTestOutputDirectory,
				properties.getProperty(AutomationConstants.outputDirectory)
						+ properties.getProperty(AutomationConstants.testPlanName) + slash
						+ properties.getProperty(AutomationConstants.currentDateTime) + slash);
		outputFileNamePrefixPath = properties.getProperty(AutomationConstants.currentTestOutputDirectory)
				+ properties.getProperty(AutomationConstants.testPlanName) + underscore
				+ properties.getProperty(AutomationConstants.currentDateTime);
		propertiesReader.close();
		System.out.println("Curren Running Test : " + properties.getProperty(AutomationConstants.testPlanName));

//		Creating Folder for output files (if not exists).
		File outputDirectory = new File(properties.getProperty(AutomationConstants.currentTestOutputDirectory));
		outputDirectory.mkdirs();

		// JMeter initialization (properties, log levels, locale, etc)
		JMeterUtils.setJMeterHome(AutomationConstants.jmeterHome);
		JMeterUtils.loadJMeterProperties(AutomationConstants.jmeterPropertiesLocation);
		JMeterUtils.initLogging();// you can comment this line out to see extra log messages of i.e. DEBUG level
		JMeterUtils.initLocale();

		// Test Plan
		testPlan = new TestPlan(properties.getProperty(AutomationConstants.testPlanName));
		testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
		testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());
		testPlan.setUserDefinedVariables((Arguments) new ArgumentsPanel().createTestElement());
		
		
		
		// Construct Test Plan from previously initialized elements
		testPlanTree.add(testPlan);
		setupThreadGroupTree = testPlanTree.add(testPlan, getSetupThreadGroup());
		tearDownThreadGroupTree = testPlanTree.add(testPlan, getTearDownThreadGroup());
//		tearDownThreadGroupTree 
		Timer timer = new Timer() {
			public long delay() {
				// TODO Auto-generated method stub
				return 2000;
			}
		};
		tearDownThreadGroupTree.add(timer);
	}

	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void runTest() throws FileNotFoundException, IOException, InterruptedException {

//		Adding Listeners to testPlan

		PerfMonCollector perfMonCollector = new PerfMonCollector();
		perfMonCollector.setName("Metrics Collector");
		perfMonCollector.setProperty("filename", outputFileNamePrefixPath + underscore + "metrics.csv");
		CollectionProperty metricConnections = new CollectionProperty();
		metricConnections.setName("metricConnections");
		CollectionProperty cpu = new CollectionProperty();
		cpu.setName("cpu");
		cpu.addProperty(new StringProperty("host", properties.getProperty("perfMonHost")));
		cpu.addProperty(new StringProperty("port", properties.getProperty("perfMonPort")));
		cpu.addProperty(new StringProperty("metric", "CPU"));
		cpu.addProperty(new StringProperty("metricParam", "combined"));

		CollectionProperty memory = new CollectionProperty();
		memory.setName("Memory");
		memory.addProperty(new StringProperty("host", properties.getProperty("perfMonHost")));
		memory.addProperty(new StringProperty("port", properties.getProperty("perfMonPort")));
		memory.addProperty(new StringProperty("metric", "Memory"));
		memory.addProperty(new StringProperty("metricParam", "usedperc"));

		metricConnections.addProperty(cpu);
		metricConnections.addProperty(memory);
		perfMonCollector.setProperty(metricConnections);
		perfMonCollector.setProperty(TestElement.TEST_CLASS, PerfMonCollector.class.getName());
		perfMonCollector.setProperty(TestElement.GUI_CLASS, PerfMonGui.class.getName());
		testPlanTree.add(perfMonCollector);
//		exporting test plan to jmx , write this code before adding resultCollectors , otherwise output jmx file will be corrupted.
		if (properties.getProperty(AutomationConstants.saveJMXFile).equalsIgnoreCase(AutomationConstants.True)) {
			SaveService.saveTree(testPlanTree, new FileOutputStream(outputFileNamePrefixPath + ".jmx"));
		}
		testPlanTree.add(testPlanTree.getArray()[0],
				getResultCollector(outputFileNamePrefixPath + ".csv", getCustomizedOutputCSVConfiguration()));
		testPlanTree.add(testPlanTree.getArray()[0], getResultCollector(
				outputFileNamePrefixPath + underscore + "simpleDataWriter.csv", getCompleteOutputCSVConfiguration()));

		jmeter.configure(testPlanTree);
		jmeter.run();
		generateDashboard();
	}

	public void generateDashboard() throws IOException, InterruptedException {
		String simpleDataWriterFile = outputFileNamePrefixPath + underscore + "simpleDataWriter.csv";
		String dashboardGeneratorCommand = "E:\\\\apache-jmeter-5.0\\bin\\jmeter.bat -g " + simpleDataWriterFile
				+ " -o " + properties.getProperty(AutomationConstants.currentTestOutputDirectory) + slash + "Dashboard";

		Runtime.getRuntime().exec("C:/Windows/System32/cmd.exe /c " + dashboardGeneratorCommand);
		Thread.sleep(2000);
		File dashboardsDirectory = new File(
				properties.getProperty(AutomationConstants.currentTestOutputDirectory) + slash + "Dashboard\\");
		System.out.println(dashboardsDirectory.exists());
		while (!(dashboardsDirectory.exists())) {
			Thread.sleep(1000);
			System.out.println("Waiting for Generating Reports..!");
		}
		postTestRun();
	}

	/**
	 * 
	 * @param threadGroupName
	 * @param numberOfThreads
	 * @param rampUpTime
	 * @param numberOfLoops
	 * @return basic ThreadGroup
	 */
	public HashTree getDefaulatThreadGroup(String threadGroupName, int numberOfThreads, int rampUpTime,
			int numberOfLoops) {
		// Loop Controller
		LoopController loopController = new LoopController();
		loopController.setLoops(numberOfLoops);
		loopController.setFirst(true);
		loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
		loopController.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());
		loopController.initialize();

		// Thread Group
		ThreadGroup threadGroup = new ThreadGroup();
		threadGroup.setName(threadGroupName);
		threadGroup.setNumThreads(numberOfThreads);
		threadGroup.setRampUp(rampUpTime);
		threadGroup.setSamplerController(loopController);
		threadGroup.setProperty(TestElement.TEST_CLASS, ThreadGroup.class.getName());
		threadGroup.setProperty(TestElement.GUI_CLASS, ThreadGroupGui.class.getName());

		HashTree threadGroupTree = testPlanTree.add(testPlan, threadGroup);
		return threadGroupTree;
	}

	public SetupThreadGroup getSetupThreadGroup() {
		// Loop Controller
		LoopController loopController = new LoopController();
		loopController.setLoops(1);
		loopController.setFirst(true);
		loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
		loopController.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());
		loopController.initialize();

		SetupThreadGroup setupThreadGroup = new SetupThreadGroup();
		setupThreadGroup.setName("Load Test");
		setupThreadGroup.setNumThreads(1);
		setupThreadGroup.setRampUp(1);
		setupThreadGroup.setProperty(TestElement.TEST_CLASS, SetupThreadGroup.class.getName());
		setupThreadGroup.setProperty(TestElement.GUI_CLASS, SetupThreadGroupGui.class.getName());
		setupThreadGroup.setSamplerController(loopController);
		return setupThreadGroup;
	}

	public PostThreadGroup getTearDownThreadGroup() {
		// Loop Controller
		LoopController loopController = new LoopController();
		loopController.setLoops(1);
		loopController.setFirst(true);
		loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
		loopController.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());
		loopController.initialize();

		PostThreadGroup tearDownThreadGroup = new PostThreadGroup();
		tearDownThreadGroup.setName("TearDown Test");
		tearDownThreadGroup.setNumThreads(1);
		tearDownThreadGroup.setRampUp(1);
		tearDownThreadGroup.setProperty(TestElement.TEST_CLASS, PostThreadGroup.class.getName());
		tearDownThreadGroup.setProperty(TestElement.GUI_CLASS, PostThreadGroupGui.class.getName());
		tearDownThreadGroup.setSamplerController(loopController);

		return tearDownThreadGroup;
	}

	public List<HTTPSampler> getLogin() {
		ArrayList<HTTPSampler> login = new ArrayList<HTTPSampler>();

		return login;
	}

	/**
	 * 
	 * @return customized SampleSaveConfiguration for Logger
	 */
	public SampleSaveConfiguration getCustomizedOutputCSVConfiguration() {
		SampleSaveConfiguration sampleSaveConfiguration = new SampleSaveConfiguration();
		sampleSaveConfiguration.setAsXml(false);
		sampleSaveConfiguration.setTimestamp(true);
		sampleSaveConfiguration.setFieldNames(true);
		sampleSaveConfiguration.setLabel(true);
		sampleSaveConfiguration.setLatency(true);
		sampleSaveConfiguration.setIdleTime(true);
		sampleSaveConfiguration.setConnectTime(true);
		sampleSaveConfiguration.setBytes(true);
		sampleSaveConfiguration.setSentBytes(true);
		sampleSaveConfiguration.setSuccess(true);

		sampleSaveConfiguration.setResponseData(false);
		sampleSaveConfiguration.setResponseHeaders(false);
		sampleSaveConfiguration.setFileName(false);
		sampleSaveConfiguration.setSampleCount(false);
		sampleSaveConfiguration.setEncoding(false);
		sampleSaveConfiguration.setRequestHeaders(false);
		sampleSaveConfiguration.setMessage(false);
		sampleSaveConfiguration.setSamplerData(false);
		sampleSaveConfiguration.setHostname(false);
		sampleSaveConfiguration.setThreadName(false);
		sampleSaveConfiguration.setThreadCounts(false);
		sampleSaveConfiguration.setDataType(false);
		sampleSaveConfiguration.setAssertionResultsFailureMessage(false);
		return sampleSaveConfiguration;
	}

	public SampleSaveConfiguration getCompleteOutputCSVConfiguration() {
		SampleSaveConfiguration sampleSaveConfiguration = new SampleSaveConfiguration();

		sampleSaveConfiguration.setAsXml(false);
		sampleSaveConfiguration.setTimestamp(true);
		sampleSaveConfiguration.setFieldNames(true);
		sampleSaveConfiguration.setLabel(true);
		sampleSaveConfiguration.setResponseData(true);
		sampleSaveConfiguration.setMessage(true);
		sampleSaveConfiguration.setThreadName(true);
		sampleSaveConfiguration.setThreadName(true);
		sampleSaveConfiguration.setDataType(true);
		sampleSaveConfiguration.setSuccess(true);
		sampleSaveConfiguration.setAssertionResultsFailureMessage(true);
		sampleSaveConfiguration.setBytes(true);
		sampleSaveConfiguration.setSentBytes(true);
		sampleSaveConfiguration.setThreadCounts(true);
		sampleSaveConfiguration.setUrl(true);
		sampleSaveConfiguration.setLatency(true);
		sampleSaveConfiguration.setIdleTime(true);
		sampleSaveConfiguration.setConnectTime(true);

		sampleSaveConfiguration.setResponseHeaders(false);
		sampleSaveConfiguration.setFileName(false);
		sampleSaveConfiguration.setSampleCount(false);
		sampleSaveConfiguration.setEncoding(false);
		sampleSaveConfiguration.setRequestHeaders(false);
		sampleSaveConfiguration.setSamplerData(false);
		sampleSaveConfiguration.setHostname(false);
		return sampleSaveConfiguration;
	}

	/**
	 * 
	 * @param fileName
	 * @param saveConfig
	 * @return resultCollector
	 */
	public ResultCollector getResultCollector(String fileName, SampleSaveConfiguration saveConfig) {

		Summariser summariser = null;
		String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");
		if (summariserName.length() > 0) {
			summariser = new Summariser(summariserName);
		}
		ResultCollector resultCollector = new ResultCollector(summariser);
		resultCollector.setFilename(fileName);
		resultCollector.setSaveConfig(saveConfig);

		return resultCollector;
	}
}
