package step_definitions;

import cucumber.api.java.en.Given;
import helpers.Open_website_actions;

import static step_definitions.Hooks.driver;

/**
 * Created by gabi on 07/05/16.
 */
public class Open_website_Def {
    @Given("^I am on the google website$")
    public void iAmOnTheGoogleWebsite() throws Throwable {
        Open_website_actions.open_google(driver);
    }

}
