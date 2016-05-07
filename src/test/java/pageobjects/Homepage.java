package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Homepage extends BaseClass {
    public Homepage(WebDriver driver) {
        super(driver);
    }

    public static class Search_bar{
        public static WebElement input(){
            return driver.findElement(By.cssSelector("#lst-ib"));
        }
    }
}