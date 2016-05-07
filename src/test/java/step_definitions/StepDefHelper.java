package step_definitions;

import org.openqa.selenium.WebDriver;

import java.util.HashMap;

/**
 * Created by gabi on 15/10/15.
 */
public class StepDefHelper {
    public WebDriver driver;
    public HashMap<String, HashMap<String, String>> datamap = null;
    public HashMap<String, String> envVars = null;


    public StepDefHelper() throws Throwable {
        this.driver = Hooks.driver;
        this.datamap = Hooks.datamap;
        this.envVars = Hooks.datamap.get("envVars");
    }

    void common_helper_across_all_steps() {
    }
}
