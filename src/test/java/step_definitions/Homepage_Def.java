package step_definitions;

import actions.Homepage_actions;
import cucumber.api.java.en.And;
import org.openqa.selenium.support.PageFactory;
import pageobjects.Homepage;

/**
 * Created by gabi on 07/05/16.
 */
public class Homepage_Def  extends StepDefHelper {

    public Homepage_Def() throws Throwable {
    }

    @And("^I search for \"([^\"]*)\"$")
    public void iSearchFor(String search_term_name) throws Throwable {
        PageFactory.initElements(driver, Homepage.class);
        Homepage_actions.search_for(search_term_name);
    }
}
