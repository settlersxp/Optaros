package pageobjects;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public abstract class BaseClass {
    public static WebDriver driver;
    public static boolean bResult;
    public static Properties locators = readPropertiesFile("locators");

    public BaseClass(WebDriver driver) {
        BaseClass.driver = driver;
        BaseClass.bResult = true;
    }

    public static Properties readPropertiesFile(String fileName) {
        Properties propFile = new Properties();
        InputStream input = null;
        Path currentRelativePath = Paths.get("");

        String file = currentRelativePath.toAbsolutePath().toString() + "/src/test/resources/testData/" + fileName + ".properties";
        try {
            input = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // load a properties file
        try {
            Reader reader = null;
            if (input != null) {
                reader = new InputStreamReader(input, "UTF-8");
            }
            propFile.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return propFile;
    }

    public static By locator(String name) {
        return By.cssSelector(locators.getProperty(name));
    }

    public static WebElement element(WebDriver driver, String locator_key){
        String local_locator = locators.getProperty(locator_key);
        try {
            return driver.findElement(By.cssSelector(local_locator));
        } catch (NotFoundException e) {
            Assert.assertTrue("The element with the locator '" + locator_key + "' :-> '" + local_locator + "' was not found in " + driver.getCurrentUrl(), false);
            return null;
        }
    }

    public static WebElement element(WebElement parent_element, String locator_key){
        String local_locator = locators.getProperty(locator_key);
        try {
            return parent_element.findElement(By.cssSelector(locators.getProperty(locator_key)));
        } catch (NotFoundException e) {
            Assert.assertTrue("The element with the locator '" + locator_key + "' :-> '" + local_locator + "' was not found in " + driver.getCurrentUrl(), false);
            return null;
        }
    }

    public static WebElement element(WebElement parent_element, String locator_key, String string_to_replace_with){
        String local_locator = String.format(locators.getProperty(locator_key), string_to_replace_with);
        try {
            return parent_element.findElement(By.cssSelector(local_locator));
        } catch (NotFoundException e) {
            Assert.assertTrue("The element with the locator '" + locator_key + "' :-> '" + local_locator + "' was not found in " + driver.getCurrentUrl(), false);
            return null;
        }
    }

    public static WebElement element(WebDriver driver, String locator_key, String string_to_replace_with) {
        return driver.findElement(By.cssSelector(String.format(locators.getProperty(locator_key),string_to_replace_with)));
    }

    public static List<WebElement> elements(WebDriver driver, String locator_key){
        return driver.findElements(By.cssSelector(locators.getProperty(locator_key)));
    }

    public static List<WebElement> elements(WebElement parent_element, String locator_key){
        return parent_element.findElements(By.cssSelector(locators.getProperty(locator_key)));
    }

    public static List<WebElement> elements(WebElement parent_element, String locator_key, String string_to_replace_with){
        return parent_element.findElements(locator(String.format(locator_key,string_to_replace_with)));
    }

    public static List<WebElement> elements(WebDriver driver, String locator_key, String string_to_replace_with){
        return driver.findElements(locator(String.format(locator_key,string_to_replace_with)));
    }

    public static By locator(String name, String what_to_replace_with) {
        return By.cssSelector(String.format(locators.getProperty(name), what_to_replace_with));
    }

    public static String get_url(WebElement element) {
        return element.findElement(By.cssSelector("a")).getAttribute("href");
    }
}
