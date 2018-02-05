package geb.mobile.android.uiautomator.specification

import geb.mobile.GebMobileBaseSpec
import geb.mobile.android.uiautomator.activities.HomeScreenActivity
import spock.lang.Stepwise

/**
 * Created by gmueksch on 23.06.14.
 */
@Stepwise
class UIAutomatorNavigatorTestWithPagesSpec extends GebMobileBaseSpec {

    // Remove comments, when you want to run from inside an IDE or set the SystemProperties with -D on the Run-Configuration
    static{
        System.setProperty("appUT.package","io.selendroid.testapp")
        //System.setProperty("appium_app", new File(ClassLoader.getSystemResource("testapk/selendroid-test-app-0.9.0.apk").toURI()).absolutePath)
        System.setProperty("appium_deviceName" , "Android")
        //To run the test on a specific device
        //System.setProperty("appium_udid","<your device id from 'adb devices'> ")
        //Use Appium
        System.setProperty("framework","appium")
    }

    def "open test-app and enter text "() {
        given:
        at HomeScreenActivity
        when:
        myTextField << "selendroid"
        then:
        myTextField.text() =~ /[sS]elendroid/
    }

    
}
