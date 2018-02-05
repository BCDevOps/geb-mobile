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

        // searchText { 
        //     AndroidElement insertTextElement = (AndroidElement) new WebDriverWait(driver, 30).until(
        //         ExpectedConditions.elementToBeClickable(MobileBy.id("org.wikipedia.alpha:id/search_src_text")))
        //     return insertTextElement
        // }

        searchText {$("#search_src_text")}
        
        // resultList {
        //     List<AndroidElement> allProductsName = driver.findElementsByClassName("android.widget.TextView")
        //     return allProductsName
        // }

        resultList {$("android.widget.TextView")}
    }
}