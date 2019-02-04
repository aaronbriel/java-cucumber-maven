package utilities;

/**
 * Created by aaronbriel on 9/21/16.
 */

import common.Config;
import common.Constants;
import cucumber.api.Scenario;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class Driver {

    protected static WebDriver driver;
    private static final String BROWSER = Config.getBrowser();
    private final Logger LOGGER = Logger.getLogger(Driver.class.getName());

    private static final String HEADER = "header";
    private static final String FOOTER = "footer";
    private static final String FITMENT = "fitment";
    private static final String AUTO_HEADER = "auto-header-";
    private static final String AUTO_FOOTER = "auto-footer-";
    private static final String AUTO_FITMENT = "auto-fitment-";

    public Driver() {}

    public void initialize() {
        if (driver == null)
            createNewDriverInstance();
    }

    /**
     * Creates a new driver instance
     */
    private void createNewDriverInstance() {
        LOGGER.info("createNewDriverInstance started");
        driver = DriverFactory.getDriver(BROWSER);
        LOGGER.info("createNewDriverInstance completed");
    }

    /**
     * Returns the current driver
     *
     * @return driver
     */
    public WebDriver getDriver() {
        return driver;
    }

    /**
     * Quits the current driver session
     */
    public void destroyDriver() {
        LOGGER.info("destroyDriver started");
        driver.quit();
        driver = null;
        LOGGER.info("destroyDriver completed");
    }

    /**
     * Returns the remote session ID
     *
     * @return String
     */
    public String getRemoteSessionId() {
        return ((RemoteWebDriver) driver).getSessionId().toString();
    }

    /**
     * Sleeps for set time to allow for animation to complete.
     */
    public void waitForMilliseconds() {
        LOGGER.info("waitForMilliseconds started with DEFAULT wait time of \"1 millisecond\"");
        waitForMilliseconds(Constants.ONE_MILLISEC_WAIT);
        LOGGER.info("waitForMilliseconds completed with DEFAULT wait time of \"1 millisecond\"");
    }

    /**
     * Sleeps for passed in time.
     *
     * @param time The time to sleep
     */
    public void waitForMilliseconds(int time) {
        LOGGER.info("waitForMilliseconds started with wait time of \"" + time + "\" milliseconds");
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            LOGGER.info(e.toString());
        }
        LOGGER.info("waitForMilliseconds completed with wait time of \"" + time + "\" milliseconds");
    }

    /**
     * Waits until the element is clickable.
     * Note: Uses a max wait time of 30 seconds
     *
     * @param byElement The element to wait for
     */
    public void waitForElementClickable(By byElement) {
        waitForElementClickable(driver.findElement(byElement));
    }

    /**
     * Waits until the element is clickable.
     * Note: Uses a variable max wait time
     *
     * @param byElement The element to wait for
     * @param time      Integer representing the max wait time in seconds
     */
    public void waitForElementClickable(By byElement, int time) {
        waitForElementClickable(driver.findElement(byElement), time);
    }

    /**
     * Waits until the element is clickable.
     * Note: Uses a max wait time of 30 seconds
     *
     * @param element The element to wait for
     */
    public void waitForElementClickable(WebElement element) {
        waitForElementClickable(element, Constants.DEFAULT_SEC_WAIT);
    }

    /**
     * Waits specified number of seconds until the element is clickable.
     *
     * @param element The element to wait for
     * @param time    Integer representing the max wait time in seconds
     */
    public void waitForElementClickable(WebElement element, int time) {
        LOGGER.info("waitForElementClickable started.");
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

        Wait wait = new FluentWait(driver)
                .withTimeout(time, TimeUnit.SECONDS)
                .pollingEvery(Constants.ONE_SEC_WAIT, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e) {
            Assert.fail("FAIL: Element \"" + element + "\" NOT found in \"" + time
                    + "\" seconds! " + "(Full Stack Trace: " + e.toString() + ")");
        }

        resetImplicitWaitToDefault();
        LOGGER.info("waitForElementClickable completed and found element \"" + element + "\"");
    }

    /**
     * Waits until the by element is visible.
     *
     * @param elementBy The by element to wait for
     */
    public void waitForElementVisible(By elementBy) {
        waitForElementVisible(driver.findElement(elementBy), Constants.DEFAULT_SEC_WAIT);
    }

    /**
     * Waits until the element is visible.
     * Note: Uses default 30 second max wait time
     *
     * @param element The element to wait for
     */
    public void waitForElementVisible(WebElement element) {
        waitForElementVisible(element, Constants.DEFAULT_SEC_WAIT);
    }

    /**
     * Waits specified time in seconds for the element to be visible.
     *
     * @param element   The element to wait for
     * @param time      The amount of time to wait
     */
    public void waitForElementVisible(WebElement element, int time) {
        LOGGER.info("waitForElementVisible started looking for element.");
        driver.manage().timeouts().implicitlyWait(Constants.ZERO, TimeUnit.SECONDS);
        Wait wait = new FluentWait(driver)
                .withTimeout(time, TimeUnit.SECONDS)
                .pollingEvery(Constants.ONE_SEC_WAIT, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);

        try {
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            Assert.fail("FAIL: Element \"" + element + "\" NOT found in \"" + time
                    + "\" seconds! " + "(Full Stack Trace: " + e.toString() + ")");
        }
        resetImplicitWaitToDefault();
        LOGGER.info("waitForElementVisible completed and found element \"" + element + "\"");
    }

    /**
     * Waits until the element is not visible.
     *
     * @param element The element to wait for
     */
    public void waitForElementNotVisible(By element) {
        waitForElementNotVisible(element, Constants.DEFAULT_SEC_WAIT);
    }

    /**
     * Waits for a specified length of time (in seconds)until the element is not visible.
     *
     * @param element The element to wait for
     * @param time    Length of time in seconds to wait
     */
    public void waitForElementNotVisible(By element, int time) {
        LOGGER.info("waitForElementNotVisible started with wait time of \"" + time + "\" milliseconds");
        driver.manage().timeouts().implicitlyWait(Constants.ZERO, TimeUnit.SECONDS);

        Wait wait = new FluentWait(driver)
                .withTimeout(time, TimeUnit.SECONDS)
                .pollingEvery(Constants.ONE_SEC_WAIT, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(element));
        } catch (TimeoutException e) {
            Assert.fail("FAIL: Element \"" + element + "\" still visible after \"" + time + "\" seconds! " +
                    "(Full Stack Trace: " + e.toString() + ")");
        }

        resetImplicitWaitToDefault();
        LOGGER.info("waitForElementNotVisible completed looking for element NOT visible \""
                + element + "\" with wait time of \"" + time + "\" milliseconds");
    }

    /**
     * Waits until the execution of the javascript returns with true.
     *
     * @param javaScript       The javascript to be executed
     * @param timeOutInSeconds The time in seconds to wait until returning a failure
     * @return boolean
     */
    public boolean pollUntil(final String javaScript, int timeOutInSeconds) {
        LOGGER.info("pollUntil started with a max wait of \"" + timeOutInSeconds + "\" seconds");
        boolean jsCondition = false;
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            (new WebDriverWait(driver, timeOutInSeconds)).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driverObject) {
                    return (Boolean) ((JavascriptExecutor) driverObject).executeScript(javaScript);
                }
            });
            jsCondition = (Boolean) ((JavascriptExecutor) driver).executeScript(javaScript);
            driver.manage().timeouts().implicitlyWait(Constants.DEFAULT_SEC_WAIT, TimeUnit.SECONDS);
            return jsCondition;
        } catch (Exception e) {
            LOGGER.info("pollUntil error condition caught: " + e.toString());
        }
        LOGGER.info("pollUntil completed with a max wait of \"" + timeOutInSeconds + "\" seconds");
        return jsCondition;
    }

    /**
     * Wait for the Text to be present in the given element, regardless of being displayed or not.
     *
     * @param by    Selector of the given element, which should contain the text
     * @param text  The text we are looking for
     * @param time  The time in seconds to wait until returning a failure
     * @return boolean
     */
    public boolean waitForTextPresent(final By by, final String text, int time) {
        LOGGER.info("waitForTextPresent started with max wait of \"" + time + "\" seconds");

        boolean isPresent = true;
        driver.manage().timeouts().implicitlyWait(Constants.ZERO, TimeUnit.SECONDS);
        Wait wait = new FluentWait(driver)
                .withTimeout(time, TimeUnit.SECONDS)
                .pollingEvery(Constants.ONE_SEC_WAIT, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);

        try {
            wait.until(ExpectedConditions.textToBePresentInElementLocated(by, text));
        } catch (TimeoutException e) {
            isPresent = false;
        }
        resetImplicitWaitToDefault();
        LOGGER.info("waitForTextPresent completed - '" + text + "' was found");
        return isPresent;
    }

    /**
     * Waits until the element contains the specified attribute value
     *
     * @param element   The element to wait for
     * @param attribute The attribute to check
     * @param value     The attribute value to check for
     */
    public void waitForElementAttribute(WebElement element, String attribute, String value) {
        LOGGER.info("waitForElementAttribute started");
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        WebDriverWait wait = new WebDriverWait(driver, Constants.DEFAULT_SEC_WAIT);

        try {
            wait.until(ExpectedConditions.attributeContains(element, attribute, value));
        } catch (TimeoutException e) {
            Assert.fail("FAIL: Element \"" + element + "\" did not contain attribute \"" + attribute +
                    "\" value \"" + value + "\" in \"" + Constants.DEFAULT_SEC_WAIT
                    + "\" seconds! " + "(Full Stack Trace: " + e.toString() + ")");
        }

        driver.manage().timeouts().implicitlyWait(Constants.DEFAULT_SEC_WAIT, TimeUnit.SECONDS);
        LOGGER.info("waitForElementAttribute completed and found element \"" + element + "\"");
    }

    /**
     * Grabs a list of elements and returns the one with the specific attribute text
     *
     * @param  element    By selector of element
     * @param  attribute  Attribute of element
     * @param  text       Text element should contain
     * @return WebElement The WebElement to return
     */
    public WebElement getElementWithAttribute(By element, String attribute, String text) {
        LOGGER.info("getElementWithAttribute started");

        List<WebElement> objects = driver.findElements(element);
        WebElement returnElement = null;

        for (WebElement object : objects) {
            if (object.getAttribute(attribute).equalsIgnoreCase(text)) {
                LOGGER.info("String '" + text + "' matched with rendered  ==>"
                        + object.getAttribute(attribute));
                returnElement = object;
            }
        }

        LOGGER.info("getElementWithAttribute completed");
        return returnElement;
    }


    /**
     * Returns element among multiple that contains text substring, if substring not found
     * returns null
     *
     * @param elementBy The By element to build list with
     * @param value     The value to search for with element
     * @return WebElement
     */
    public WebElement getElementWithText(By elementBy, String value) {
        LOGGER.info("getElementWithText started looking for element \"" +
                elementBy + "\" with value \"" + value);
        WebElement returnElement = null;
        try {
            List<WebElement> elements = driver.findElements(elementBy);
            for (WebElement element : elements) {
                if (element.getText().toLowerCase().contains(value.toLowerCase())) {
                    returnElement = element;
                }
            }
        } catch (NoSuchElementException e) {
            returnElement = null;
        }
        LOGGER.info("getElementWithText completed looking for element \"" +
                elementBy + "\" with value \"" + value + "\"");
        return returnElement;
    }

    /**
     * Returns element among multiple that contains text substring, if substring not found
     * returns null
     *
     * @param elementBy The By element to build list with
     * @param value     The value to search for with element
     * @return WebElement
     */
    public List<WebElement> getElementsWithText(By elementBy, String value) {
        LOGGER.info("getElementsWithText started looking for element \"" +
                elementBy + "\" with value \"" + value);
        List <WebElement> returnElements = new ArrayList<WebElement>();
        try {
            List<WebElement> elements = driver.findElements(elementBy);
            for (WebElement element : elements) {
                if (element.getText().toLowerCase().contains(value.toLowerCase())) {
                    returnElements.add(element);
                }
            }
        } catch (NoSuchElementException e) {
            returnElements = null;
        }
        LOGGER.info("getElementsWithText completed looking for element \"" +
                elementBy + "\" with value \""
                + value);
        return returnElements;
    }

    /**
     * Returns element among multiple that contains matching text
     * returns null
     *
     * @param elementBy The By element to build list with
     * @param value     The text to search for with element
     * @return WebElement
     */
    public WebElement getElementWithMatchingText(By elementBy, String value) {
        LOGGER.info("getElementWithMatchingText started looking for element with value \"" + value);
        WebElement returnElement = null;
        try {
            List<WebElement> elements = driver.findElements(elementBy);
            for (WebElement element : elements) {
                if (element.getText().toLowerCase().equalsIgnoreCase(value.toLowerCase())) {
                    returnElement = element;
                }
            }
        } catch (NoSuchElementException e) {
            returnElement = null;
        }
        LOGGER.info("getElementWithMatchingText completed looking for element \"" + elementBy +
                "\" with value \"" + value + "\"");
        return returnElement;
    }

    /**
     * Clicks an element via javascript executor
     *
     * @param element The element to click
     */
    public void jsClick(WebElement element) {
        LOGGER.info("jsClick started with element \"" + element + "\"");
        String mouseOverScript = "arguments[0].click();";
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(mouseOverScript, element);
        } catch (Exception e) {
            Assert.fail("FAIL: Clicking an element with JS executor FAILED with error: " + e);
        }
        LOGGER.info("jsClick completed with element \"" + element + "\"");
    }

    /***
     * Scrolls an element into view (bottom of element will align with bottom of page) via javascript executor
     * @param element The element to scroll into view
     */
    public void jsScrollToElement(WebElement element) {
        LOGGER.info("jsScrollToElement started with element \"" + element + "\"");
        String mouseOverScript = "arguments[0].scrollIntoView(false);";
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(mouseOverScript, element);
        } catch (Exception e) {
            LOGGER.info("jsScrollToElement encountered exception: " + e);
        }
        LOGGER.info("jsScrollToElement completed with element \"" + element + "\"");
    }

    /**
     * Moves/scrolls to passed element such that the bottom of the element is aligned with bottom of page and clicks
     * the element using JS
     *
     * @param element The element to move to and click
     */
    public void jsMoveToElementClick(WebElement element) {
        LOGGER.info("jsMoveToElementClick started with element \"" + element + "\"");
        jsScrollToElement(element);
        jsClick(element);
        LOGGER.info("jsMoveToElementClick completed with element \"" + element + "\"");
    }

    /**
     * Finds an element with By selector and text and clicks on element
     *
     * @param by                By selector of element with text
     * @param text              Text of element to click on
     */
    public void clickElementWithText(By by, String text) {
        LOGGER.info("clickElementWithText started");
        boolean found = false;
        try {

            if (Config.isMobile())
                waitForMilliseconds();

            List<WebElement> elements = driver.findElements(by);
            for (WebElement element : elements) {

                if (element.getText().toLowerCase().contains(text.toLowerCase())) {
                    found = true;
                    element.click();
                    break;
                }
            }
        } catch (NoSuchElementException e) {
            Assert.fail("FAIL: Element \"" + by.toString() + "\" NOT found! (Full Stack Trace: " + e.toString() + ")");
        }

        if (!found)
            Assert.fail("FAIL: Element \"" + by.toString() + "\" with text \"" + text + "\" NOT found!");
        LOGGER.info("clickElementWithText completed");
    }

    /**
     * Finds and clicks on a link using the text value
     *
     * @param linkText Text value of link to click
     */
    public void clickElementWithLinkText(String linkText) {
        LOGGER.info("clickElementWithLinkText started with linkText \"" + linkText + "\"");
        try {
            WebElement linkElement = null;
            if (!Config.isFirefox()) {
                linkElement = driver.findElement(By.linkText(linkText));
            } else {
                List<String> commonTags = new ArrayList<String>(Arrays.asList("a", "li", "h1", "div"));
                driver.manage().timeouts().implicitlyWait(Constants.ONE_SEC_WAIT, TimeUnit.SECONDS);
                for (String tag : commonTags) {
                    try {
                        linkElement = getElementWithText(By.tagName(tag), linkText);
                        if (linkElement != null) {
                            break;
                        }
                    } catch (Exception e) {
                        LOGGER.info("Could not find an element in Firefox by tag type \"" + tag
                                + "\" with text value \"" + linkText + "\"");
                    }
                }
            }
            resetImplicitWaitToDefault();
            jsScrollToElement(linkElement);
            linkElement.click();
        } catch (Exception e) {
            Assert.fail("FAIL: Clicking an element by link text value \"" + linkText + "\" FAILED with error: " + e);
        }
        LOGGER.info("clickElementWithLinkText completed with linkText \"" + linkText + "\"");
    }

    /**
     * Finds and clicks on a link using part of the text value
     *
     * @param linkText Partial text value of link to click
     */
    public void clickElementByPartialText(String linkText) {
        LOGGER.info("clickElementByPartialText started with linkText \"" + linkText + "\"");
        try {
            WebElement linkElement = driver.findElement(By.partialLinkText(linkText));
            waitForElementClickable(linkElement);
            linkElement.click();
        } catch (Exception e) {
            Assert.fail("FAIL: Clicking an element by partial link text value \"" + linkText + "\" FAILED with error: "
                    + e);
        }
        LOGGER.info("clickElementByPartialText started with linkText \"" + linkText + "\"");
    }

    /**
     * Scrolls an element into view before firing the click event. Useful when a modal or menu has options
     * listed outside current view such that they may be behind another element. This method will scroll them into
     * view and make a click collision between elements much less likely to occur.
     *
     * @param element The element to scroll into view on the page
     */
    public void jsScrollToElementClick(WebElement element) {
        LOGGER.info("jsScrollToElementClick started with element \"" + element + "\"");
        String scrollScript = "arguments[0].scrollIntoView(true);";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(scrollScript, element);
        waitForElementClickable(element);
        jsClick(element);
        LOGGER.info("jsScrollToElementClick completed with element \"" + element + "\"");
    }

    /**
     * Returns the last substring of a WebElement's text with delimiter
     *
     * @param element   The WebElement's text to pull the string from
     * @param delimiter The character preceding the string to extract
     * @return String
     */
    public String getLastSubstring(WebElement element, String delimiter) {
        return element.getText().substring(element.getText().lastIndexOf(delimiter) + 1);
    }

    /**
     * Gets the URL passed in
     *
     * @param url The url to go to
     */
    public void getUrl(String url) {
        LOGGER.info("getUrl started");
        waitForPageToLoad();
        try {
            driver.get(url);
        } catch (TimeoutException e) {
            LOGGER.info("Page load timed out, attempting refresh...");
            driver.navigate().refresh();
            waitForPageToLoad();
            driver.get(url);
        }

        LOGGER.info("getUrl completed");
    }

    /**
     * Checks if element (WebElement) is displayed
     *
     * @param element The element to check
     * @return boolean
     */
    public boolean isElementDisplayed(WebElement element) {
        return isElementDisplayed(element, null, Constants.FIVE_SEC_WAIT);
    }

    /**
     * Checks if element (WebElement) is displayed
     *
     * @param element   The element to check
     * @param time      The time to wait in checking for the element
     * @return boolean
     */
    public boolean isElementDisplayed(WebElement element, int time) {
        return isElementDisplayed(element, null, time);
    }

    /**
     * Checks if element (By Object) is displayed
     *
     * @param element The element to check
     * @return boolean
     */
    public boolean isElementDisplayed(By element) {
        return isElementDisplayed(null, element, Constants.FIVE_SEC_WAIT);
    }

    /**
     * Checks if element (By Object) is displayed
     *
     * @param element The element to check
     * @param time      The time to wait in checking for the element
     * @return boolean
     */
    public boolean isElementDisplayed(By element, int time) {
        return isElementDisplayed(null, element, time);
    }

    /***
     * Checks if element (web or by) is displayed
     * @param webElement The web element to check
     * @param byElement The by element to check
     * @param time Time to wait for element to display
     * @return Boolean on whether element is displayed
     */
    public boolean isElementDisplayed(WebElement webElement, By byElement, int time) {
        LOGGER.info("isElementDisplayed started with with MAX wait time of \"" + time + "\" seconds");
        driver.manage().timeouts().implicitlyWait(Constants.ZERO, TimeUnit.SECONDS);

        Wait wait = new FluentWait(driver)
                .withTimeout(time, TimeUnit.SECONDS)
                .pollingEvery(Constants.ONE_SEC_WAIT, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);

        if (webElement != null) {
            try {
                wait.until(ExpectedConditions.visibilityOf(webElement));
                resetImplicitWaitToDefault();
                return true;
            } catch (TimeoutException tse) {
                LOGGER.info("The WebElement: \"" + webElement + "\" was NOT displayed");
                resetImplicitWaitToDefault();
                return false;
            }
        } else {
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(byElement));
                resetImplicitWaitToDefault();
                return true;
            } catch (TimeoutException tse) {
                LOGGER.info("The By element locator: \"" + byElement + "\" was NOT displayed");
                resetImplicitWaitToDefault();
                return false;
            }
        }
    }

    /**
     * Embeds a screenshot into a scenario if it is failed
     *
     * @param scenario The scenario to check status of and embed screenshot if failed
     * @throws IOException General exception caught to allow for graceful failure
     */
    public void embedScreenshotOnFailure(Scenario scenario) throws IOException {
        LOGGER.info("embedScreenshotOnFailure started with scenario \"" + scenario + "\"");
        if (scenario.isFailed()) {
            try {
                if (Config.isIphone() || Config.isIpad()) {
                    //TODO: AB 3/8/17 implement screenshots for iphone ipad, may need to convert
                    //TODO (cont) File to Bytes to allow for embedding
                    LOGGER.info("Screenshots not yet implemented for iPhone/iPad!");
                    //WebDriver driver1 = new Augmenter().augment(driver);
                    //File file  = ((TakesScreenshot)driver1).getScreenshotAs(OutputType.FILE);
                    //FileUtils.copyFile(file, new File(scenario.getName() + ".jpg"));
                } else {
                    final byte[] screenshot = ((TakesScreenshot) driver)
                            .getScreenshotAs(OutputType.BYTES);
                    scenario.embed(screenshot, "image/png");
                }
                LOGGER.info("embedScreenshotOnFailure created and embedded screenshot for scenario \""
                        + scenario + "\"");
            } catch (WebDriverException somePlatformsDontSupportScreenshots) {
                LOGGER.info(somePlatformsDontSupportScreenshots.getMessage());
            }
        }
    }

    /**
     * Returns element with constructed autoClass name
     *
     * @param linkName The element to check
     * @param type     Page location used to determine auto class name
     * @return webElement
     */
    public WebElement constructAutoTagClassName(String linkName, String type) {
        LOGGER.info("constructAutoTagClassName started");
        WebElement autoClassName = null;
        String constructedLink = cleanStringOfSpaces(linkName);

        try {
            if (type.equalsIgnoreCase(HEADER)) {
                autoClassName = driver.findElement(By.className(AUTO_HEADER + constructedLink));
            } else if (type.equalsIgnoreCase(FOOTER)) {
                autoClassName = driver.findElement(By.className(AUTO_FOOTER + constructedLink));
            } else if (type.equalsIgnoreCase(FITMENT)) {
                autoClassName = driver.findElement(By.className(AUTO_FITMENT + constructedLink));
            }
        } catch (Exception e) {
            Assert.fail("FAIL: Constructing auto tag class name for element \"" + linkName + "\" of type \"" + type
                    + "\" FAILED with error: " + e);
        }
        LOGGER.info("constructAutoTagClassName completed");
        return autoClassName;
    }

    /**
     * Takes a string and removes any blank spaces inside
     *
     * @param stringToClean Text to eliminate spaces from
     * @return String
     */
    public String cleanStringOfSpaces(String stringToClean) {
        LOGGER.info("cleanStringOfSpaces started with string \"" + stringToClean + "\"");
        stringToClean = stringToClean.replaceAll("[\\s]", "-");
        LOGGER.info("cleanStringOfSpaces completed; string is now \"" + stringToClean.toLowerCase() + "\"");
        return stringToClean.toLowerCase();
    }

    /**
     * Uses string type and link text to find element
     * Clicks on found element
     *
     * @param type     What section of the page the link belongs to (header, footer, fitment)
     * @param linkText Text value of link to click
     */
    public void clickElementByAutoClassName(String type, String linkText) {
        LOGGER.info("clickElementByAutoClassName started");
        WebElement element = constructAutoTagClassName(linkText, type);
        //Handing intermittent 'cannot determine loading status' failures
        try {
            waitForElementClickable(element);
            element.click();
        } catch (Exception e) {
            waitForElementClickable(element);
            element.click();
        }
        LOGGER.info("AutoClassName " + linkText + " Clicked");
        LOGGER.info("clickElementByAutoClassName completed");
    }

    /**
     * Checks if text is displayed
     *
     * @param text The string text to check
     * @return boolean
     */
    public boolean
    isTextPresentInPageSource(String text) {
        return driver.getPageSource().contains(text);
    }

    /**
     * Simulates clicking the 'back' browser navigation button
     */
    public void browserNavigateBackAction() {
		LOGGER.info("browserNavigateBackAction started");
		try {
			if (Config.isSafari()) {
				((JavascriptExecutor) driver).executeScript("history.go(-1)");
			} else
				driver.navigate().back();
		} catch (Exception e) {
			Assert.fail("FAIL: Navigating back in the browser FAILED with error: " + e);
		}
		LOGGER.info("browserNavigateBackAction completed");
    }

    /**
     * Checks if element is found on page with provided attributes and text
     *
     * @param element   By selector of element
     * @param attribute Attribute of element
     * @param text      Text element should contain
     */
    public void assertElementAttributeString(By element, String attribute, String text) {
        LOGGER.info("assertElementAttributeString started");
        boolean StringFound = false;
        List<WebElement> objects = driver.findElements(element);
        int i = 0;
        for (WebElement object : objects) {
            if (object.getAttribute(attribute).contains(text)) {
                LOGGER.info("String '" + text + "' matched with rendered  ==>"
                        + object.getAttribute(attribute));
                StringFound = true;
                break;
            } else {
                i++;
            }
        }

        if (!StringFound) {
            Assert.fail("FAIL: String \"" + text + "\" NOT found!");
        }
        LOGGER.info("assertElementAttributeString completed");
    }

    /**
     * Checks if element contains text string
     *
     * @param element By selector for element
     * @param text    Text element should contain
     * @return Boolean
     */
    public boolean checkIfElementContainsText(By element, String text) {
        LOGGER.info("checkIfElementContainsText started");
        boolean found = false;
        List<WebElement> webElements = driver.findElements(element);
        for (WebElement webElement : webElements) {
            if (webElement.getText().contains(text)) {
                found = true;
                break;
            }
        }
        LOGGER.info("Confirmed that the element contains '" + text + "'.");
        LOGGER.info("checkIfElementContainsText completed");
        return found;
    }

    /**
     * Checks if element contains text string
     *
     * @param element WebElement to check
     * @param text    Text element should contain
     * @return Boolean
     */
    public boolean checkIfElementContainsText(WebElement element, String text) {
        LOGGER.info("checkIfElementContainsText started");
        boolean found = false;
        if (element.getText().contains(text)) {
            found = true;
        }
        LOGGER.info("Confirmed that the element contains '" + text + "'.");
        LOGGER.info("checkIfElementContainsText completed");
        return found;
    }

    /**
     * Returns By object
     *
     * @param byType  Type of By selector
     * @param element The element to get the By for
     * @return By
     */
    public By getBy(String byType, String element) {
        LOGGER.info("getBy started");
        By by = null;

        if (byType.equalsIgnoreCase(Constants.CLASS_NAME)) {
            by = By.className(element);
        } else if (byType.equalsIgnoreCase(Constants.CSS)) {
            by = By.cssSelector(element);
        } else if (byType.equalsIgnoreCase(Constants.ID)) {
            by = By.id(element);
        } else if (byType.equalsIgnoreCase(Constants.NAME)) {
            by = By.name(element);
        } else if (byType.equalsIgnoreCase(Constants.TAG_NAME)) {
            by = By.tagName(element);
        } else if (byType.equalsIgnoreCase(Constants.XPATH)) {
            by = By.xpath(element);
        } else if (byType.equalsIgnoreCase(Constants.LINK_TEXT)) {
            by = By.linkText(element);
        } else if (byType.equalsIgnoreCase(Constants.PARTIAL_LINK_TEXT)) {
            by = By.partialLinkText(element);
        }

        LOGGER.info("getBy completed");
        return by;
    }

    /**
     * Scrolls to bottom of page
     */
    public void scrollToBottom() {
        LOGGER.info("scrollToBottom started");
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("scroll(0, 250);");
        LOGGER.info("scrollToBottom completed");
    }

    /**
     * Selects visible text value from provided dropdown By element
     *
     * @param by         The dropdown element with values
     * @param optionText Values to select from dropdown (By) element
     */
    public void selectFromDropdownByVisibleText(By by, String optionText) {
        LOGGER.info("selectFromDropdownByVisibleText started");
        WebElement selectEle = driver.findElement(by);
        Select mySelect = new Select(selectEle);
        jsScrollToElement(selectEle);
        mySelect.selectByVisibleText(optionText);
        LOGGER.info("selectFromDropdownByVisibleText completed");
    }

    /**
     * Selects visible text value from provided dropdown element
     *
     * @param dropDownEle The dropdown element with values
     * @param optionText  Values to select from dropdown element
     */
    public void selectFromDropdownByVisibleText(WebElement dropDownEle, String optionText) {
        LOGGER.info("selectFromDropdownByVisibleText started");
        Select mySelect = new Select(dropDownEle);
        jsScrollToElement(dropDownEle);
        mySelect.selectByVisibleText(optionText);
        LOGGER.info("selectFromDropdownByVisibleText completed");
    }

    /***
     * Double-clicks the By element control
     * @param byElement control to receive the double-click
     */
    public void doubleClickControl(By byElement) {
        doubleClickControl(driver.findElement(byElement));
    }

    /***
     * Double-clicks either a non-null By element or else a WebElement control
     * @param webElement null OR control to receive the double-click
     */
    public void doubleClickControl(WebElement webElement) {
        LOGGER.info("doubleClickControl started");
        Actions action = new Actions(driver);
        jsScrollToElement(webElement);
        action.doubleClick(webElement).build().perform();
        LOGGER.info("doubleClickControl completed");
    }

    /***
     * Resets the implicit wait to the default value of the framework i.e. 30 seconds
     */
    public void resetImplicitWaitToDefault() {
        driver.manage().timeouts().implicitlyWait(Constants.DEFAULT_SEC_WAIT, TimeUnit.SECONDS);
    }

    /**
     * Returns the displayed element when there are multiple with the same by value
     *
     * @param by    The by to create a list of elements with
     * @param time  The amount of time to wait for trying to get the element
     * @return  WebElement  The WebElement being displayed
     */
    public WebElement getDisplayedElement(By by, int time) {
        List<WebElement> webElements = driver.findElements(by);
        for(WebElement webElement : webElements) {
            if (isElementDisplayed(webElement, time)) {
                return webElement;
            }
        }
        return null;
    }

    /**
     * Returns the displayed element when there are multiple with the same by value
     *
     * @param   webElements The list of WebElements to check
     * @param time  The amount of time to wait for trying to get the element
     * @return  WebElement  The WebElement being displayed
     */
    public WebElement getDisplayedElement(List<WebElement> webElements, int time) {
        for(WebElement webElement : webElements) {
            if (isElementDisplayed(webElement, time)) {
                return webElement;
            }
        }
        return null;
    }

    /**
     * Waits for a specific class to be present on the page, using pollUntil
     *
     * @param   className   The name of the class to poll for
     * @param   time        The time to wait
     */
    public void waitForClassPresent(String className, int time) {
        String script = "return document.getElementsByClassName('" + className + "')[0] != null;";
        if (pollUntil(script, time)) {
            LOGGER.info("className " + className + " found.");
        } else {
            Assert.fail("FAIL: className " + className + " NOT found.");
        }
    }

    /**
     * Pulls the text value from a By element
     *
     * @param   by      The by element to pull the value from
     * @return  String  The extracted value
     */
    public String getByValue(By by) {
        String byString = by.toString();
        return byString.substring(byString.lastIndexOf(": ") + 2);
    }

    /**
     * Waits for page to load within default timeout
     */
    public void waitForPageToLoad() {
        pollUntil("return document.readyState.indexOf('complete')>-1;", Constants.DEFAULT_SEC_WAIT);
    }

    /**
     * Checks if an attribute is present in an element
     *
     * @param   element     The by element to check
     * @param   attribute   The attribute to look for
     * @return  boolean     Whether the attribute was present or not
     */
    public boolean isAttributePresent(WebElement element, String attribute) {
        LOGGER.info("isAttributePresent started");
        Boolean result = false;
        try {
            String value = element.getAttribute(attribute);
            if (value != null){
                result = true;
            }
        } catch (Exception e) {
            LOGGER.info("isAttributePresent threw error:" + e);
        }
        LOGGER.info("isAttributePresent started");
        return result;
    }


    /**
     * Gets the number of WebElements of the type specified
     *
     * @param   by  The By to create a list of WebElements from
     * @return  int The number of WebElements counted
     */
    public int getElementCount(By by) {
        waitForElementVisible(by);
        List<WebElement> webElements = driver.findElements(by);
        return webElements.size();
    }
}