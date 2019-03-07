package performanceTestBase;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import performanceUtils.AutomationConstants;
import performanceUtils.TestContext;

public class BaseTest {

	protected TestContext testContext = null;

	@BeforeMethod
	public void setUpTest() {
		testContext = new TestContext();
	}

	@AfterMethod
	public void GenerateDashboardReports(String simple) {
		System.out.println("helloworld");
	}
}
