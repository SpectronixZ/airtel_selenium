package in.co.webdriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class driverFactory {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();

    public static void initDriver(String browser) {
        // Pick browser from system property OR fallback to parameter OR default "chrome"
        String b = System.getProperty("browser", browser != null ? browser : "chrome");

        if (b == null || b.isEmpty()) {
            throw new IllegalArgumentException("No browser specified! Use -Dbrowser=<chrome|firefox|edge>");
        }

        switch (b.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
//                chromeOptions.addArguments("--headless=new", "--disable-gpu", "--no-sandbox");
                chromeOptions.addArguments("--ignore-certificate-errors");
                driver.set(new ChromeDriver(chromeOptions));
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--headless");
                driver.set(new FirefoxDriver(firefoxOptions));
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--headless=new", "--disable-gpu", "--no-sandbox");
                driver.set(new EdgeDriver(edgeOptions));
                break;

            default:
                throw new IllegalArgumentException("Browser not supported: " + b);
        }

        getDriver().manage().window().maximize();
    }

    public static WebDriver getDriver() {
        return driver.get(); // just return current thread's driver
    }

    public static boolean openUrl(String url) {
        WebDriver webDriver = getDriver();
        try {
            webDriver.get(url);

            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
            wait.until(driver -> ((JavascriptExecutor) driver)
                    .executeScript("return document.readyState").equals("complete"));

            String pageSource = webDriver.getPageSource();
            String title = webDriver.getTitle();

            if (pageSource.contains("This site can’t be reached") ||
                    pageSource.contains("ERR_CONNECTION_REFUSED") ||
                    title.equals("") ||
                    title.equalsIgnoreCase("chrome-error://chromewebdata/")) {
                System.out.println("Site not reachable: " + url);
                return false;
            } else {
                System.out.println("Site loaded successfully: " + url);
                return true;
            }

        } catch (Exception e) {
            System.out.println("Unable to reach site: " + e.getMessage());
            return false;
        }
    }

    public static void quitDriver() {
        if(driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }


}
