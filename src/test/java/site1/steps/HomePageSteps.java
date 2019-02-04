package site1.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import site1.pages.HomePage;
import utilities.Driver;

/**
 * Created by aaronbriel on 5/18/17.
 */
public class HomePageSteps {

    private HomePage homePage;

    public HomePageSteps(Driver driver) {
        homePage = new HomePage(driver);
    }

    @Given("^I go to the site1 homepage$")
    public void iGoToHome() throws Throwable {
        homePage.goToHome();
    }

    @When("^I assert that username \"(.*?)\" is displayed$")
    public void iAssertUsename(String username) throws Throwable {
        homePage.assertUsername(username);
    }

}
