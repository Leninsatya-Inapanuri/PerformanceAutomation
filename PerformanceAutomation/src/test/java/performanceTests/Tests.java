/**
 * 
 */
package performanceTests;

import java.io.IOException;

import org.apache.jmeter.engine.JMeterEngineException;
import org.testng.annotations.Test;

import performanceUseCases.DeviceCreation;
import performanceUseCases.UserCreation;
import performanceUtils.AutomationConstants;

/**
 * @author Leninkumar.Inapanuri
 *
 */
public class Tests {

	@Test
	public void deviceCreationTestPlan() throws IOException, JMeterEngineException, InterruptedException {
		DeviceCreation deviceCreation = new DeviceCreation(AutomationConstants.deviceCreationPropertiesPath);
		deviceCreation.prepareTestPlan();
		deviceCreation.runTest();
	}

	@Test
	public void userCreationTestPlan() throws IOException, InterruptedException, JMeterEngineException {
		UserCreation userCreation = new UserCreation(AutomationConstants.userCreationPropertiesPath);
		userCreation.prepareTestPlan();
		userCreation.runTest();
	}
}
