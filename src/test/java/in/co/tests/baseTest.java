package in.co.tests;

import in.co.webdriver.driverFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class baseTest {

    @BeforeSuite
    public void setup() throws InterruptedException {
        driverFactory.initDriver("chrome");
        Thread.sleep(5000);
    }

    @AfterSuite
    public void teardown() {
        driverFactory.quitDriver();
    }

}
