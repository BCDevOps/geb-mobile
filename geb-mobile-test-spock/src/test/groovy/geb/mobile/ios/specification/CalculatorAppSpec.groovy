package geb.mobile.ios.specification

import geb.mobile.ios.views.CalculatorAppView
import geb.spock.GebSpec

import spock.lang.*

/**
 * Sample test case for iOS app calculator native app:
 */

class CalculatorAppSpec extends GebSpec {

    def "open test-app and test sum of two numbers"(){
        given: "Land on home page"
        at UICatalogAppView

        when:"Enter two numbers, #Number1 and #Number2"
        insertTextElement1 = "#Number1"
        insertTextElement2 = "#Number2"

        and: "Click on sum up button"
        sumElement.click()

        then: "Check result as #Result"
        resultField == "#Result"

        where:
        Number1 | Number2 | Result
        '0'       | '0'       | '0'
        '10'       | '10'       | '20'
        '-1'       | '-1'       | '-2'
        '100'       | '100'       | '200'
    }
}
