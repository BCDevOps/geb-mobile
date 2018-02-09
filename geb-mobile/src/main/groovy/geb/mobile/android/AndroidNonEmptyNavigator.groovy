// package geb.mobile.android

// import geb.Browser
// import geb.Page
// import geb.error.UndefinedAtCheckerException
// import geb.error.UnexpectedPageException
// import geb.navigator.AbstractNavigator
// import geb.navigator.EmptyNavigator
// import geb.navigator.Navigator
// import geb.navigator.SelectFactory
// import geb.textmatching.TextMatcher
// import geb.waiting.WaitTimeoutException
// import groovy.util.logging.Slf4j
// import org.openqa.selenium.By
// import org.openqa.selenium.WebElement
// import org.openqa.selenium.remote.RemoteWebElement
// import sun.reflect.generics.reflectiveObjects.NotImplementedException

// import java.util.regex.Pattern

// import static java.util.Collections.EMPTY_LIST

// import io.appium.java_client.MobileElement
// import io.appium.java_client.android.AndroidDriver
// import io.appium.java_client.android.AndroidElement


// @Slf4j
// class AndroidNonEmptyNavigator extends AbstractNavigator {

//     protected final List<WebElement> contextElements

//     private Map _props = [:] as Map

//     AndroidDriver driver

//     AndroidNonEmptyNavigator(Browser browser, Collection<? extends WebElement> contextElements) {
//         super(browser)
//         this.contextElements = contextElements.toList().asImmutable()
//         //this._props = firstElement().properties
//         this.driver = (AndroidDriver)browser.driver
//     }


// // using the 
//     protected Navigator navigatorFor(Collection<WebElement> contextElements) {
//         browser.navigatorFactory.createFromWebElements(contextElements)
//     }

//     public Map getProps(){
//        if( _props.size()>0 ) return _props
//         synchronized ( _props ){
//             _props.putAll(firstElement().properties)
//         }
//         return _props
//     }

//     public String getPropsByName(name){
//         getProps()[name]
//     }

//     private String getAppPackage() {
//         driver.capabilities.getCapability("appPackage")
//     }

//     @Override
//     Navigator find(Map<String, Object> predicates, String selector) {
//         selector = optimizeSelector(selector, predicates)
//         if (selector) {
//             find(selector).filter(predicates)
//         } else {
//             find(predicates)
//         }
//     }

// // ==========================From UIAutomation:==========================
//     @Override
//     Navigator find(String selectorString) {
//         log.debug "Selector: $selectorString"
// // XPath:
//         if (selectorString.startsWith("//")) {
//             return navigatorFor(driver.findElements(By.xpath(selectorString)))
//         }
// // ID:
//         if (selectorString.startsWith("#")) {
//             String value = selectorString.substring(1)
//             if( value.indexOf(':')>0 ) {
//                 try{
//                     return navigatorFor(driver.findElementsByAndroidUIAutomator("resourceId(\"$value\")"))
//                 }catch(e){
//                     log.warn("Selector $selectorString: findElementsByAndroidUIAutomator resourceId(\"$value\") : $e.message")
//                     return new EmptyNavigator()
//                 }
//             }else {
//                 def apk = getAppPackage()
//                 if( !apk ) log.warn("for Selector $selectorString : AppPackage is emtpy, result may not be correct ")
//                 try {
//                     return navigatorFor(driver.findElementsByAndroidUIAutomator("resourceId(\"$appPackage:id/$value\")"))
//                 }catch(e){
//                     if( e.message =~ /BROWSER_TIMEOUT/ ) {
//                         log.error("Got Browser-Timeout, browser maybe already closed: $e.message")
//                         throw e
//                     }
//                     log.warn("Selector $selectorString: findElementsByAndroidUIAutomator resourceId(\"$appPackage:id/$value\") ")
//                     return new EmptyNavigator()
//                 }
//             }
//         }
// // WEB_VIEW:
//         else if ( selectorString.startsWith(".") ){
//             log.debug "At web view with: $selectorString"
//             return navigatorFor(driver.findElementsByCssSelector(selectorString))
//         } 
// // Class Name:
//         else if (selectorString.indexOf('.') > 1) {
//             log.debug "Using Class Name with: $selectorString"
//             return navigatorFor(driver.findElementsByClassName(selectorString))
//         }
// // Accessibility ID (content_Desc):
//         else {
//             log.debug "Using Accessibility ID with: $selectorString"
//             return navigatorFor(driver.findElementsByAccessibilityId(selectorString))
//         }
//     }

