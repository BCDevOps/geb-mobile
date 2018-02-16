package geb.mobile.android.activities

import geb.mobile.android.AndroidBaseActivity
import groovy.util.logging.Slf4j

/**
 * Created by gmueksch on 23.06.14.
 */

@Slf4j
class HomeScreenActivity extends AndroidBaseActivity {

    static content = {
        searchButton {find("~Search Wikipedia")}
        searchText {$("#search_src_text")}
        resultList {$("android.widget.TextView")}
        targetResult {$("//android.widget.TextView[@text='HyperCard']")}

        swipeAction {
//            waitFor(5,1) {
            if(!targetResult) {
                swipeDown()
            } else {
                back()
                return true
            }
        }
    }
}
