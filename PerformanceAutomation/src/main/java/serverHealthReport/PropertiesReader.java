package serverHealthReport;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

	private String propertiesFileLocation = Constants.presentWorkingDirectory
			+ "\\sourceFiles\\serverHealthChart.properties";
	private Properties properties = new Properties();

	PropertiesReader() throws IOException {
		FileReader propertiesFile = new FileReader(propertiesFileLocation);
		this.properties.load(propertiesFile);
		propertiesFile.close();
	}

	public String getProperty(String propertyName) {
		return properties.getProperty(propertyName);
	}
}