//     @Override
//     Navigator unique() {
//         new AndroidUIAutomatorNonEmptyNavigator(browser, contextElements.unique(false))
//     }

//     // @Override
//     protected getInputValue(WebElement input) {
//         def value
//         def tagName = tag()

//         if (tagName == "android.widget.Spinner") {
//             if( AndroidHelper.isOnListView(driver) )
//                 value = input.findElementByAndroidUIAutomator("fromParent(new UiSelector().checked(true))").getText()
//             else
//                 value = input.findElementByAndroidUIAutomator("fromParent(new UiSelector())").getText()
//         } else if (tagName in ['android.widget.CheckBox','android.widget.Switch']) {
//             value = input.getAttribute("checked")
//         } else {
//             value = input.getText()
//         }
//         log.debug("inputValue for $tagName : $value ")
//         value
//     }

//     // @Override
//     void setInputValue(WebElement input, Object value) {

//         def tagName = tag()
//         log.debug("setInputValue: $input, $tagName")
//         if (tagName == "android.widget.Spinner") {
//             if (getInputValue(input) == value) return
//             setSpinnerValue(input,value)
//             AndroidHelper.closeListView(driver)

//         } else if (tagName in ['android.widget.CheckBox', 'android.widget.RadioButton' ,'android.widget.Switch']) {
//             def checked = input.getAttribute("checked")?.toBoolean()
//             if ( !checked && value) {
//                 input.click()
//             } else if (checked && !value ) {
//                 input.click()
//             }
//         }else {
//             //TODO: hideKeyboard after sendKeys
//             //TODO: clear Copy/Paste 
// //            input.clear()
//             //input.sendKeys(Keys.HOME,Keys.chord(Keys.SHIFT,Keys.END),value);
//             input.sendKeys value as String

//             try{
//                 driver.hideKeyboard()
//             }catch(e){
//                 log.warn("Hiding keyboard propably worked, but has thrown exc: $e.message")
//             }
//         }
//     }

//     private void setSpinnerValueWithScrollTo(MobileElement input, value) {
//         try {
//             input.click()
//             driver.scrollTo(value?.toString())?.click()
//         } catch (e) {
//             log.warn("Could not set $value to $input.tagName : $e.message")
//         }
//     }

//     private void setSpinnerValueWithScrollToExact(MobileElement input, value) {
//         try {
//             input.click()
//             driver.scrollTo(value?.toString())?.click()
//         } catch (e) {
//             log.warn("Could not set $value to $input.tagName : $e.message")
//         }
//     }

//     private void setSpinnerValue(MobileElement input, value) {
//         try {
//             def currVal = getInputValue(input)
//             log.debug("Setting $value to Spinner: currentVal: ${currVal}")
//             input.click()
//             //input.properties
//             driver.findElementByAndroidUIAutomator("text(\"$value\")").click()
//             //input.findElementByAndroidUIAutomator("fromParent(new UiSelector().text(\"$value\"))")?.click()
//             if (getInputValue(input) == value) return
//             if( AndroidHelper.isOnListView(driver) ) {
//                 log.debug("Value not set and on ListView: Scrolling to $value")
//                 browser.driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().className(\"android.widget.ListView\")).flingBackward();")
//                 driver.findElementByAndroidUIAutomator("text(\"$value\")").click()
//             }
//             //input.findElementByAndroidUIAutomator("fromParent(new UiSelector().text(\"$value\"))")?.click()
//         } catch (e) {
//             log.warn("Error selecting with UiAutomator: $e.message")
//         }
//     }



//     private void flingBack(){
//         driver.execute("new UiScrollable(new UiSelector().className('android.widget.ListView')).flingBackward();",null)
//     }


// // ==============================================================================


//     // @Override
//     // Navigator filter(String selectorString) {
//     //     throw new NotImplementedException("no filtering by css on selendroid elements yet")
//     // }

//     @Override
//     Navigator filter(Map<String, Object> predicates) {
//         navigatorFor contextElements.findAll { matches(it, predicates) }
//     }

//     @Override
//     Navigator not(String selectorString) {
//         throw new NotImplementedException("no filtering by css on selendroid elements yet")
//     }

