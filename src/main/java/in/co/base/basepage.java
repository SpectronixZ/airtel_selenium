package in.co.base;

import com.aventstack.extentreports.ExtentTest;
import in.co.webdriver.driverFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class basepage {

    private static WebDriver driver = driverFactory.getDriver();

    private static WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

    public static String runFolder = null;

    public static String initFolder = null;

    public static String path;

    private static ExtentTest test;

    public basepage(ExtentTest test) {
        this.test = test;
    }

    public static void sendkeys(By locatortype, String locatorvalue) {
        WebElement element = driver.findElement(locatortype);
        try {
            element.sendKeys(locatorvalue);
            System.out.println(element.getText() + " ::: " + locatorvalue);
        } catch (Exception e) {
            System.out.println("Element not found or keys not able to pass : " + element.getText() + e);
        }
    }

    public static void click(By locatortype) {
        WebElement element = driver.findElement(locatortype);
        long startTime = System.currentTimeMillis();
        try {
            element.click();
            long executionTime = System.currentTimeMillis() - startTime;
            logPassWithScreenshot(test, driver, "Clicked element: " + element.getText(), executionTime);
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logFailWithScreenshot(test, driver, "Failed to click element: " + element.getText(), e, executionTime);
        }
    }

    public static void clickSubmenuContainer(By locatortype, List<By> waitforelement) {
        List<WebElement> element = driver.findElements(locatortype);
        for (int i = 0; i < element.size(); i++) {
            WebElement submenus = element.get(i);
            long startTime = System.currentTimeMillis();
            try {
                if (!submenus.isDisplayed()) {
                    scroll(submenus);
                }
                submenus.click();
                if (waitforelement.size() == 1) {
                    waituntilelementloads(waitforelement.get(0));
                } else if (waitforelement.size() > 1) {
                    for (By waitele : waitforelement) {
                        waituntilelementloads(waitele);
                        break;
                    }
                }
                long executionTime = System.currentTimeMillis() - startTime;
                logPassWithScreenshot(test, driver, "Clicked submenu: " + submenus.getText(), executionTime);
            } catch (Exception e) {
                long executionTime = System.currentTimeMillis() - startTime;
                logFailWithScreenshot(test, driver, "Failed submenu: " + submenus.getText(), e, executionTime);
            }
        }
    }

    public static void waituntilelementloads(By element) {
        long starttime = System.currentTimeMillis();
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
        long endtime = System.currentTimeMillis();
        System.out.println("time taken to load :: " + (endtime - starttime));
    }

    public static void scroll(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        } catch (Exception e) {
            System.err.println("unable to scroll to element: " + e.getMessage());
        }
    }

    public static void logPassWithScreenshot(ExtentTest test, WebDriver driver, String message, long executionTime) {
        try {
            String base64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);

            String html = "<table border='1' style='width:100%; border-collapse: collapse;'>"
                    + "<tr><th>Message</th><th>Status</th><th>Execution Time (ms)</th><th>Screenshot</th></tr>"
                    + "<tr><td>" + message + "</td><td><span style='color:green;'>PASS</span></td><td>" + executionTime + "</td>"
                    + "<td><img src='data:image/png;base64," + base64 + "' width='100' height='100' /></td></tr>"
                    + "</table>";

            test.info(html);
        } catch (Exception e) {
            test.warning("Failed to capture screenshot: " + e.getMessage());
        }
    }

    public static void logFailWithScreenshot(ExtentTest test, WebDriver driver, String message, Throwable throwable, long executionTime) {
        try {
            String base64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);

            String html = "<table border='1' style='width:100%; border-collapse: collapse;'>"
                    + "<tr><th>Message</th><th>Status</th><th>Execution Time (ms)</th><th>Screenshot</th></tr>"
                    + "<tr><td>" + message + "</td><td><span style='color:red;'>FAIL</span></td><td>" + executionTime + "</td>"
                    + "<td><img src='data:image/png;base64," + base64 + "' width='100' height='100' /></td></tr>"
                    + "</table>";

            test.fail(html);
            test.fail(throwable);
        } catch (Exception e) {
            test.warning("Failed to capture screenshot: " + e.getMessage());
        }
    }

//    public static String takeScreenShot(String image) {
//        WebDriver driver = driverFactory.getDriver(); // get current driver
//        if (driver == null) {
//            System.err.println("Driver is null, cannot take screenshot: " + image);
//            return null;
//        }
//        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
//        // 1️⃣ Ensure folder exists
//        if (runFolder == null) {
//            String dateFolder = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
//            runFolder = System.getProperty("user.dir") + "/src/test/java/screenshots/" + dateFolder + "/";
//            new File(runFolder).mkdirs();
//            System.out.println("runFolder ::: " + runFolder);
//        }
//        String path = runFolder + image + "_" + timestamp + ".png";
//        try {
//            // 2️⃣ Capture screenshot as FILE
//            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//            // 3️⃣ Copy to your folder
//            File destFile = new File(path);
//            org.apache.commons.io.FileUtils.copyFile(srcFile, destFile);
//            System.out.println("Screenshot saved :: " + path);
//            // 4️⃣ Capture Base64 for embedding in HTML
//            String base64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
//            return base64;
//        } catch (Exception e) {
//            System.err.println("Failed to capture screenshot: " + e.getMessage());
//            return null;
//        }
//    }
//
//    public static void initRunFolder() {
//        if (initFolder == null) {
//            // 1️⃣ Create timestamped report folder
//            String dateFolder = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
//            initFolder = System.getProperty("user.dir") + "/src/test/java/report/" + dateFolder + "/";
//            new File(initFolder).mkdirs();
//            System.out.println("Report folder created: " + initFolder);
//
//            // 2️⃣ Create subfolder for screenshots inside the report folder
//            String screenshotFolder = initFolder + "screenshots/";
//            new File(screenshotFolder).mkdirs();
//            System.out.println("Screenshot subfolder created: " + screenshotFolder);
//        }
//    }

}
