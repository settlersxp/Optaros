package step_definitions;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Hooks {
    public static WebDriver driver;
    public static HashMap<String, HashMap<String, String>> datamap = null;

    @Before
    /**
     * Delete all cookies at the start of each scenario to avoid
     * shared state between tests
     */
    public void openBrowser() throws MalformedURLException, InterruptedException {
        System.out.println("Called openBrowser");
        Path currentRelativePath = Paths.get("");


        String file = currentRelativePath.toAbsolutePath().toString() + "/src/test/resources/browsers";
        if (System.getProperty("browser") == null) {
            System.setProperty("browser", "chrome");
        }

        switch (System.getProperty("browser")) {
            case "chrome":
                String OS = System.getProperty("os.name").toLowerCase();
                DesiredCapabilities capabilities = DesiredCapabilities.chrome();

                if (OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0) {
                    System.setProperty("webdriver.chrome.driver", file + "/chromedriver-linux");
                } else if (OS.contains("mac")) {
                    System.setProperty("webdriver.chrome.driver", file + "/chromedriver");
                } else {
                    System.setProperty("webdriver.chrome.driver", file + "/chromedriver.exe");
                }

                if(System.getProperty("mobile") != null ){
                    Map<String, String> mobileEmulation = new HashMap<>();
                    mobileEmulation.put("deviceName", "Google Nexus 5");


                    Map<String, Object> chromeOptions = new HashMap<>();
                    chromeOptions.put("mobileEmulation", mobileEmulation);

                    capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                }

                driver = new ChromeDriver(capabilities);
                TimeUnit.SECONDS.sleep(1);
                break;

            case "firefox":
                System.setProperty("webdriver.firefox.driver", file + "/firefox");
                driver = new FirefoxDriver();
                break;

            case "html":

                break;
        }

        datamap = new HashMap<>();

        //map the environment variables
        HashMap<String, String> envVars = new HashMap<>();
        Properties staticInformation = readPropertiesFile("staticInformation");
        String envUrl;


        if (System.getProperty("staging") == null ||
                System.getProperty("staging").equalsIgnoreCase("production") ||
                System.getProperty("staging").equalsIgnoreCase("0")) {
            envUrl = "http://";
        } else {

            envUrl = "http://";
        }

        if(System.getProperty("mobile") != null ){
        }else{
            envVars.put("mobile", "false");
        }

        envVars.put("homepageURL", envUrl);
        datamap.put("envVars", envVars);

        //map the locators
        HashMap<String, String> locatorsMap = new HashMap<>();
        Properties locators = readPropertiesFile("locators");
        locatorsMap.putAll(putPropertiesValuesToMap(locators));
        datamap.put("locators", locatorsMap);

//        System.out.println("Current data" +datamap);

        if(driver!=null){
            driver.manage().deleteAllCookies();
            driver.manage().window().maximize();
        }


//        DeinDealHelper.clean_all_data_keys();
    }


    @After
    /**
     * Embed a screenshot in test report if test is marked as failed
     */
    public void embedScreenshot(Scenario scenario) {

        if (scenario.isFailed()) {
            try {
                scenario.write("Current Page URL is " + driver.getCurrentUrl());
//            byte[] screenshot = getScreenshotAs(OutputType.BYTES);
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.embed(screenshot, "image/png");
            } catch (WebDriverException somePlatformsDontSupportScreenshots) {
                System.err.println(somePlatformsDontSupportScreenshots.getMessage());
            }

        }
        driver.quit();

    }


    public Properties readPropertiesFile(String fileName) {
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
            assert input != null;
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return propFile;
    }

    public HashMap<String, String> putPropertiesValuesToMap(Properties properties, String stagingName) {
        HashMap<String, String> localMap = new HashMap<>();

        Enumeration e = properties.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            localMap.put(key, properties.getProperty(key).replace("{stagingName}", stagingName));
        }
        return localMap;
    }

    public HashMap<String, String> putPropertiesValuesToMap(Properties properties) {
        HashMap<String, String> localMap = new HashMap<>();

        Enumeration e = properties.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            localMap.put(key, properties.getProperty(key));
        }
        return localMap;
    }
}