//     @Override
//     Navigator not(Map<String, Object> predicates, String selectorString) {
//         throw new NotImplementedException("no filtering by css on selendroid elements yet")
//     }

//     @Override
//     Navigator not(Map<String, Object> predicates) {
//         navigatorFor contextElements.findAll { element ->
//             !matches(element, predicates)
//         }
//     }

//     @Override
//     Navigator getAt(int index) {
//         navigatorFor(Collections.singleton(getElement(index)))
//     }

//     @Override
//     Navigator getAt(Range range) {
//         navigatorFor getElements(range)
//     }

//     Navigator getAt(EmptyRange range) {
//         new EmptyNavigator(browser)
//     }

//     @Override
//     Navigator getAt(Collection indexes) {
//         navigatorFor getElements(indexes)
//     }

//     @Override
//     Collection<WebElement> allElements() {
//         contextElements as WebElement[]
//     }

//     @Override
//     WebElement getElement(int index) {
//         contextElements[index]
//     }

//     @Override
//     List<WebElement> getElements(Range range) {
//         contextElements[range]
//     }

//     List<WebElement> getElements(EmptyRange range) {
//         EMPTY_LIST
//     }

//     @Override
//     List<WebElement> getElements(Collection indexes) {
//         contextElements[indexes]
//     }

//     @Override
//     Navigator remove(int index) {
//         int size = size()
//         if (!(index in -size..<size)) {
//             this
//         } else if (size == 1) {
//             new EmptyNavigator(browser)
//         } else {
//             navigatorFor(contextElements - contextElements[index])
//         }
//     }

//     @Override
//     Navigator next() {
//         navigatorFor collectElements {
//             it.findElement By.xpath("following-sibling::*")
//         }
//     }

//     @Override
//     Navigator next(String selectorString) {
//         throw new NotImplementedException("no next by xpath on selendroid elements yet")
// //        navigatorFor collectElements {
// //            def siblings = it.findElements(By.xpath("following-sibling::*"))
// //            siblings.find { CssSelector.matches(it, selectorString) }
// //        }
//     }

//     @Override
//     Navigator nextAll() {
//         throw new NotImplementedException("no next by xpath on selendroid elements yet")
// //        navigatorFor collectElements {
// //            it.findElements By.xpath("following-sibling::*")
// //        }
//     }

//     @Override
//     Navigator nextAll(String selectorString) {
//         navigatorFor collectElements {
//             def siblings = it.findElements(By.xpath("following-sibling::*"))
//             siblings.findAll { CssSelector.matches(it, selectorString) }
//         }
//     }

//     @Override
//     Navigator nextUntil(String selectorString) {
//         throw new NotImplementedException("no next by xpath on selendroid elements yet")
// //        navigatorFor collectElements { element ->
// //            def siblings = element.findElements(By.xpath("following-sibling::*"))
// //            collectUntil(siblings, selectorString)
// //        }
//     }

//     @Override
//     Navigator previous() {
//         throw new NotImplementedException("no next by xpath on selendroid elements yet")
// //        navigatorFor collectElements {
// //            def siblings = it.findElements(By.xpath("preceding-sibling::*"))
// //            siblings ? siblings.last() : EMPTY_LIST
// //        }
//     }

//     @Override
//     Navigator previous(String selectorString) {
//         throw new NotImplementedException("no previous by xpath on selendroid elements yet")
// //        navigatorFor collectElements {
// //            def siblings = it.findElements(By.xpath("preceding-sibling::*")).reverse()
// //            siblings.find { CssSelector.matches(it, selectorString) }
// //        }
//     }

//     @Override
//     Navigator prevAll() {
//         throw new NotImplementedException("no previous by xpath on selendroid elements yet")
// //        navigatorFor collectElements {
// //            it.findElements(By.xpath("preceding-sibling::*"))
// //        }
//     }

//     @Override
//     Navigator prevAll(String selectorString) {
//         navigatorFor collectElements {
//             def siblings = it.findElements(By.xpath("preceding-sibling::*")).reverse()
//             siblings.findAll { CssSelector.matches(it, selectorString) }
//         }
//     }

