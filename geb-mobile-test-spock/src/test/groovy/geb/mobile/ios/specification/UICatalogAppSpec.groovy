package geb.mobile.ios.specification

import geb.mobile.ios.views.UICatalogAppView
import geb.spock.GebSpec

/**
 * Created by gmueksch on 01.09.14.
 */
class UICatalogAppSpec extends GebSpec {

    def "enter numbers to TextFields"(){
        given:
        at UICatalogAppView
        when:
        insertTextElement1 = "12"
        insertTextElement2 = "22"
        and:
        sumElement.click()
        then:
        resultField == "34"
    }

}
