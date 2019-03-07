package performanceUtils;

public interface AutomationConstants {

	public final String jmeterHome = "E:\\apache-jmeter-5.0\\";
	public final String jmeterPropertiesLocation = "E:\\apache-jmeter-5.0\\bin\\jmeter.properties";
	public final String presentWorkingDirectory = System.getProperty("user.dir");
	public final String defaultDateTimeFormat = "yyyyMMdd_hhmmssa";

//	portals to test
	public final int Shivalik = 1;
	public final int Asura = 2;
	public final int Dhar = 3;
	public final int Trishul = 4;

//	login type 
	public final int spLoginType = 1;
	public final int partnerLoginType = 2;
	public final int clientLoginType = 3;

//	boolean values to use 
	public final String trueValue = "true";
	public final String falsevalue = "false";

//	Strings
	public final String currentTestOutputDirectory = "currentTestOutputDirectory";
	public final String currentDateTime = "currentDateTime";

//	default property String names
//	keys in properties files should match these values.
	public final String testPlanName = "testPlanName";
	public final String outputDirectory = "output_directory";
	public final String saveJMXFile = "saveJMXFile";
	public final String outputFileNamePrefixPath = "outputFileNamePrefixPath";

//	properties files paths
	public final String deviceCreationPropertiesPath = "deviceCreationTest.properties";
	public final String userCreationPropertiesPath = "userCreationTest.properties";

//	boolean Strings
	public final String True = "true";
	public final String False = "false";

//	serverMetrics
	public final String Label = "label";
	public final String Elapsed = "elapsed";
	public final String Timestampe = "timeStamp";
	public final String ResponseCode = "responseCode";
	public final String Latency = "Latency";

//	HTTPMethods
	public final String GET_METHOD = "GET";
	public final String POST_METHOD = "POST";
}