//     @Override
//     Navigator prevUntil(String selectorString) {
//         throw new NotImplementedException("no previous by xpath on selendroid elements yet")
// //        navigatorFor collectElements { element ->
// //            def siblings = element.findElements(By.xpath("preceding-sibling::*")).reverse()
// //            collectUntil(siblings, selectorString)
// //        }
//     }

//     @Override
//     Navigator parent() {
//         navigatorFor collectElements {
//             it.findElement By.xpath("parent::*")
//         }
//     }

//     @Override
//     Navigator parent(String selectorString) {
//         parent().filter(selectorString)
//     }

//     @Override
//     Navigator parents() {
//         navigatorFor collectElements {
//             it.findElements(By.xpath("ancestor::*")).reverse()
//         }
//     }

//     @Override
//     Navigator parents(String selectorString) {
// //        navigatorFor collectElements {
// //            def ancestors = it.findElements(By.xpath("ancestor::*")).reverse()
// //            ancestors.findAll { CssSelector.matches(it, selectorString) }
// //        }
//         null
//     }

//     @Override
//     Navigator parentsUntil(String selectorString) {
//         navigatorFor collectElements { element ->
//             def ancestors = element.findElements(By.xpath("ancestor::*")).reverse()
//             collectUntil(ancestors, selectorString)
//         }
//     }

//     @Override
//     Navigator closest(String selectorString) {
//         throw new NotImplementedException("no css on selendroid elements yet")
// //        navigatorFor collectElements {
// //            def parents = it.findElements(By.xpath("ancestor::*")).reverse()
// //            parents.find { CssSelector.matches(it, selectorString) }
// //        }
//     }

//     @Override
//     Navigator children() {
//         navigatorFor collectElements {
//             it.findElements By.xpath("child::*")
//         }
//     }

//     @Override
//     Navigator children(String selectorString) {
//         children().filter(selectorString)
//     }

//     @Override
//     Navigator siblings() {
//         navigatorFor collectElements {
//             it.findElements(By.xpath("preceding-sibling::*")) + it.findElements(By.xpath("following-sibling::*"))
//         }
//     }

//     @Override
//     Navigator siblings(String selectorString) {
//         siblings().filter(selectorString)
//     }

//     @Override
//     boolean hasClass(String valueToContain) {
//         any { valueToContain in it.classes() }
//     }

//     @Override
//     boolean is(String tag) {
//         contextElements.any { tag.equalsIgnoreCase(it.tagName) }
//     }

//     @Override
//     boolean isDisplayed() {
//         getAttribute('displayed') ?: false
//     }

//     @Override
//     String tag() {
//         firstElement().getTagName()
//     }

//     @Override
//     String text() {
//         firstElement().text ?: getAttribute("name")
//     }


//     @Override
//     String getAttribute(String name) {

//         try {
//             def attribute = firstElement().getAttribute(name)
//             if( attribute != null ) return attribute
//         }catch(e){
//             log.warn("Attribute[$name]on ${firstElement()} : $e.message")
//         }
//         log.debug("Looking for Attribute $name in Properties")
//         return getPropsByName(name)

//     }

//     @Override
//     List<String> classes() {
//         contextElements.head().getAttribute("class")?.tokenize()?.unique()?.sort() ?: EMPTY_LIST
//     }

//     @Override
//     def value() {
//         getInputValue(contextElements.head())
//     }

//     @Override
//     Navigator value(value) {
//         setInputValues(contextElements, value)
//         this
//     }

//     @Override
//     Navigator leftShift(value) {
//         contextElements.each {
//             it.sendKeys value
//         }
//         this
//     }

//     @Override
//     Navigator click() {
//         contextElements.first().click()
//         this
//     }

//     @Override
//     Navigator click(Class<? extends Page> pageClass) {
//         click()
//         browser.page(pageClass)
//         def at = false
//         def error = null
//         try {
//             at = browser.verifyAt()
//         } catch (AssertionError e) {
//             error = e
//         } catch (WaitTimeoutException e) {
//             error = e
//         } catch (UndefinedAtCheckerException e) {
//             at = true
//         } finally {
//             if (!at) {
//                 throw new UnexpectedPageException(pageClass, error)
//             }
//         }
//         this
//     }

//     @Override
//     Navigator click(List<Class<? extends Page>> potentialPageClasses) {
//         click()
//         browser.page(*potentialPageClasses)
//         this
//     }

//     @Override
//     int size() {
//         contextElements.size()
//     }

