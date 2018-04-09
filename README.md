
# Geb Mobile Extension
---

## Under Development
+ WIP: This tool has basic functionality working. Please make request for more mobile actions and operations.
+ notice: For iOS 10.x, the UI element will not be accessible from Appium according to https://github.com/facebook/WebDriverAgent/issues/703
---

## Running Sample Test Cases with Docker usage:
1. Build the Docker image (Dockerfile in the CI folder)
2. The sample test case will be executed as last step in building



## Running Sample Test Cases with Cloud-based mobile devices:
+ To run the Android test case: `./gradlew -i clean androidOnBrowserStack`
+ To run iOS test case: `./gradlew -i clean iOSOnBrowserStack`
+ To upload apps: replace app path in geb-mobile-test-spock/build.gradle, then run `./gradlew -i clean uploadAppToDeviceFarm`


## Running Sample Test Cases with Local devices:

To run Android local device, please install adb:
+ install SDK `brew install android-platform-tools`
+ setup android SDK home path
+ connect device with USB, check if device is recognized by android device bridge `adb devices`
+ To run the test case: replace device info in geb-mobile-test-spock/build.gradle, then run `./gradlew -i clean androidOnLocalDevice`

To run iOS local device:
+ install Xcode
+ install npm at https://www.npmjs.com/get-npm
+ init npm for the package.json `npm init`
+ install XCUITest Driver: `npm install appium-xcuitest-driver`
+ connect device with USB, check if device is recognized by xcode `xcrun instruments -s devices`
+ follow instructions from appium repo: https://github.com/appium/appium-xcuitest-driver/blob/master/docs/real-device-config.md to install WebDriverAgent on iPhone (this will be an app that helps to install and run testing apps on the device)
+ make sure it works as mentioned from above config
+ To run the test case: replace device info in geb-mobile-test-spock/build.gradle, then run `./gradlew -i clean iOSOnLocalDevice`


## Test Case Creation:
+ switch context of app between ["NATIVE_CONTEXT", "WEBVIEW_1", "WEBVIEW_2"...], use setNativeContext() and setWebViewContext()
+ Find elements use [#, @, //, ~] prefix to refer to different find methods (more details to be added later)


## TODO
+ Add mobile touch screen specific methods for iOS (scroll + target, long press..)
+ Find element for children
+ Chrome/Chromeium Driver
+ Locate elements from layout xml using Appium Desktop
+ data table in test case
+ Add old drivers: selendroid, UIautomation 1


## Dependency versions:
+ Geb v2.0
+ Spock v1.0-groovy-2.3
+ Groovy v2.3.11
+ Gradle v4.3.1
+ Appium Java Client v5.0.4 (stable version)
+ Selenium v3.8.1
+ jUnit v4.11
