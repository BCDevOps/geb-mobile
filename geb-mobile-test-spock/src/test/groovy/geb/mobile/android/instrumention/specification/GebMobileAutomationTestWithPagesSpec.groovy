package geb.mobile.android.instrumention.specification

import geb.mobile.GebMobileBaseSpec
import geb.mobile.android.instrumention.activities.HomeScreenActivity
import spock.lang.Stepwise

@Stepwise
class GebMobileAutomationTestWithPagesSpec extends GebMobileBaseSpec {

    def "open app and search with text input"() {

        given: "I land on Home screen"
            at HomeScreenActivity

        when: "I click on the search button"
            searchButton.click()

        and: "I type in search string"
            searchText.click()
            // sendKeys not supported by old drivers
            // searchText.sendKeys("BrowserStack")

        then: "I should see results"
            assert resultList.size() > 0
    }
}