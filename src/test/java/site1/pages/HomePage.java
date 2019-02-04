package site1.pages;

import common.Config;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

import java.util.logging.Logger;

/**
 * Created by aaronbriel on 5/18/17.
 */
public class HomePage {

    private Driver driver;
    private WebDriver webDriver;
    private final Logger LOGGER = Logger.getLogger(HomePage.class.getName());

    public HomePage(Driver driver) {
        this.driver = driver;
        webDriver = driver.getDriver();
        PageFactory.initElements(webDriver, this);
    }

    @FindBy(className = "vcard-username")
    public static WebElement username;


    /**
     * Navigates the page to the main home page, passed in as -DbaseUrl
     */
    public void goToHome() {
        LOGGER.info("goToHome started");
        driver.getUrl(Config.getBaseUrl());
        driver.waitForPageToLoad();
        LOGGER.info("goToHome completed");
    }


    /**
     * Verifies the username
     */
    public void assertUsername(String usernameValue) {
        LOGGER.info("assertUsername started");
        driver.waitForElementVisible(username);
        Assert.assertTrue("FAILURE: Actual username: \"" + username.getText()
                        + "\", expected username: \"" + usernameValue+ "\"!",
                username.getText().contains(usernameValue));
        LOGGER.info("Confirmed that \"" + usernameValue + "\" was listed as the username.");
        LOGGER.info("assertUsername completed");
    }

}
