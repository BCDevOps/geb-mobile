package geb.mobile.driver

import groovy.util.logging.Slf4j
import io.appium.java_client.AppiumDriver
import io.selendroid.SelendroidCapabilities
import io.selendroid.SelendroidDriver
import org.openqa.selenium.Platform
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver
import org.uiautomation.ios.IOSCapabilities
import org.uiautomation.ios.client.uiamodels.impl.RemoteIOSDriver
import org.uiautomation.ios.communication.device.DeviceType

/**
 * Created by gmueksch on 12.08.14.
 */
@Slf4j
class GebMobileDriverFactory {

    public static String FRAMEWORK_APPIUM = "appium"
    public static String FRAMEWORK_SELENDRIOD = "selendroid"
    public static String FRAMEWORK_IOSDRIVER = "iosdriver"

    public static URL getURL(String url) {
        String seleniumUrl = System.getProperty("selenium.url")
        if (seleniumUrl) return new URL(seleniumUrl)
        else return new URL(url)
    }

    public static RemoteWebDriver createMobileDriverInstance() {
        if (useSelendroid()) {
            DesiredCapabilities capa
            if (appPackage() && appVersion())
                capa = new SelendroidCapabilities("${appPackage()}:${appVersion()}")
            else
                capa = DesiredCapabilities.android()

            def p = ~/^selendroid_(\w+)$/
            System.properties.each { k, v ->
                def m = p.matcher(k)
                if (m.matches()) {
                    try {
                        log.info("Setting set${m[0][1]}($v)")
                        capa."set${m[0][1]}"(v)
                    } catch (e) {
                        log.info("problem setting value to config: $e.message")
                    }
                }
            }
            return new SelendroidDriver(getURL("http://localhost:4444/wd/hub"), capa)
        } else if (useAppium()) {
            DesiredCapabilities capa = new DesiredCapabilities()
            capa.setCapability("platformName", "Android");
            //capa.setCapability("deviceName", "Android");
            //capa.setCapability("browserName", "Android")

            System.properties.each { String k, v ->
                def m = k =~ /^appium_(.*)$/
                if (m.matches()) {
                    log.info "setting appium property: $k , $v"
                    capa.setCapability(m[0][1], v)
                }
            }
            if( capa.getCapability("platformName") == "Android" ){
                capa.setCapability("appPackage", appPackage())
                capa.setCapability("platform", Platform.ANDROID)
            }


            if (capa.getCapability("automationName") == "selendroid") {
                log.info("Create SelendroidDriver for Appium, cause AutomationName is set to selendroid")
                return new SelendroidDriver(getURL("http://localhost:4723/wd/hub"), capa)
            }
            log.info("Create AppiumDriver ")
            def driver
            try {
                driver = new AppiumDriver(getURL("http://localhost:4723/wd/hub"), capa)
                sleep(1000)
                log.info("Driver created: $driver.capabilities")
                return driver
            } catch (e) {
                log.error("eXC: $e.message", e)
                if (e.message =~ /Android devices must be of API level 17 or higher/) {
                    capa.setCapability("automationName", "selendroid")
                    try {
                        driver = new SelendroidDriver(getURL("http://localhost:4723/wd/hub"), capa)
                    } catch (ex) {
                        log.error("Error:", ex)
                    }
                }
            }
            if (!driver) throw new RuntimeException("Appiumdriver could not be started")
        } else if (useIosDriver()) {
            DesiredCapabilities capa = new DesiredCapabilities()
            capa.setCapability(IOSCapabilities.BUNDLE_NAME, appPackage())
            if (appVersion())
                capa.setCapability(IOSCapabilities.BUNDLE_VERSION, appVersion())
            capa.setCapability(IOSCapabilities.DEVICE, DeviceType.iphone)

            System.properties.each { String k, v ->
                def m = k =~ /^iosdriver_(.*)$/
                if (m.matches()) {
                    log.info "setting ios property: $k , $v"
                    capa.setCapability(m[0][1], v)
                }
            }

            new RemoteWebDriver(getURL("http://localhost:5555/wd/hub/"), capa)

        } else {
            throw new Exception("Set Systemproperty 'framework' to selendroid or appium")
        }

    }

    public static boolean useAppium() {
        System.properties.framework == FRAMEWORK_APPIUM
    }

    public static boolean useSelendroid() {
        System.properties.framework == FRAMEWORK_SELENDRIOD
    }

    public static boolean useIosDriver() {
        System.properties.framework == FRAMEWORK_IOSDRIVER
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
    public static void setFrameWork(String framework, def map=null){
        System.setProperty("framework", framework )
        map?.each{k,v->
            if( k in  ['appUT.package', 'appUT.version'] ) System.setProperty(k,v)
            else System.setProperty("${framework}_${k}",v)
        }
    }

    /**
     * Convinient Method to set Framework and Capabilities for ...
     * @param map
     */
    public static void setIosDriver(def map){
        setFrameWork(FRAMEWORK_IOSDRIVER,map)
    }

    /**
     * Convinient Method to set Framework and Capabilities for ...
     * @param map
     */
    public static void setAppium(def map){
        setFrameWork(FRAMEWORK_APPIUM,map)
    }

    /**
     * Convinient Method to set Framework and Capabilities for ...
     * @param map
     */
    public static void setAppiumIos(def map){
        if( !map ) map = []
        map.platformName = 'iOS'
        setFrameWork(FRAMEWORK_APPIUM,map)
    }


    /**
     * Convinient Method to set Framework and Capabilities for ...
     * @param map
     */
    public static void setSelendroid(def map){
        setFrameWork(FRAMEWORK_SELENDRIOD,map)
    }



}
