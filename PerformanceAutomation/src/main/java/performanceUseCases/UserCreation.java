package performanceUseCases;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.jmeter.engine.JMeterEngineException;
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.collections.HashTree;

import performanceUtils.AutomationConstants;
import performanceUtils.BaseTestPlan;

public class UserCreation extends BaseTestPlan {

	public UserCreation(String propertiesFileName) throws IOException {
		super(propertiesFileName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void prepareTestPlan() throws JMeterEngineException, FileNotFoundException, IOException {

		HashTree threadGroup = getDefaulatThreadGroup("userCreation", 1, 1, 30);

		HTTPSampler google = new HTTPSampler();
		google.setName("Google");
		google.setDomain("www.google.com");
		google.setPath("/");
		google.setProperty(TestElement.TEST_CLASS, HTTPSampler.class.getName());
		google.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());
		google.setMethod(AutomationConstants.GET_METHOD);
		threadGroup.add(google);
	}

	@Override
	public void postTestRun() throws InterruptedException {
		System.out.println("Test Completed");
		// TODO Auto-generated method stub
	}

	@Override
	protected void prepareSetupThreadGroup() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void prepareTearDownThreadGroup() {
		// TODO Auto-generated method stub

	}

}
