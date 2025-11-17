package in.co.pages;

import in.co.base.basepage;
import in.co.locators.b2c_locatortype;
import in.co.webdriver.driverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

public class loginPage {

    private WebDriver driver = driverFactory.getDriver();

    public void login(String username, String password) {
        // ✅ check if driver exists
        if (driver == null) {
            System.err.println("Driver is null. Cannot perform login.");
            return;
        }

        // ✅ check if page loaded (optional: check title or some unique element)
        try {
            if (driver.getTitle().isEmpty() || driver.getPageSource().contains("ERR_CONNECTION_REFUSED")) {
                System.err.println("Site is unreachable. Skipping login.");
                return;
            }
        } catch (Exception e) {
            System.err.println("Exception while checking page: " + e.getMessage());
            return;
        }

        int attempts = 0;
        while (attempts < 3) {
            try {
                basepage.sendkeys(By.xpath(b2c_locatortype.usr_xpath), username);
                basepage.sendkeys(By.xpath(b2c_locatortype.pwd_xpath), password);
                basepage.click(By.xpath(b2c_locatortype.login_xpath));
                basepage.waituntilelementloads(By.xpath(b2c_locatortype.wait_login_xpath));
//                basepage.takeScreenShot("AfterLoginSuccess");
                System.out.println("Application login successfully!");
                return;
            } catch (StaleElementReferenceException e) {
                attempts++;
                System.err.println("Stale element, retrying... attempt " + attempts);
                if (attempts == 3) {
                    throw new RuntimeException("Login failed due to stale element even after retries!", e);
                }
            } catch (Exception e) {
                throw new RuntimeException("Exception during login: " + e.getMessage(), e);
            }
        }
    }
}


