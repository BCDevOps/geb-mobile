
# Geb Mobile Extension
---

## Under Development
+ WIP
---

## Running Sample Test Cases
+ To run the Android test case: `./gradlew -i clean androidOnBrowserStack`
+ To run iOS test case: `./gradlew -i clean iOSOnBrowserStack`
+ To upload apps: replace app path in geb-mobile-test-spock/build.gradle, then run `./gradlew -i clean uploadAppToDeviceFarm`

## TODO
+ Add mobile touch screen specific methods (scroll + direction +distance/target, swipe, long press, )
+ Add mobile KeyCode
+ Local simulator/emulator (Xcode and ADB)
+ Find element for children
+ Chrome/Chromeium Driver
+ Auto import app bs from cloud device farm
+ Locate elements from layout xml using Appium Desktop
+ data table in test case
+ Add old drivers: selendroid, UIautomation 1
+ docker image


## Dependency versions:
+ Geb v2.0
+ Spock v1.0-groovy-2.3
+ Groovy v2.3.11
+ Gradle v4.3.1
+ Appium Java Client v5.0.4 (stable version)
+ Selenium v3.8.1
+ jUnit v4.11
