package geb.mobile.android.activities

import geb.mobile.android.AndroidBaseActivity

/**
 * Created by gmueksch on 23.06.14.
 */
class HomeScreenActivity extends AndroidBaseActivity {

    static content = {
        searchButton {$("~Search Wikipedia")}
        searchText {$("#search_src_text")}
        resultList {$("android.widget.TextView")}

    }
}
