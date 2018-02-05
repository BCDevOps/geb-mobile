package geb.mobile.android.instrumention.activities
import geb.mobile.android.AndroidBaseActivity

import io.appium.java_client.MobileBy
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.AndroidElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

class HomeScreenActivity extends AndroidBaseActivity {

    static content = {
        searchButton {$("Search Wikipedia")}

        searchText {$("#search_src_text")}

        resultList {$("android.widget.TextView")}
    }
}