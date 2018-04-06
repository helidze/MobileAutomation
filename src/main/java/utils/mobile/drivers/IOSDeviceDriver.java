package utils.mobile.drivers;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.SysUtils;
import utils.mobile.DriverWindows;
import utils.mobile.MobileDriver;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class IOSDeviceDriver extends MobileDriver {

    private static final Logger LOG = Logger.getLogger(IOSDeviceDriver.class);

    private String UDID;
    private String deviceName;

    private String deviceVersion;
    private Integer deviceVersionNumber;

    private AppiumDriverLocalService appiumDriverLocalService;
    private String appiumVersion;
    private Integer appiumVersionNumber;

    private Integer xcodeVersionNumber;

    /**
     * Application Maintenance methods.
     */

    public void startMobileServer() {
        LOG.info("Check connected device.");
        HashMap<String, String> deviceProperties = getDeviceProperties();
        UDID = deviceProperties.get("UniqueDeviceID");
        deviceName = deviceProperties.get("DeviceName");
        deviceVersion = deviceProperties.get("ProductVersion");
        deviceVersionNumber = Integer.valueOf(deviceVersion.split("\\.")[0]);

        LOG.info("Check Xcode and Appium version.");
        String xcodeVersion = getXCodeVersion();
        xcodeVersionNumber = Integer.valueOf(xcodeVersion.split("\\.")[0]);
        appiumVersion = getAppiumVersion();
        appiumVersionNumber = Integer.valueOf(appiumVersion.split("\\.")[1]);

        LOG.info("Start Appium Server.");
        appiumDriverLocalService = new AppiumServiceBuilder()
                .usingPort(APPIUM_PORT)
                .withIPAddress("127.0.0.1")
                .withArgument(GeneralServerFlag.LOG_LEVEL, "error")
                .build();
        appiumDriverLocalService.start();
    }

    public void startMobileDriver(String appLocation) {
        LOG.info("Check environment compatibility");
        if (xcodeVersionNumber >= 8) {
            assert appiumVersionNumber >= 6 : "Xcode 8 and higher supported from Appium version 1.6. Your Appium version : " + appiumVersion;
            assert deviceVersionNumber >= 9 : "Appium from version 1.6 do not support devices under 9.2. Your device version : " + deviceVersion;
        } else {
            assert deviceVersionNumber < 10 : "IOS SDK 10 and higher supported with Xcode 8 and higher.";
        }

        if (!isIOSWebkitDebugProxyEnabled()) {
            reStartIOSWebkitDebugProxyForUDID(UDID);
        }

        LOG.info("Check mobile driver instance.");
        webDriver = null;
        uninstallApplications();
        if (xcodeVersionNumber >= 8) {
            webDriver = createXCUITestDriver(appLocation);
        } else {
            webDriver = createIOSDriver(appLocation);
        }
    }

    private WebDriver createIOSDriver(String appLocation) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, deviceVersion);
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, COMMAND_TIMEOUT);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        capabilities.setCapability(MobileCapabilityType.UDID, UDID);
        capabilities.setCapability(MobileCapabilityType.APP, appLocation);

        capabilities.setCapability(MobileCapabilityType.ORIENTATION, "LANDSCAPE");
        capabilities.setCapability(MobileCapabilityType.AUTO_WEBVIEW, true);
        capabilities.setCapability(IOSMobileCapabilityType.INTER_KEY_DELAY, 50);
        capabilities.setCapability(IOSMobileCapabilityType.SEND_KEY_STRATEGY, "grouped");

        try {
            webDriver = new IOSDriver(new URL("http://127.0.0.1:" + APPIUM_PORT + "/wd/hub"), capabilities);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert webDriver != null : "Can not launch IOS driver. See log below.";
        return webDriver;
    }

    private WebDriver createXCUITestDriver(String appLocation) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, deviceVersion);
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, COMMAND_TIMEOUT);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        capabilities.setCapability(MobileCapabilityType.UDID, UDID);
        capabilities.setCapability(MobileCapabilityType.APP, appLocation);

        capabilities.setCapability(MobileCapabilityType.ORIENTATION, "LANDSCAPE");
        capabilities.setCapability(IOSMobileCapabilityType.NATIVE_WEB_TAP, true);
        capabilities.setCapability("simpleIsVisibleCheck", true);

        capabilities.setCapability(IOSMobileCapabilityType.SHOW_XCODE_LOG, false);
        capabilities.setCapability(IOSMobileCapabilityType.SHOW_IOS_LOG, false);

        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
        capabilities.setCapability(IOSMobileCapabilityType.USE_NEW_WDA, true);
        capabilities.setCapability("xcodeOrgId", "7Z5N86YJ2S");
        capabilities.setCapability("xcodeSigningId", "iPhone Developer");

        LOG.info("Start iOS webDriver and launch application on device with udid : " + UDID);
        try {
            webDriver = new IOSDriver(new URL("http://127.0.0.1:" + APPIUM_PORT + "/wd/hub"), capabilities);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert webDriver != null : "Can not launch IOS driver. See log below.";
        return webDriver;
    }

    private HashMap<String, String> getDeviceProperties() {
        HashMap<String, String> deviceProperties = new HashMap<>();
        String iDeviceInfoResponse = executeRuntimeRequest("ideviceinfo", true);
        assert !iDeviceInfoResponse.isEmpty() : "There are no device connected!";

        String[] propertiesPairArray = iDeviceInfoResponse.split("\n");
        for (String propertiesPair : propertiesPairArray) {
            String[] propertyPair = propertiesPair.split(": ");
            if (propertyPair.length == 0) {
                LOG.error("Incorrect info response line: " + propertiesPair);
            } else if (propertyPair.length == 1) {
                deviceProperties.put(propertyPair[0], "");
            } else {
                deviceProperties.put(propertyPair[0], propertyPair[1]);
            }
        }

        return deviceProperties;
    }

    private String getXCodeVersion() {
        String versionNumber = executeRuntimeRequest("xcodebuild -version", true);
        versionNumber = versionNumber.split("\n")[0].split("Xcode ")[1];
        LOG.info("Current Xcode version : " + versionNumber);
        return versionNumber;
    }

    private void uninstallApplications() {
        executeRuntimeRequest("ideviceinstaller --udid " + UDID + " --uninstall " + BUNDLE_ID, true);
        executeRuntimeRequest("ideviceinstaller --udid " + UDID + " --uninstall com.apple.test.WebDriverAgentRunner-Runner", true);
    }

    private String getAppiumVersion() {
        String appiumVersion = executeRuntimeRequest("appium -v", true);
        appiumVersion = appiumVersion.replace("\n", "");
        LOG.info("Current Appium version : " + appiumVersion);
        return appiumVersion;
    }

    private boolean isAppiumServerRunning() {
        LOG.info("Check that appium server is running...");
        String isServerAliveOutput = executeRuntimeRequest("lsof -i -P | pgrep node", true);
        return !isServerAliveOutput.isEmpty();
    }

    private void reStartIOSWebkitDebugProxyForUDID(String UDID) {
        LOG.info("ReStart ios webkit debug proxy, for current device UDID: " + UDID);
        executeRuntimeRequest("killall ios_webkit_debug_proxy", true);
        executeRuntimeRequest("ios_webkit_debug_proxy -c " + UDID + ":27753", false);
        assert isIOSWebkitDebugProxySuccessfullyStarted() : "ios_webkit_debug_proxy does not started in 30 seconds.";
    }

    private boolean isIOSWebkitDebugProxySuccessfullyStarted() {
        for (int i = 0; i < 30; i++) {
            if (isIOSWebkitDebugProxyEnabled()) {
                return true;
            } else {
                LOG.info("Wait for IOSWebkitDebugProxy started ..." + i);
                SysUtils.sleep(1000);
            }
        }
        return false;
    }

    private boolean isIOSWebkitDebugProxyEnabled() {
        String httpCode = executeRuntimeRequest("curl -I -s http://localhost:27753/json  -o /dev/null -w \"%{http_code}\"", true);
        return httpCode.equals("200\n");
    }

    public void devDriverSwitchTo(String window) {
        Set<String> contextHandles = ((IOSDriver) webDriver).getContextHandles();
        LOG.info("Available context size = " + contextHandles.size());
        for (String context : contextHandles) {
            if (context.contains(window)) {
                ((IOSDriver) webDriver).context(context);
                LOG.info("Switch to context '" + context + "'.");
                break;
            }
        }
    }

    public void takeConfirm(String alertText) {
        Alert alert = webDriver.switchTo().alert();
        assert alert.getText().equals(alertText) : "Incorrect alert message present ! : " + alert.getText();
        LOG.info("ALERT : " + alert.getText());
        alert.accept();
    }

    /**
     * Overwrite WebDriver methods.
     */

    public void quit() {
        LOG.info("IOSDeviceDriver quit. Uninstall application and close all server connection.");
        if (webDriver != null) {
            webDriver.quit();
            webDriver = null;
        }
        if (isAppiumServerRunning()) {
            appiumDriverLocalService.stop();
        }
    }

    /**
     * Implement Native methods.
     */


    public void takePhoto() {
        LOG.info("Take photo via device camera.");
        devDriverSwitchTo(DriverWindows.NATIVE_APP.getView());
        if (appiumVersionNumber > 5) {
            if (isXCUIElementDisplayed(By.name("“Form.com” Would Like to Access the Camera"))) {
                longPressXCUIElement(By.name("OK"));
            }
            clickXCUIElement(By.name("shutter"));
            clickXCUIElement(By.name("Use Photo"));
            waitForXCUIElementNotDisplayed(By.name("Use Photo"), 2);
        } else {
            clickUIAutomationElement("UIATarget.localTarget().frontMostApp().mainWindow().buttons()[\"PhotoCapture\"]");
            clickUIAutomationElement("UIATarget.localTarget().frontMostApp().mainWindow().buttons()[\"Use Photo\"]");
            waitForUIAutomationElementNotDisplayed("UIATarget.localTarget().frontMostApp().mainWindow().buttons()[\"Use Photo\"]");
        }
        devDriverSwitchTo(DriverWindows.WEBVIEW.getView());
    }

    public void verifyUploadedFileName(String actualFileName, String expectedFileName) {
        assert actualFileName.matches("sc_photo_.*?.jpg") : "Filename " + actualFileName + " doesn't match regex";
    }


    public void selectElementNative(By by, int index) {
        LOG.info("SELECT Element '" + by.toString() + "'.");
        WebElement element = webDriver.findElement(by);
        Select select = new Select(element);
        select.selectByIndex(index);
        triggerChangeEvent(element);
    }

    private void triggerChangeEvent(WebElement webElement) {
        SysUtils.sleep(1000);
        executeJavaScript("var elem=arguments[0]; jQuery(\"#\"+elem.id).trigger(\"change\")", webElement);
        SysUtils.sleep(1000);
    }

    public void clearAndSendKeysViaKeyboard(By by, CharSequence message) {
        LOG.info("CLEAR field: '" + by.toString() + "' and TYPE text: '" + message + "' via KEYBOARD.");
        clearElement(by);
        WebElement element = webDriver.findElement(by);
        executeJavaScript("var elem=arguments[0]; elem.value = \"" + message + "\";", element);
        triggerChangeEvent(element);
    }

    public void typeKeysWithTrigger(By by, CharSequence message) {
        LOG.info("TYPE text: '" + message + "' via KEYBOARD.");
        WebElement element = webDriver.findElement(by);
        element.sendKeys(message);
        triggerChangeEvent(element);
    }

    private void clearElement(By by) {
        LOG.info("CLEAR field: '" + by.toString() + "'.");
        webDriver.findElement(by).clear();
    }

    public void sendKeys(By by, CharSequence message) {
        LOG.info("CLEAR field: '" + by.toString() + "' and TYPE text: '" + message + "'.");
        webDriver.findElement(by).sendKeys(message);
    }

    public void clearAndSendKeys(By by, CharSequence message) {
        LOG.info("CLEAR field: '" + by.toString() + "' and TYPE text: '" + message + "'.");
        final WebElement element = webDriver.findElement(by);
        element.clear();
        element.sendKeys(message);
    }

    public void scrollTo(By by) {
        LOG.error("Element location calculation incorrect implemented for appium version above 1.5");
    }

    /**
     * IOS Native Element Applying using UIAutomation.
     */

    private void waitForUIAutomationElementNotDisplayed(String by) {
        final Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withTimeout(30, TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS);
        wait.until(result -> {
            try {
                return !((IOSDriver) webDriver).findElementByIosUIAutomation(by).isDisplayed();
            } catch (WebDriverException var3) {
                return Boolean.TRUE;
            }
        });
    }

    private void clickUIAutomationElement(String by) {
        new WebDriverWait(webDriver, 10).until(result -> ((IOSDriver) webDriver).findElementByIosUIAutomation(by).isDisplayed());
        ((IOSDriver) webDriver).findElementByIosUIAutomation(by).click();
    }

    /**
     * IOS Native Element Applying using XCUI.
     */

    private void waitForXCUIElementNotDisplayed(By by, int timeoutInSec) {
        setImplicitlyWait(timeoutInSec, TimeUnit.SECONDS);
        final Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withMessage("Element " + by.toString() + " is still visible")
                .withTimeout(timeoutInSec, TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS);
        wait.until(result -> {
            try {
                return !webDriver.findElement(by).isDisplayed();
            } catch (WebDriverException var3) {
                return Boolean.TRUE;
            }
        });
        setImplicitlyWait(60, TimeUnit.SECONDS);
    }

    private boolean isXCUIElementDisplayed(By by) {
        setImplicitlyWait(5, TimeUnit.SECONDS);
        boolean isDisplayed = ((IOSDriver) webDriver).findElements(by).size() > 0;
        setImplicitlyWait(60, TimeUnit.SECONDS);
        return isDisplayed;
    }

    private void clickXCUIElement(By by) {
        new WebDriverWait(webDriver, 10).until(result -> webDriver.findElement(by).isDisplayed());
        webDriver.findElement(by).click();
    }

    private void longPressXCUIElement(By by) {
        new WebDriverWait(webDriver, 10).until(result -> webDriver.findElement(by).isDisplayed());
        WebElement webElement = webDriver.findElement(by);
        new TouchAction((AppiumDriver) webDriver).longPress(webElement).perform();
        waitForXCUIElementNotDisplayed(by, 2);
    }

    public void hideKeyboard() {
        try {
            ((IOSDriver) webDriver).hideKeyboard();
        } catch (Exception e) {
            LOG.info("Keyboard is not visible.");
        }

    }
}
