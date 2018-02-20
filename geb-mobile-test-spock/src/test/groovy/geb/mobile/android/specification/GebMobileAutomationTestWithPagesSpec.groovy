package geb.mobile.android.specification

import geb.mobile.GebMobileBaseSpec
import geb.mobile.android.activities.HomeScreenActivity
//import geb.mobile.android.activities.RegisterUserActivity
//import geb.mobile.android.activities.VerifyUserActivity
//import geb.mobile.android.activities.WebViewActivity
//import geb.mobile.android.activities.TakePictureActivity
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
        doCameraFlow()
    }




//    def "take picture with camera app"() {
//        given: "open up camera"
//        at TakePictureActivity
//
//        when: "accept location request"
//        sleep(1000)
//        locationButton.click()
//
//
//        then: "take the pic"
//        sleep(1000)
//        shutterButton.click()
//        sleep(5000)
//    }

}
