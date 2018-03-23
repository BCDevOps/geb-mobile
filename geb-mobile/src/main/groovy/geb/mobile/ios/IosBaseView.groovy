package geb.mobile.ios

import geb.Page
import groovy.util.logging.Slf4j
import io.appium.java_client.ios.IOSDriver
import org.openqa.selenium.ScreenOrientation

/**
 * Created by gmueksch on 27.08.14.
 *
 *
 * Notes by Shelly on 22.03.2018
 *
 * Limitation on iOS devices:
 * 1. cannot get the activity/view name for current page -> 'at' cannot be used
 * 2. cannot bring up the general settings, such as the airplane mode
 * 3. cannot Lock screen
 *
 *
 *
 *
 */

@Slf4j
class IosBaseView extends Page{

    static content = {
    }

    void switchOrientation( orientation ) {
            if (driver instanceof IOSDriver) {
                if (orientation == ScreenOrientation.LANDSCAPE && driver.orientation != orientation) {
                    log.info("Switch orientation to $orientation")
                    executeTargetFunc('setDeviceOrientation(UIA_DEVICE_ORIENTATION_LANDSCAPELEFT)')

                } else if( orientation == ScreenOrientation.PORTRAIT && driver.orientation != orientation) {
                    executeTargetFunc('setDeviceOrientation(UIA_DEVICE_ORIENTATION_PORTRAIT)')
                }
            }
    }


    def executeTargetFunc( func ){
        try {
            ((IOSDriver) driver).findElementsByIosUIAutomation("var target = UIATarget.localTarget();target.$func")
        }catch(e){
            log.error("Error on target Function $func: $e.message")
        }
    }

}
