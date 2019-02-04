package site1.pages;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

import java.util.logging.Logger;

/**
 * Created by aaronbriel on 5/18/17.
 */
public class CommonActions {

    private Driver driver;
    private WebDriver webDriver;
    private final Logger LOGGER = Logger.getLogger(CommonActions.class.getName());

    public CommonActions(Driver driver) {
        this.driver = driver;
        webDriver = driver.getDriver();
        PageFactory.initElements(webDriver, this);
    }


    /**
     * Asserts that the expected title is displayed on the page
     *
     * @param expectedTitle The expected title of the page
     */
    public void assertPageTitleContains(String expectedTitle) {
        LOGGER.info("assertPageTitleContains started");
        String actualTitle = webDriver.getTitle();
        Assert.assertTrue("FAIL: Expected title to contain: \"" + expectedTitle + "\", " +
                        "Actual title: \"" + actualTitle + "\"",
                actualTitle.contains(expectedTitle));
        LOGGER.info("Confirmed that the page title contains \"" + expectedTitle + "\".");
        LOGGER.info("assertPageTitleContains completed");
    }

}
