package utilities;

import common.Config;
import common.Constants;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by aaronbriel on 10/4/16.
 */
public class DriverFactory {

    private static final String APPIUM_URL = "http://127.0.0.1:4723/wd/hub";
    private static final String ANDROID_NATIVE_APP = "NATIVE_APP";

    private enum Browsers {
        ANDROID_PHONE,
        ANDROID_TABLET,
        IE,
        IPHONE,
        IPAD,
        FIREFOX,
        CHROME,
        SAFARI,
        NONE;

        /**
         * Creates browser name (IE, FIREFOX, CHROME, SAFARI)
         *
         * @return Browser instance
         */
        public static Browsers browserForName(String browser) throws IllegalArgumentException {
            for (Browsers b : values()) {
                if (b.toString().equalsIgnoreCase(browser)) {
                    return b;
                }
            }
            throw browserNotFound(browser);
        }

        private static IllegalArgumentException browserNotFound(String outcome) {
            return new IllegalArgumentException(("Invalid browser [" + outcome + "]"));
        }
    }

    /**
     * Returns current webDriver session based on browser name passed in
     *
     * @param browserInUse Browser name currently being used in the session
     * @return WebDriver instance
     */
    public static WebDriver getDriver(String browserInUse) {
        Browsers browser;
        WebDriver driver;

        if (browserInUse == null) {
            browser = Browsers.CHROME;
        } else {
            browser = Browsers.browserForName(browserInUse);
        }
        switch (browser) {
            case NONE:
                return null;
            case CHROME:
                driver = createChromeDriver();
                break;
            case SAFARI:
                driver = createSafariDriver();
                break;
            case IE:
                driver = createIEDriver();
                break;
            case IPHONE:
                driver = createiPhoneDriver();
                break;
            case IPAD:
                driver = createiPhoneDriver();
                break;
            case ANDROID_PHONE:
                driver = createAndroidDriver();
                break;
            case ANDROID_TABLET:
                driver = createAndroidDriver();
                break;
            case FIREFOX:
            default:
                driver = createFirefoxDriver();
                break;
        }
        addAllBrowserSetup(driver);
        return driver;
    }

    /**
     * Creates and returns instance of iPhone/iPad driver with desired capabilities
     *
     * @return iPhone or iPad driver
     */
    private static WebDriver createiPhoneDriver() {

        WebDriver driver = null;
        DesiredCapabilities capabilities = new DesiredCapabilities();

        if (Config.getIsRemoting()) {
            return getRemoteDriver(capabilities);
        } else if (Config.getIsSaucelabs()) {
            return getSaucelabsDriver(capabilities);
        } else {
            //Set -DdeviceName to "iPhone 6" or "iPad Air 2"
            capabilities.setCapability("deviceName", Config.getDeviceName());
            //TODO: Required for actual iPhone device
            //capabilities.setCapability("udid", Config.getUdid());
            //May be necessary to automatically start ios_webkit_debug_proxy
            //Command line is: ios_webkit_debug_proxy -c UDID:27753 -d
            //capabilities.setCapability("startIWDP", true);
            capabilities.setCapability("platformName", "iOS");
            capabilities.setCapability("platformVersion", "11.2");
            capabilities.setCapability("browserName", "safari");
            capabilities.setCapability("deviceOrientation", "portrait");
            capabilities.setCapability("automationName", "XCUITest");
            try {
                driver = new IOSDriver<WebElement>(new URL(APPIUM_URL),
                        capabilities);
            } catch (MalformedURLException e) {
                Assert.fail("iPhone/iPad url was invalid.");
            }
            return driver;
        }
    }


