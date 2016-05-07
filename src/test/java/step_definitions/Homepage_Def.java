package step_definitions;

import cucumber.api.java.en.And;
import actions.Homepage_actions;
import org.openqa.selenium.support.PageFactory;
import pageobjects.Homepage;

import static step_definitions.Hooks.driver;

/**
 * Created by gabi on 07/05/16.
 */
public class Homepage_Def {

    @And("^I search for \"([^\"]*)\"$")
    public void iSearchFor(String search_term_name) throws Throwable {
        PageFactory.initElements(driver, Homepage.class);
        Homepage_actions.search_for(search_term_name);
    }
}
