package geb.mobile.ios.views

import geb.mobile.ios.IosBaseView


class CalculatorAppView extends IosBaseView {

    static at = { insertTextElement1.isEnabled() }

    static content = {
        insertTextElement1 { $("#TextField1") }
        insertTextElement2 { $("#TextField2") }
        sumElement { $("#ComputeSumButton") }
        resultField { $("#UIAStaticText")[0] }
    }
}