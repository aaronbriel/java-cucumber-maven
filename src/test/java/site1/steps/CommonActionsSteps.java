package site1.steps;

import site1.pages.CommonActions;
import cucumber.api.java.en.Then;
import utilities.Driver;

/**
 * Created by aaronbriel on 5/18/17.
 */
public class CommonActionsSteps {

    private CommonActions commonActions;

    public CommonActionsSteps(Driver driver) {
        commonActions = new CommonActions(driver);
    }

    @Then("^I am brought to the page with title that contains \"(.*?)\"$")
    public void i_am_brought_to_the_page_with_title(String title) throws Throwable {
        commonActions.assertPageTitleContains(title);
    }

}