    /**
     * Creates and returns instance of Android driver with desired capabilities
     *
     * @return Android driver
     */
    private static WebDriver createAndroidDriver() {
        WebDriver driver = null;
        DesiredCapabilities capabilities = DesiredCapabilities.android();

        if (Config.getIsRemoting()) {
            return getRemoteDriver(capabilities);
        } else if (Config.getIsSaucelabs()) {
            return getSaucelabsDriver(capabilities);
        } else {
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
            capabilities.setCapability(CapabilityType.BROWSER_NAME, Config.getAndroidBrowser());
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, Config.getDeviceName());
            try {
                driver = new AndroidDriver<WebElement>(new URL(APPIUM_URL), capabilities);
            } catch (MalformedURLException e) {
                Assert.fail("Android device url was invalid.");
            }

            //TODO: For use on Android Emulators that encounter 'Welcome To Chrome' splash screen
            //handleAndroidEmulatorChromeSplashScreen(driver);
            return driver;
        }
    }

    /***
     * Clicks through the 'Welcome To Chrome' splash screen that appears on Android emulators
     * @param driver - driver object to cast as Appium driver to handle Chrome splash screen
     */
    private static void handleAndroidEmulatorChromeSplashScreen(WebDriver driver) {
        AppiumDriver appiumDriver = (AppiumDriver) driver;
        String context = appiumDriver.getContext();
        try {
            appiumDriver.context(ANDROID_NATIVE_APP);
            appiumDriver.findElement(By.id("com.android.chrome:id/terms_accept")).click();
            appiumDriver.findElement(By.id("com.android.chrome:id/negative_button")).click();
            appiumDriver.context(context);
        } catch (Exception e) {
            appiumDriver.context(context);
        }
    }

    /**
     * Creates and returns instance of Safari driver with desired capabilities
     *
     * @return Safari driver
     */
    private static WebDriver createSafariDriver() {

        DesiredCapabilities capabilities = DesiredCapabilities.safari();

        if (Config.getIsRemoting()) {
            return getRemoteDriver(capabilities);
        } else if (Config.getIsSaucelabs()) {
            return getSaucelabsDriver(capabilities);
        } else {
            //System.setProperty("webdriver.safari.driver", getDriverPath(Constants.SAFARI_BROWSER));
            SafariOptions safariOptions = new SafariOptions();
            safariOptions.setUseCleanSession(true);
            capabilities.setCapability(SafariOptions.CAPABILITY, safariOptions);

            //Used for new, built-in safaridriver for Safari 10
            //return new RemoteWebDriver(desiredCapabilities);

            //Used for SafariDriver version 2.48.0 (SafariDriver.safariextz), also saucelabs current version.
            //In resources/external, or http://selenium-release.storage.googleapis.com/index.html?path=2.48/
            //Installation:
            //1. Open Safari, Go to Preferences > Extensions, and drag and drop this plugin into it
            //2. Safari will ask you Are you sure you want to install the extension 'WebDriver'? Trust
            return new SafariDriver(capabilities);
        }
    }

    /**
     * Creates and returns instance of Internet Explorer driver with desired capabilities
     *
     * @return Internet Explorer driver
     */
    private static WebDriver createIEDriver() {

        DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();

        capabilities.setCapability("nativeEvents", false);
        capabilities.setCapability("ignoreZoomSetting", true);
        capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);

        if (Config.getIsRemoting()) {
            return getRemoteDriver(capabilities);
        } else if (Config.getIsSaucelabs()) {
            return getSaucelabsDriver(capabilities);
        } else {
            System.setProperty("webdriver.ie.driver", getDriverPath());
            return new InternetExplorerDriver(capabilities);
        }

    }

    /**
     * Creates and returns instance of Chrome driver with desired capabilities
     *
     * @return Chrome driver
     */
    private static WebDriver createChromeDriver() {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        if (Config.getIsRemoting()) {
            return getRemoteDriver(capabilities);
        } else if (Config.getIsSaucelabs()) {
            return getSaucelabsDriver(capabilities);
        } else {
            System.setProperty("webdriver.chrome.driver", getDriverPath());
            return new ChromeDriver();
        }
    }

    /**
     * Creates and returns instance of Firefox driver with desired capabilities
     *
     * @return Firefox driver
     */
    private static WebDriver createFirefoxDriver() {

        DesiredCapabilities capabilities = DesiredCapabilities.firefox();

        if (Config.getIsRemoting()) {
            return getRemoteDriver(capabilities);
        } else if (Config.getIsSaucelabs()) {
            capabilities.setCapability("seleniumVersion", Config.getSeleniumVersion());
            return getSaucelabsDriver(capabilities);
        } else {
            System.setProperty("webdriver.gecko.driver", getDriverPath());
            return new FirefoxDriver(capabilities);
        }
    }

    /**
     * Sets implicit wait and page load timeout for current session
     * Also maximizes the browser window
     *
     * @param driver Current instance of webDriver
     */
    private static void addAllBrowserSetup(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(Constants.DEFAULT_SEC_WAIT, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(Constants.ONE_MIN_WAIT, TimeUnit.SECONDS);
        //TODO: Maximize not supported in 2.48, remove once new safaridriver stable and leveraged
        if (!Config.getBrowser().equalsIgnoreCase(Constants.SAFARI_BROWSER) &&
                !Config.isMobile())
            driver.manage().window().maximize();
    }

    /**
     * Gets driver path of used driver on machine
     *
     * @return String of driver path on disk
     */
    private static String getDriverPath() {

        String osType = System.getProperty("os.name");
        String cwd = System.getProperty("user.dir");
        String driverPath = cwd + "/src/test/resources/external/";

        if (Config.getBrowser().equalsIgnoreCase(Constants.SAFARI_BROWSER)) {
            driverPath = driverPath + "SafariDriver";
        } else if (Config.getBrowser().equalsIgnoreCase(Constants.IE_BROWSER)) {
            driverPath = driverPath + "IEDriverServer.exe";
        } else if (Config.getBrowser().equalsIgnoreCase(Constants.CHROME_BROWSER)) {
            if (osType.contains("Windows")) {
                driverPath = driverPath + "chromedriver.exe";
            } else {
                driverPath = driverPath + "chromedriver";
            }
        } else if (Config.getBrowser().equalsIgnoreCase(Constants.FIREFOX_BROWSER)) {
            if (osType.contains("Windows")) {
                driverPath = driverPath + "geckodriver.exe";
            } else {
                driverPath = driverPath + "geckodriver";
            }
        }
        return driverPath;
    }

    /**
     * Returns the Saucelabs driver instance, first setting capabilities based on
     * whether this is from Saucelabs OnDemand in Jenkins or run locally
     *
     * @param capabilities The desired capabilities to bind to the driver instance
     * @return driver       The driver instance to return
     */
    private static WebDriver getSaucelabsDriver(DesiredCapabilities capabilities) {
        WebDriver driver = null;

        //For running saucelabs tests through jenkins OnDemand plugin
        if (Config.getSaucelabsBrowser() != null) {
            if (Config.isMobile()) {
                capabilities.setCapability("deviceName", Config.getSaucelabsDevice());
            }
            capabilities.setBrowserName(Config.getSaucelabsBrowser());
            capabilities.setVersion(Config.getSaucelabsBrowserVersion());
            capabilities.setCapability(CapabilityType.PLATFORM, Config.getSaucelabsPlatform());
        //For running saucelabs tests locally
        } else {
            if (Config.isMobile()) {
                setLocalMobileSaucelabsCapabilities(capabilities);
            } else {
                capabilities.setCapability("platform", Config.getDefaultSaucelabsPlatform());
                capabilities.setCapability("version", Config.getDefaultSaucelabsVersion());
            }
        }

        try {
            driver = new RemoteWebDriver(new URL(Config.getSaucelabsUrl()), capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return driver;
    }

    /**
     * Returns the remote driver instance
     *
     * @param capabilities The desired capabilities to bind to the driver instance
     * @return driver       The driver instance to return
     */
    private static WebDriver getRemoteDriver(DesiredCapabilities capabilities) {
        WebDriver driver = null;
        try {
            driver = new RemoteWebDriver(new URL(getRemoteUrl()), capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return driver;
    }

    /**
     * Returns the remote driver instance of the Selenium Grid
     *
     * @return remoteUrl       The remote URL based on browser
     */
    private static String getRemoteUrl() {
        String remoteUrl = null;
        if (Config.getBrowser().equalsIgnoreCase(Constants.FIREFOX_BROWSER)) {
            remoteUrl = Constants.FIREFOX_REMOTE;
        } else if (Config.getBrowser().equalsIgnoreCase(Constants.IE_BROWSER)) {
            remoteUrl = Constants.IE_REMOTE;
        } else if (Config.getBrowser().equalsIgnoreCase(Constants.CHROME_BROWSER)) {
            remoteUrl = Constants.CHROME_REMOTE;
        } else if (Config.getBrowser().equalsIgnoreCase(Constants.SAFARI_BROWSER)) {
            remoteUrl = Constants.SAFARI_REMOTE;
        } else {
            Assert.fail("There is currently no grid setup for " + Config.getBrowser() + ".");
        }
        return remoteUrl;
    }


    /**
     * Sets android saucelabs capabilities for local runs (vs Jenkins Sauce OnDemand)
     */
    private static void setLocalMobileSaucelabsCapabilities(DesiredCapabilities capabilities) {
        Assert.fail("You must set your local saucelabs mobile capabilities in " +
                "DriverFactory.setLocalMobileSaucelabsCapabilities");
        //Example 1 for actual Samsung galaxy device
        //capabilities.setCapability("appiumVersion", "1.5.3");
        //capabilities.setCapability("deviceName","Samsung Galaxy S6 Device");
        //capabilities.setCapability("deviceOrientation", "portrait");
        //capabilities.setCapability("browserName", "chrome");
        //capabilities.setCapability("platformVersion", "6.0");
        //capabilities.setCapability("platformName","Android");
        //Example 2 for Generic Android Emulator
        //capabilities.setCapability("appiumVersion", "1.5.3");
        //capabilities.setCapability("deviceName","Android Emulator");
        //capabilities.setCapability("deviceOrientation", "portrait");
        //capabilities.setCapability("browserName", "Browser");
        //capabilities.setCapability("platformVersion", "5.1");
        //capabilities.setCapability("platformName","Android");
        //Example 3 for iPhone or iPad
        //Set -DdeviceName command line parameter to "iPhone 6" or "iPad Air 2"
        //capabilities.setCapability("deviceName", Config.getDeviceName());
        //capabilities.setCapability("platformName", "iOS");
        //capabilities.setCapability("platformVersion", "9.3");
        //capabilities.setCapability("browserName", "safari");
        //capabilities.setCapability("deviceOrientation", "portrait");
    }

}
