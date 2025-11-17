package in.co.tests;

import com.aventstack.extentreports.ExtentTest;
import in.co.base.basepage;
import in.co.locators.b2c_locatortype;
import in.co.pages.loginPage;
import in.co.tests.report.ExtentTestManager;
import in.co.webdriver.driverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Test
//@Listeners(in.co.tests.reportListener.class)
@Listeners(in.co.tests.report.ExtentListener.class)
public class tabNavigation extends baseTest{

    private static WebDriver driver = driverFactory.getDriver();

    @Test(priority = 0)
    public void openApp() throws InterruptedException {
        ExtentTest test = ExtentTestManager.getTest();
        basepage basepg = new basepage(test);
        test.info("Opening browser");

        driverFactory.openUrl("https://10.240.129.219:8080/app");
        Thread.sleep(3000);

        loginPage loginpage = new loginPage();
        loginpage.login("superadmin", "superadmin");
        test.pass("Login successfully");
        Thread.sleep(3000);

    }

    @Test(dependsOnMethods = {"openApp"})
    public void faultmenus() throws InterruptedException {
        ExtentTest test = ExtentTestManager.getTest();
        basepage basepg = new basepage(test);
        test.info("Clicking Fault submenu...");
        basepg.clickSubmenuContainer(By.xpath(b2c_locatortype.fault_submenu), b2c_locatortype.waits);
        test.pass("Fault submenu navigation completed");
        Thread.sleep(3000);

    }

}
