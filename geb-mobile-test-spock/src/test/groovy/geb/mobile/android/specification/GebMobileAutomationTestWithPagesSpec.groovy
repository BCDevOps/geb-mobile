package geb.mobile.android.specification

import geb.mobile.GebMobileBaseSpec
import geb.mobile.android.activities.HomeScreenActivity
import geb.mobile.android.activities.CameraActivity

import spock.lang.Stepwise


/**
 * Created by gmueksch on 23.06.14.
 */



/**
 * Sample test case for android app on Wiki native app:
 * TODO: rename activity to match the app's current activity
 */
@Stepwise
class GebMobileAutomationTestWithPagesSpec extends GebMobileBaseSpec {

    def "open test-app and enter search string "() {
        given: "I land on Home screen"
        at HomeScreenActivity

        when: "I click on the search button"
        searchButton.click()

        and: "I type in search string and scroll down"
        searchText = "BrowserStack"
        swipeAction

        then: "I should see results"
        assert resultList.size() > 0
    }



//    def "test camera and take photo"() {
//        given: "open the camera"
//        sleep(3000)
//        at CameraActivity
//
//        when: "I enable the location request"
//        acceptButton.click()
//
//        and: "I click on the shutter button"
//        sleep(1000)
//        shutterButton.click()
//
//        then: "I should have the camera ready again"
//        preview
//    }
}
