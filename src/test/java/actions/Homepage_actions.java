package actions;

import pageobjects.Homepage;
import pageobjects.Results_page;

/**
 * Created by gabi on 07/05/16.
 */
public class Homepage_actions {
    public static void search_for(String search_term_name) {
        Homepage.Search_bar.input().sendKeys(search_term_name);
        Results_page.Search_bar.button().click();
    }
}
