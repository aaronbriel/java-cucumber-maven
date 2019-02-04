package common;

/**
 * Created by aaronbriel on 9/17/16.
 */

public class Config {

    private static final String BROWSER = System.getProperty("browser", Constants.CHROME_BROWSER);
    private static final String IS_REMOTING = System.getProperty("isRemoting", "false");
    private static final String IS_SAUCELABS = System.getProperty("isSaucelabs", "false");
    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";
    private static final String SITE_REGION = System.getProperty("siteRegion", null);
    private static final String BASE_URL = System.getProperty("baseUrl", "localhost");
    private static final String DATA_SET = System.getProperty("dataSet", Constants.QA);
    //NOTE: Android version 5.1 and before use "Browser", for above use "Chrome"
    private static final String ANDROID_BROWSER = System.getProperty("androidBrowser", "Chrome");
    //NOTE: Device name for droid can be found by running ../android_sdk/platform_tools/abd devices
    private static final String DEVICE_NAME = System.getProperty("deviceName", "emulator-5554");
    private static final String UDID = System.getProperty("udid", "PLEASE SPECIFIY -Dudid");
    private static final String SAUCE_USERNAME = System.getenv("SAUCE_USERNAME");
    private static final String SAUCE_ACCESS_KEY = System.getenv("SAUCE_ACCESS_KEY");
    private static final String HMC_PATH = "/hmc/hybris";
    private static final String SAUCELABS_SAFARI_PLATFORM = "OS X 10.11";
    private static final String SAUCELABS_SAFARI_VERSION = "10.0";
    private static final String SAUCELABS_IE_PLATFORM = "Windows 7";
    private static final String SAUCELABS_IE_VERSION = "11.0";
    private static final String SAUCELABS_CHROME_PLATFORM = "Windows 7";
    private static final String SAUCELABS_CHROME_VERSION = "54.0";
    private static final String SAUCELABS_FIREFOX_PLATFORM = "Windows 10";
    private static final String SAUCELABS_FIREFOX_VERSION = "45.0";

    public static String getBaseUrl() {
        return HTTP + BASE_URL;
    }

    public static String getBrowser() {
        return BROWSER;
    }

    public static Boolean getIsRemoting() {
        return Boolean.parseBoolean(IS_REMOTING);
    }

    public static Boolean getIsSaucelabs() {
        return Boolean.parseBoolean(IS_SAUCELABS);
    }

    public static String getSaucelabsBrowser() {
        return System.getenv("SELENIUM_BROWSER");
    }

    public static String getSaucelabsBrowserVersion() {
        return System.getenv("SELENIUM_VERSION");
    }

    public static String getSaucelabsPlatform() { return System.getenv("SELENIUM_PLATFORM"); }

    public static String getSaucelabsDevice() {
        return System.getenv("SELENIUM_DEVICE");
    }

    public static String getSauceUsername() {
        return SAUCE_USERNAME;
    }

    public static String getSauceAccessKey() {
        return SAUCE_ACCESS_KEY;
    }

    public static String getDeviceName() { return DEVICE_NAME; }

    public static String getUdid() {
        return UDID;
    }

    public static String getAndroidBrowser() {
        return ANDROID_BROWSER;
    }

    public static String getSeleniumVersion() { return System.getenv("SELENIUM_POM_VERSION"); }


    /**
     * Checks if browser parameter is a mobile phone or tablet
     *
     * @return boolean Whether browser is a mobile phone or tablet
     */
    public static boolean isMobile() {
        boolean isMobile = false;
        if (isAndroidPhone() ||
            isAndroidTablet() ||
            isIphone() ||
            isIpad())
            isMobile = true;
        return isMobile;
    }

    /**
     * Checks if browser parameter is a mobile phone
     *
     * @return boolean Whether browser is a mobile phone
     */
    public static boolean isMobilePhone() {
        boolean isMobilePhone = false;
        if (getBrowser().equalsIgnoreCase(Constants.ANDROID_PHONE) ||
            isIphone())
            isMobilePhone = true;
        return isMobilePhone;
    }

    /**
     * Checks if current browser is Safari
     *
     * @return Boolean
     */
    public static boolean isSafari() {
        return getBrowser().equalsIgnoreCase(Constants.SAFARI_BROWSER);
    }

    /**
     * Checks if current browser is Chrome
     *
     * @return Boolean
     */
    public static boolean isChrome() {
        return getBrowser().equalsIgnoreCase(Constants.CHROME_BROWSER);
    }

    /**
     * Checks if current browser is Fireofox
     *
     * @return Boolean
     */
    public static boolean isFirefox() { return getBrowser().equalsIgnoreCase(Constants.FIREFOX_BROWSER); }

    /**
     * Checks if current browser is Internet Explorer
     *
     * @return Boolean
     */
    public static boolean isIe() {
        return getBrowser().equalsIgnoreCase(Constants.IE_BROWSER);
    }

    /**
     * Checks if current browser is iphone
     *
     * @return Boolean
     */
    public static boolean isIphone() {
        return getBrowser().equalsIgnoreCase(Constants.IPHONE);
    }

    /**
     * Checks if current browser is ipad
     *
     * @return Boolean
     */
    public static boolean isIpad() {
        return getBrowser().equalsIgnoreCase(Constants.IPAD);
    }

    /**
     * Checks if current browser is an Android phone
     * @return Boolean
     */
    public static boolean isAndroidPhone() { return getBrowser().equalsIgnoreCase(Constants.ANDROID_PHONE); }

    /**
     * Checks if current browser is an Android tablet
     * @return Boolean
     */
    public static boolean isAndroidTablet() { return getBrowser().equalsIgnoreCase(Constants.ANDROID_TABLET); }


    /**
     * Returns the saucelabs url
     *
     * @return url      The saucelabs url
     */
    public static String getSaucelabsUrl() {
        return "http://" + SAUCE_USERNAME + ":" + SAUCE_ACCESS_KEY + "@ondemand.saucelabs.com:80/wd/hub";
    }

    /**
     * Returns the default saucelabs platform when running locally
     *
     * @return platform     The default saucelabs platform based on browser
     */
    public static String getDefaultSaucelabsPlatform() {
        String platform = null;
        if (isSafari()) {
            platform = SAUCELABS_SAFARI_PLATFORM;
        } else if (isChrome()) {
            platform = SAUCELABS_CHROME_PLATFORM;
        } else if (isFirefox()) {
            platform = SAUCELABS_FIREFOX_PLATFORM;
        } else if (isIe()) {
            platform = SAUCELABS_IE_PLATFORM;
        }
        return platform;
    }

    /**
     * Returns the default saucelabs browser version when running locally
     *
     * @return version     The default saucelabs browser version based on browser
     */
    public static String getDefaultSaucelabsVersion() {
        String version = null;
        if (isSafari()) {
            version = SAUCELABS_SAFARI_VERSION;
        } else if (isChrome()) {
            version = SAUCELABS_CHROME_VERSION;
        } else if (isFirefox()) {
            version = SAUCELABS_FIREFOX_VERSION;
        } else if (isIe()) {
            version = SAUCELABS_IE_VERSION;
        }
        return version;
    }

}