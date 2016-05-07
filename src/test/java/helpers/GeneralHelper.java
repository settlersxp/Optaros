package helpers;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by gabi on 22/09/15.
 */
public class GeneralHelper {
    public static String[] split_URL_to_sections(String URL) {
        String[] listOfURLSections;
        listOfURLSections = URL.split("/");
        return listOfURLSections;
    }

    public static void scroll_to_element(WebElement incElement, WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", incElement);
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(incElement));
    }

    public static void color_element(WebElement selector, WebDriver driver) {
        String jsCode = "arguments[0].style[\"background-color\"] =\"red\"";
        ((JavascriptExecutor) driver).executeScript(jsCode, selector);
    }

    public static int rand_int(int min, int max) {
        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return rand.nextInt((max - min) + 1) + min;
    }

    public static WebElement get_random_web_element_for_locator_from_string(WebDriver driver, String locatorString) {
        List<WebElement> availableElements = driver.findElements(By.cssSelector(locatorString));
        if (availableElements.size() == 0) {
            Assert.assertTrue("The locator " + locatorString + " did not find any elements", false);
        }
        int id = rand_int(0, availableElements.size() - 1);
        return availableElements.get(id);
    }

    public static WebElement get_parent_element(WebElement elem) {
        WebElement parentElem = null;
        if (elem != null)
            parentElem = elem.findElement(By.xpath(".."));
        return parentElem;
    }

    public static StringBuilder perform_POST_API_call(String url) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }


        BufferedReader rd = null;
        try {
            assert response != null;
            rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder result = new StringBuilder();
        String line;
        try {
            assert rd != null;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean wait_for_stale_element(WebDriver driver, By by) {
        boolean result = false;
        int attempts = 0;
        while (attempts < 2) {
            try {
                driver.findElements(by);
                result = true;
                break;
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                System.out.println(e.getMessage());
            }
            attempts++;
        }
        return result;
    }

    public static String generate_string(int length) {
        String randomString = "";

        int i;
        Random r = new Random();
        for (i = 0; i < length; i++) {
            char c = (char) (r.nextInt(26) + 'a');
            randomString += c;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return randomString;
    }

    public static String generate_new_email_address() {
        String s1, s2, s3;
        s1 = generate_string(7);
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        Date date = new Date();
        s2 = dateFormat.format(date);
        s3 = "@mailinator.com";
        save_data("randomEmail", s1 + s2 + s3);
        return s1 + s2 + s3;
    }

    public static String generate_new_password(int length) {
        String result = generate_string(length);
        save_data("randomPassword", result);
        return result;
    }

    public static void set_properties(String fileName, String key, String value, String mode) {
        Properties props = read_properties_file("sendData");
        if (mode.equalsIgnoreCase("u")) {
            String existingKeys = get_data(key);
            if (existingKeys != null && !existingKeys.equals("")) {
                value = existingKeys + "," + value;
            }
            props.setProperty(key, value);
        } else if (mode.equalsIgnoreCase("n")) {
            props.setProperty(key, value);
        } else if (mode.equalsIgnoreCase("a")) {
            String existingKeys = get_data(key);
            if (existingKeys != null) {
                value = existingKeys + "," + value;
            }
            props.setProperty(key, value);
        } else {
            Assert.assertTrue("Implement the mode " + mode + " for set_properties", false);
        }
        save_properties_file(fileName, props);
    }

    private static void save_properties_file(String fileName, Properties props) {
        Path currentRelativePath = Paths.get("");

        String file = currentRelativePath.toAbsolutePath().toString() + "/src/test/resources/testData/" + fileName + ".properties";
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            assert outputStream != null;
            props.store(outputStream, "Save successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save_data(String key, String value) {
        set_properties("sendData", key, value, "n");
    }

    public static void reset_data(String key) {
        set_properties("sendData", key, "", "n");
    }

    public static void update_data(String key, String value) {
        set_properties("sendData", key, value, "u");
    }

    public static void update_data(String key, HashSet<String> value) {
        String final_value = String.join(",", value);
        set_properties("sendData", key, final_value, "u");
    }

    public static String get_data(String name) {
        String data = read_properties_file("sendData").getProperty(name);
        System.out.println("The data retrieved for " + name + " is " + data);
        return data;
    }

    public static Properties read_properties_file(String fileName) {
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

    public static void target_page_to_self(WebDriver driver, String id_element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.getElementById('" + id_element + "').setAttribute('target', '_self')");
    }

    public static void mouse_actions(WebDriver driver, WebElement element, String link) throws InterruptedException {
        Actions act = new Actions(driver);
        act.moveToElement(element).perform();
        while (!driver.findElement(By.linkText(link)).isDisplayed()) Thread.sleep(250);
        driver.findElement(By.linkText(link)).click();
    }

    public static void mouse_actions(WebDriver driver, String link1, String link2) throws InterruptedException {
        WebElement element = driver.findElement(By.linkText(link1));
        Actions act = new Actions(driver);
        act.moveToElement(element).perform();
        while (!driver.findElement(By.linkText(link1)).isDisplayed()) Thread.sleep(250);
        driver.findElement(By.linkText(link2)).click();
    }

    public static <T> T get_random_element_from_list(List<T> list_of_elements) {
        int number = rand_int(0, list_of_elements.size() - 1);
        return list_of_elements.get(number);
    }

    public static void clean_saved_data(String key, String value) {
        Properties initial_properties = read_properties_file("sendData");
        String[] values_to_be_used = value.split(",");

        //if the current key is stored in the file clean it
        initial_properties.setProperty(key, value);

        //set every key that will be used during the test equal to null so that the index not found errors will be avoided
        for (String value_to_be_used : values_to_be_used) {
            initial_properties.setProperty(value_to_be_used, "");
        }

        save_properties_file("sendData", initial_properties);
    }

    public static String get_localisation(String key) {
        Properties localisation = read_properties_file("localisation");
        return localisation.getProperty(key);
    }

    public static String get_error_message(String key, String language) {
        String final_key = key + "_" + language;
        Properties error_messages = read_properties_file("errorMessages");
        return error_messages.getProperty(final_key);
    }

    public static String perform_POST_API_call(String url, List<NameValuePair> params) {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);

        try {
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity respEntity = response.getEntity();

            if (respEntity != null) {
                // EntityUtils to get the response content
                return EntityUtils.toString(respEntity);
            }
        } catch (IOException e) {
            // writing exception to log
            e.printStackTrace();
        }
        return null;
    }

    public static String perform_GET_API_call(String url, List<NameValuePair> params) {
        if (!url.endsWith("?"))
            url += "?";

        String paramString = URLEncodedUtils.format(params, "utf-8");

        url += paramString;


        HttpClient httpclient = HttpClients.createDefault();
        HttpGet http_get = new HttpGet(url);
        try {
            HttpResponse response = httpclient.execute(http_get);
            HttpEntity respEntity = response.getEntity();

            if (respEntity != null) {
                // EntityUtils to get the response content
                return EntityUtils.toString(respEntity);
            }
        } catch (IOException e) {
            // writing exception to log
            e.printStackTrace();
        }
        return null;
    }

    public static String perform_PUT_API_call(String url, List<NameValuePair> params, List<NameValuePair> headers) {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPut httppost = new HttpPut(url);

        if (headers != null) {
            for (NameValuePair header : headers) {
                httppost.addHeader(header.getName(), header.getValue());
            }
        }

        if (params != null) {
            try {
                httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        try {
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity respEntity = response.getEntity();

            if (respEntity != null) {
                // EntityUtils to get the response content
                return EntityUtils.toString(respEntity);
            }
        } catch (IOException e) {
            // writing exception to log
            e.printStackTrace();
        }
        return null;
    }

    public static boolean is_centered_in(WebElement wrapper, WebElement element_under_test) {
        int space_between_elements = wrapper.getSize().getHeight() - element_under_test.getSize().getHeight();
        int total_position_difference = element_under_test.getLocation().getY() - wrapper.getLocation().getY();
        return (total_position_difference == space_between_elements / 2);
    }

    public static String get_localisation(String key, String current_language) {
        Properties localisation = read_properties_file("localisation");
        return localisation.getProperty(key + "_" + current_language);
    }

    /**
     * Validates the text of an element
     *
     * @param element          the webelement under test which contains the text
     * @param current_language DE or FR in lowercase
     * @param location         error for the errors file, "" for translations file
     * @param key              the identifier of the string in the file
     * @param validation_type  the text "start"s with the text in the file, "equal"s the text in the file, "end"s with the text in the file
     */
    public static void validate_content(WebElement element, String current_language, String location, String key, String validation_type) {
        String expected_text;
        if (location.equalsIgnoreCase("error")) {
            expected_text = get_error_message(key, current_language);
        } else {
            expected_text = get_localisation(key, current_language);
        }
        String actual_text = element.getText();

        if (validation_type.equalsIgnoreCase("start")) {
            Assert.assertTrue("The " + key + " element should start with the text '" + expected_text + "' but '" + actual_text + "' was found",
                    actual_text.startsWith(expected_text));
        } else if (validation_type.equalsIgnoreCase("equal")) {
            Assert.assertTrue("The " + key + " element should have the text " + expected_text + "' but '" + actual_text + "' was found",
                    actual_text.equals(expected_text));
        } else if (validation_type.equalsIgnoreCase("end")) {
            Assert.assertTrue("The " + key + " element should end with the text " + expected_text + "' but '" + actual_text + "' was found",
                    actual_text.endsWith(expected_text));
        }
    }

    public static String generate_new_mailinator_address(String environment_under_test) {
        String s1, s2, s3;
        s1 = generate_string(7);
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        Date date = new Date();
        s2 = dateFormat.format(date);
        s3 = s1 + s2 + "@mailinator.com";
        save_data("mailinator_user_" + environment_under_test, s1 + s2);
        save_data("mailinator_email_" + environment_under_test, s3);
        return s3;
    }

    public static String generate_number(int length) {
        long i;
        String randomString = "";
        Random r = new Random();
        for (i = 0; i < length; i++) {
            int c = r.nextInt(9);
            randomString += c;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return randomString;
    }

    public static String get_path_to_test_data_file(HashMap<String, String> envVars) {
        String path_to_home = System.getProperty("user.home");
//        System.out.println("user.home: "+path_to_home);
        String path_to_directory = path_to_home + System.getProperty("file.separator") + "testData2";
//        System.out.println("directory: "+ path_to_directory);
        new File(path_to_directory).mkdirs();

        String file_name = envVars.get("staging_collections_file_name");
        if (envVars.get("is_staging").equalsIgnoreCase("false")) {
            file_name = envVars.get("production_collections_file_name");
        }
        return path_to_directory + System.getProperty("file.separator") + file_name;
    }

    public static void clean_all_data_keys() {
        Path currentRelativePath = Paths.get("");
        String file_name = currentRelativePath.toAbsolutePath().toString() + "/src/test/resources/testData/sendData.properties";
        File send_data_file = new File(file_name);
        try {
            send_data_file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get_js_value(WebDriver driver, String variable_name) {
        Object o;
        try {
            o = ((JavascriptExecutor) driver).executeScript("return " + variable_name + ";");
        } catch (WebDriverException e) {
            return "";
        }
        if (o == null) {
            return "";
        } else {
            return o.toString();
        }
    }

    public static String run_js_script(WebDriver driver, String script) {
        Object o;
        try {
            o = ((JavascriptExecutor) driver).executeScript(script);
        } catch (WebDriverException e) {
            return "";
        }
        if (o == null) {
            return "";
        } else {
            return o.toString();
        }
    }

    public static <T> T last(List<T> list) {
        return list.get(list.size() - 1);
    }

    public static void validate_content_visible_correct(WebElement element, String content) {
        String expected_text = element.getText().trim();
        Assert.assertTrue("The expected text is " + expected_text + " but " + content + " was found.", expected_text.equals(content));
        Assert.assertTrue("The element with the text '" + content + "' should have been visible", element.isDisplayed());
    }

    public String array_to_string(String[] incomingArray) {
        StringBuilder result = new StringBuilder();
        for (String anIncomingArray : incomingArray) result.append(anIncomingArray);
        return result.toString();
    }
}
