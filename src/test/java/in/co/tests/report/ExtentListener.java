package in.co.tests.report;

import com.aventstack.extentreports.Status;
import in.co.webdriver.driverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTestManager.createTest(result.getMethod().getMethodName()).info("Test Started");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTestManager.getTest().log(Status.PASS, "Test Passed");

        try {
            String base64Screenshot = ((TakesScreenshot) driverFactory.getDriver())
                    .getScreenshotAs(OutputType.BASE64);
            ExtentTestManager.getTest().pass("Screenshot on Failure",
                    com.aventstack.extentreports.MediaEntityBuilder
                            .createScreenCaptureFromBase64String(base64Screenshot)
                            .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTestManager.getTest().log(Status.FAIL, result.getThrowable());

        try {
            String base64Screenshot = ((TakesScreenshot) driverFactory.getDriver())
                    .getScreenshotAs(OutputType.BASE64);
            ExtentTestManager.getTest().fail("Screenshot on Failure",
                    com.aventstack.extentreports.MediaEntityBuilder
                            .createScreenCaptureFromBase64String(base64Screenshot)
                            .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTestManager.getTest().log(Status.SKIP, "Test Skipped");

        try {
            String base64Screenshot = ((TakesScreenshot) driverFactory.getDriver())
                    .getScreenshotAs(OutputType.BASE64);
            ExtentTestManager.getTest().skip("Screenshot on Failure",
                    com.aventstack.extentreports.MediaEntityBuilder
                            .createScreenCaptureFromBase64String(base64Screenshot)
                            .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.getInstance().flush();
    }
}