//     @Override
//     boolean isEmpty() {
//         size() == 0
//     }

//     @Override
//     Navigator head() {
//         first()
//     }

//     @Override
//     Navigator first() {
//         navigatorFor(Collections.singleton(firstElement()))
//     }

//     @Override
//     Navigator last() {
//         navigatorFor(Collections.singleton(lastElement()))
//     }

//     @Override
//     Navigator tail() {
//         navigatorFor contextElements.tail()
//     }

//     @Override
//     Navigator verifyNotEmpty() {
//         this
//     }

//     @Override
//     String toString() {
//         contextElements*.toString()
//     }

//     def methodMissing(String name, arguments) {
//         if (!arguments) {
//             navigatorFor collectElements {
//                 it.findElements By.name(name)
//             }
//         } else {
//             throw new MissingMethodException(name, getClass(), arguments)
//         }
//     }


//     def propertyMissing(String name) {
//         switch (name) {
//             case ~/@.+/:
//                 return getAttribute(name.substring(1))
//             default:
//                 def inputs = collectElements {
//                     it.findElements(By.name(name))
//                 }

//                 if (inputs) {
//                     return getInputValues(inputs)
//                 } else {
//                     throw new MissingPropertyException(name, getClass())
//                 }
//         }
//     }

//     def propertyMissing(String name, value) {
//         def inputs = collectElements {
//             it.findElements(By.name(name))
//         }

//         if (inputs) {
//             setInputValues(inputs, value)
//         } else {
//             throw new MissingPropertyException(name, getClass())
//         }
//     }

//     /**
//      * Optimizes the selector if the predicates contains `class` or `id` keys that map to strings. Note this method has
//      * a side-effect in that it _removes_ those keys from the predicates map.
//      */
//     protected String optimizeSelector(String selector, Map<String, Object> predicates) {
//         if (!selector) {
//             return selector
//         }

//         def buffer = new StringBuilder(selector)
//         if (predicates.containsKey("id") && predicates["id"] in String) {
//             buffer << "#" << CssSelector.escape(predicates.remove("id"))
//         }
//         if (predicates.containsKey("class") && predicates["class"] in String) {
//             predicates.remove("class").split(/\s+/).each { className ->
//                 buffer << "." << CssSelector.escape(className)
//             }
//         }
//         if (buffer[0] == "*" && buffer.length() > 1) buffer.deleteCharAt(0)
//         return buffer.toString()
//     }

//     protected boolean matches(WebElement element, Map<String, Object> predicates) {
//         def result = predicates.every { name, requiredValue ->
//             def actualValue
//             switch (name) {
//                 case "text": actualValue = element.text; break
//                 case "class": actualValue = element.getAttribute("class")?.tokenize(); break
//                 default: actualValue = element.getAttribute(name)
//             }
//             matches(actualValue, requiredValue)
//         }
//         result
//     }

//     protected boolean matches(String actualValue, String requiredValue) {
//         actualValue == requiredValue
//     }

//     protected boolean matches(String actualValue, Pattern requiredValue) {
//         actualValue ==~ requiredValue
//     }

//     protected boolean matches(String actualValue, TextMatcher matcher) {
//         matcher.matches(actualValue)
//     }

//     protected boolean matches(Collection<String> actualValue, String requiredValue) {
//         requiredValue in actualValue
//     }

//     protected boolean matches(Collection<String> actualValue, Pattern requiredValue) {
//         actualValue.any { it ==~ requiredValue }
//     }

//     protected boolean matches(Collection<String> actualValue, TextMatcher matcher) {
//         actualValue.any { matcher.matches(it) }
//     }

//     protected getInputValues(Collection<WebElement> inputs) {
//         def values = []
//         inputs.each { WebElement input ->
//             def value = getInputValue(input)
//             if (value != null) values << value
//         }
//         return values.size() < 2 ? values[0] : values
//     }

//     // protected getInputValue(WebElement input) {
//     //     def value
//     //     def tagName = tag()

//     //     if (tagName == "android.widget.Spinner") {
//     //         value = input?.findElementByAndroidUIAutomator("new UiSelector().enabled(true)").getText()
//     //     } else if (tagName == "android.widget.CheckBox") {
//     //         value = input.getAttribute("checked")
//     //     } else {
//     //         value = input.getText()
//     //     }
//     //     log.debug("inputValue for $tagName : $value ")
//     //     value
//     // }

