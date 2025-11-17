package in.co.locators;

import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.List;

public class b2c_locatortype {

    public static String usr_xpath = "//input[@class='login-input' and @type='text']";
    public static String pwd_xpath = "//input[@class='login-input' and @type='password']";
    public static String login_xpath = "//button[@class='login-button']";
//    public static String wait_login_xpath = "//main[@class='main-content']";
    public static String wait_login_xpath = "//div[@class='cygnet-content-layout']";
    public static String fault_submenu = "//div[@class='submenu-container open']//a";
    public static List<By> waits = Arrays.asList(
            By.xpath("//div[@class='cygnet-content-layout']"),
            By.xpath("//div[@class='cygnet-table-container']")
    );
}
