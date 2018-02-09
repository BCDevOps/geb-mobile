package geb.mobile.driver

import groovy.util.logging.Slf4j
import io.appium.java_client.AppiumDriver
import io.appium.java_client.android.AndroidDriver
// import io.appium.java_client.android.AndroidUiautomator2Driver
import io.appium.java_client.ios.IOSDriver


import org.openqa.selenium.Platform

import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.LocalFileDetector
import org.openqa.selenium.remote.RemoteWebDriver


/*
The appium drivers:

iOS
    The XCUITest Driver
    (DEPRECATED) The UIAutomation Driver

Android
    (BETA) The Espresso Driver
    The UiAutomator2 Driver
    (DEPRECATED) The UiAutomator Driver
    (DEPRECATED) The Selendroid Driver

The Windows Driver (for Windows Desktop apps)

The Mac Driver (for Mac Desktop apps)
*/



@Slf4j
class GebMobileDriverFactory {

// no selendroid:


    public static String FRAMEWORK_APPIUM = "appium"
    // public static String FRAMEWORK_SELENDRIOD = "selendroid"
    // public static String FRAMEWORK_IOSDRIVER = "iosdriver"
    public static String FRAMEWORK_SELENIUM = "selenium"

    public static URL getURL(String url) {
        String appiumUrl = System.getProperty("appium.url")
        return new URL(appiumUrl)
        // here
        // if (appiumUrl) return new URL(appiumUrl)
        // else return new URL(url)
    }

    public static RemoteWebDriver createMobileDriverInstance() {
        log.info("Create Mobile Driver Instance for Framework ${System.properties.framework} ... ")

// Using appium:
        if (useAppium()) {
            DesiredCapabilities capa = new DesiredCapabilities()
            //set default platform to android:
            capa.setCapability("platformName", "Android");
            // get the capa setting defined by user:
            System.properties.each { String k, v ->
                def m = k =~ /^appium_(.*)$/
                if (m.matches()) {
                    log.info "setting appium property: $k , $v"
                    capa.setCapability(m[0][1], v)
                }
            }
            def driver
            // Android:
            if (capa.getCapability("platformName") == "Android") {
                capa.setCapability("platform", Platform.ANDROID)
                if( appPackage() ) capa.setCapability("appPackage", appPackage())

                // set default name to Android for emulator:
                if (!capa.getCapability("deviceName")) capa.setCapability("deviceName", "Android");


/*
                // Appium use UiAutomator2 instead of selendroid now: (https://appium.io/docs/en/drivers/android-uiautomator2/index.html)
                // for android native apps only!
                // here: Appium android with Espresso is coming! (https://github.com/appium/appium)
                if (capa.getCapability("automationName") == "UiAutomator2") {
                    log.info("Create Appium UiAutomator2-Driver (SelendroidDriver)")
                    // create new seesion:
                    // driver = new AndroidUiautomator2Driver();
                    // await driver.createSession(getURL("http://localhost:4723/wd/hub"), capa);
                    driver = new AndroidUiautomator2Driver(getURL("http://localhost:4723/wd/hub"), capa);
                    driver.setFileDetector(new LocalFileDetector())
                    sleep(1000)
                    log.info("Driver created: $driver.capabilities")
                    return driver
                }
*/                

                log.info("Create Appium AndroidDriver")
                try {
                    driver = new AndroidDriver(getURL("http://localhost:4723/wd/hub"), capa)
                    driver.setFileDetector(new LocalFileDetector())
                    sleep(1000)
                    log.info("Driver created: $driver.capabilities")
                    return driver
                } catch (e) {
                    //
                    log.error("eXC: $e.message", e)
                    if (e.message =~ /Android devices must be of API level 17 or higher/) {
                        log.error("Do not support Android API lower than 17.")
                        // if API lower than 17, use selendroid, for now we do not want to support this:
                        // capa.setCapability("automationName", "selendroid")
                        // try {
                        //     driver = new SelendroidDriver(getURL("http://localhost:4723/wd/hub"), capa)
                        //     driver.setFileDetector(new LocalFileDetector())
                        // } catch (ex) {
                        //     log.error("Error:", ex)
                        // }
                    }
                }
            }
            // iOS:
            else {
                // Appium use XCUITest Driver instead of UIAutomation Driver now, for apps of iOS 9.3 and up:
                // if (capa.getCapability("automationName") == "XCUITest") {
                    // log.info("Create Appium XCUITest-Driver")
                    // need to set additional capa:
                    // desired_caps['udid'] = udid
                    // desired_caps['automationName'] = 'XCUITest'
                    // desired_caps['realDeviceLogger'] = 'idevicesyslog'
                    // driver = new webdriver.Remote('http://0.0.0.0:4723/wd/hub', capa)
                // }

                log.info("Create Appium IOSDriver")
                driver = new IOSDriver(getURL("http://localhost:4723/wd/hub"), capa)
                driver.setFileDetector(new LocalFileDetector())
                sleep(1000)
                log.info("Driver created: $driver.capabilities")
                return driver
            }
            if (!driver) throw new RuntimeException("Appium Driver could not be started")
        } 

// Selenium:
        else if (System.properties.framework == FRAMEWORK_SELENIUM) {
            DesiredCapabilities capa = DesiredCapabilities.firefox()
            System.properties.each { String k, v ->
                def m = k =~ /^selenium_(.*)$/
                if (m.matches()) {
                    log.info "setting ios property: $k , $v"
                    capa.setCapability(m[0][1], v)
                }
            }
            def selenium = new RemoteWebDriver(getURL("http://localhost:4444/wd/hub/"),capa)
            selenium.setFileDetector(new LocalFileDetector())
            return selenium
        } 

// Not able to identify:
        else {
            throw new Exception("Set Systemproperty 'framework' to Selenium or Appium")
        }

    }

    public static boolean useAppium() {
        System.properties.framework == FRAMEWORK_APPIUM
    }
    public static String appPackage() {
        System.properties.'appUT.package'
    }

    public static String appVersion() {
        System.properties.'appUT.version'
    }

    /*  Test Helper Methods */
    /**
     *
     * @param framework
     * @param map the capabilities to add
     */
    public static void setFrameWork(String framework, def map = null) {
        System.setProperty("framework", framework)
        map?.each { k, v ->
            def key = "${framework}_${k}"
            if (k in ['appUT.package', 'appUT.version']) System.setProperty(k, v)
            else if(System.getProperty(key,null)==null) System.setProperty(key, v)
        }
    }

// for now, this should not be used. User should specify the targeted capability of drivers:
    /**
     * Convinient Method to set Framework and Capabilities for ...
     * @param map
     */
    public static void setAppium(def map) {
        setFrameWork(FRAMEWORK_APPIUM, map)
    }

    public static void setAppiumAndroid(def map = []){
        map.platformName = map.platformName ?: 'Android'
        map.appActivity = map.appActivity ?: "MainActivity"
        setAppium(map)
    }

    public static void setAppiumIos(def map) {
        if (!map) map = []
        map.platformName = map.platformName ?: 'iOS'
        map.deviceName = map.deviceName ?: 'iPhone 6'
        setFrameWork(FRAMEWORK_APPIUM, map)
    }

}
