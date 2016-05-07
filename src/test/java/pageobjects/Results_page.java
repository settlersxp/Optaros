package pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Results_page extends BaseClass {
    public Results_page(WebDriver driver) {
        super(driver);
    }

    public static class Search_bar {
        public static WebElement button() {
            return element(driver,"search_bar_button");
        }
    }
}