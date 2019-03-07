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

/**
 * @author Leninkumar.Inapanuri
 *
 */
public class DeviceCreation extends BaseTestPlan {

	public DeviceCreation(String propertiesLocation) throws IOException {
		super(propertiesLocation);
	}

	@Override
	public void prepareTestPlan() throws JMeterEngineException, FileNotFoundException, IOException {

		// First HTTP Sampler - open uttesh.com
		HTTPSampler examplecomSampler = new HTTPSampler();
		examplecomSampler.setDomain("www.google.com");
//		examplecomSampler.setPort(80);
		examplecomSampler.setPath("/");
		examplecomSampler.setMethod(AutomationConstants.GET_METHOD);
		examplecomSampler.setName("Google");
		examplecomSampler.setProperty(TestElement.TEST_CLASS, HTTPSampler.class.getName());
		examplecomSampler.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());
		HashTree threadGroupHashTree = getDefaulatThreadGroup("Device Creation", 1, 1, 100);
		threadGroupHashTree.add(examplecomSampler);
	}

	@Override
	protected void postTestRun() throws InterruptedException {

		System.out.println("Test Ended");
		System.out.println(testPlan.getUserDefinedVariables().toString());
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