//     protected void setInputValues(Collection<WebElement> inputs, value) {
//         inputs.each { WebElement input ->
//             setInputValue(input, value)
//         }
//     }

//     /**
//      * Defines how the value is set to the input
//      * @param input
//      * @param value
//      */
//     // abstract void setInputValue(WebElement input, value) ;

//     protected getValue(WebElement input) {
//         input?.getAttribute("value")
//     }

//     protected setSelectValue(WebElement element, value) {
//         def select = new SelectFactory().createSelectFor(element)

//         if (value == null || (value instanceof Collection && value.empty)) {
//             select.deselectAll()
//             return
//         }

//         def multiple = select.multiple
//         def valueStrings
//         if (multiple) {
//             valueStrings = (value instanceof Collection ? new LinkedList(value) : [value])*.toString()
//         } else {
//             valueStrings = [value.toString()]
//         }

//         for (valueString in valueStrings) {
//             try {
//                 select.selectByValue(valueString)
//             } catch (org.openqa.selenium.NoSuchElementException e1) {
//                 try {
//                     select.selectByVisibleText(valueString)
//                 } catch (org.openqa.selenium.NoSuchElementException e2) {
//                     throw new IllegalArgumentException("couldn't select option with text or value: $valueString")
//                 }
//             }
//         }

//         if (multiple) {
//             def selectedOptions = select.getAllSelectedOptions()
//             for (selectedOption in selectedOptions) {
//                 if (!valueStrings.contains(selectedOption.getAttribute("value")) && !valueStrings.contains(selectedOption.text)) {
//                     selectedOption.click()
//                     assert !selectedOption.isSelected()
//                 }
//             }
//         }
//     }

//     protected String labelFor(WebElement input) {
//         def id = input.getAttribute("id")
//         def labels = browser.driver.findElements(By.xpath("//label[@for='$id']"))
//         if (!labels) {
//             labels = input.findElements(By.xpath("ancestor::label"))
//         }
//         labels ? labels[0].text : null
//     }

//     /**
//      * This works around an inconsistency in some of the WebDriver implementations.
//      * According to the spec WebElement.getAttribute should return the Strings "true" or "false"
//      * however ChromeDriver and HtmlUnitDriver will return "" or null.
//      */
//     protected boolean getBooleanAttribute(WebElement input, String attribute) {
//         !(input.getAttribute(attribute) in [null, false, "false"])
//     }

//     protected WebElement firstElementInContext(Closure closure) {
//         def result = null
//         for (int i = 0; !result && i < contextElements.size(); i++) {
//             try {
//                 result = closure(contextElements[i])
//             } catch (org.openqa.selenium.NoSuchElementException e) {
//             }
//         }
//         result
//     }

//     protected List<WebElement> collectElements(Closure closure) {
//         List<WebElement> list = []
//         contextElements.each {
//             try {
//                 def value = closure(it)
//                 switch (value) {
//                     case Collection:
//                         list.addAll value
//                         break
//                     default:
//                         if (value) list << value
//                 }
//             } catch (org.openqa.selenium.NoSuchElementException e) {
//             }
//         }
//         list
//     }

//     protected Collection<WebElement> collectUntil(Collection<WebElement> elements, String selectorString) {
//         int index = elements.findIndexOf { CssSelector.matches(it, selectorString) }
//         index == -1 ? elements : elements[0..<index]
//     }



// // Deprecated: https://groups.google.com/forum/#!topic/geb-dev/sS_tFJEQzVw
//     // @Override
//     // boolean isDisabled() {
//     //     def dis = null
//     //     try {
//     //         dis = getAttribute('disabled')
//     //     }catch(e){
//     //         log.warn("No 'disabled' attribute ")
//     //     }
//     //     if( dis == null ){
//     //         dis = !getAttribute('enabled')
//     //     }
//     //     return Boolean.valueOf(dis)
//     // }

//     // @Override
//     // boolean isEnabled() {
//     //     def ena = getAttribute('enabled')
//     //     return Boolean.valueOf(ena)
//     // }

//     // @Override
//     // boolean isReadOnly() {
//     //     return false
//     // }

//     // @Override
//     // boolean isEditable() {
//     //     return true
//     // }



// }