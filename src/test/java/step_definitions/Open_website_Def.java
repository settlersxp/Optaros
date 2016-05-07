package step_definitions;

import actions.Open_website_actions;
import cucumber.api.java.en.Given;

/**
 * Created by gabi on 07/05/16.
 */
public class Open_website_Def extends StepDefHelper {

    public Open_website_Def() throws Throwable {
    }

    @Given("^I am on the google website$")
    public void iAmOnTheGoogleWebsite() throws Throwable {
        Open_website_actions.open_google(driver);
    }

}
