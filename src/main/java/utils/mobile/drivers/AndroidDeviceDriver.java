package utils.mobile.drivers;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
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

import java.net.URL;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class AndroidDeviceDriver extends MobileDriver {

    private static final String ADB_LOCATION = System.getenv("ANDROID_HOME") + "\\platform-tools\\adb";

    private static Logger LOG = Logger.getLogger(AndroidDeviceDriver.class);

    private AppiumDriverLocalService appiumDriverLocalService;
    private IDevice iDevice;

    /**
     * Application Maintenance methods.
     */

    public void startMobileServer() {
        AndroidDebugBridge androidDebugBridge = getAndroidDebugBridge();
        iDevice = getDevice(androidDebugBridge);

        killAllProcessThatUsedPort();
        uninstallAppiumApplication();

        LOG.info("Start Appium Server.");
        appiumDriverLocalService = new AppiumServiceBuilder()
                .usingPort(APPIUM_PORT)
                .withIPAddress("127.0.0.1")
                .withArgument(GeneralServerFlag.LOG_LEVEL, "error")
                .build();
        appiumDriverLocalService.start();

        LOG.info("Appium server is successfully started on port: " + APPIUM_PORT);
        webDriver = null;
    }

    public void startMobileDriver(String appLocation) {
        LOG.info("Start Android Driver on device.");
        unLockDeviceScreen();
        webDriver = createAndroidDriver(appLocation);
    }

    private WebDriver createAndroidDriver(String appLocation) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, COMMAND_TIMEOUT);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, iDevice.getSerialNumber());
        capabilities.setCapability(MobileCapabilityType.UDID, iDevice.getSerialNumber());
        capabilities.setCapability(MobileCapabilityType.APP, appLocation);
        capabilities.setCapability(MobileCapabilityType.CLEAR_SYSTEM_FILES, true);
        capabilities.setCapability(AndroidMobileCapabilityType.RECREATE_CHROME_DRIVER_SESSIONS, true);
        capabilities.setCapability("autoGrantPermissions", true);
        LOG.info("#APP_LOCATION: " + appLocation);

        LOG.info("Start Android webDriver and launch application on device.");
        try {
            webDriver = new AndroidDriver(new URL("http://127.0.0.1:" + APPIUM_PORT + "/wd/hub"), capabilities);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert webDriver != null : "Problem with creation AndroidDriver instance. See log below.";
        return webDriver;
    }

    private boolean isAppiumServerRunning() {
        LOG.info("Check that appium server is running...");
        String isServerAliveOutput = executeRuntimeRequest("FOR /F \"tokens=5\" %P IN ('netstat -nao ^| findstr :" + APPIUM_PORT + "') " +
                "DO (FOR /F %b in ('TASKLIST /FI \"PID eq %P\" ^| findstr /I node.exe') do @echo %P)", true);
        return !isServerAliveOutput.isEmpty();
    }

    private void killAllProcessThatUsedPort() {
        LOG.info("Kill all process that used port for Appium server.");
        executeRuntimeRequest("FOR /F \"tokens=5 delims= \" %P " +
                "IN ('netstat -a -n -o ^| findstr :" + APPIUM_PORT + "') DO TaskKill.exe /F /PID %P", true);
    }

    private void uninstallAppiumApplication() {
        LOG.info("Delete previous application, selendroid and androiddriver from device.");
        executeRuntimeRequest("adb uninstall io.appium.settings", true);
        executeRuntimeRequest("adb uninstall io.appium.unlock", true);
    }

    private AndroidDebugBridge getAndroidDebugBridge() {
        LOG.info("Init AndroidDebugBridge via ADB.");
        AndroidDebugBridge androidDebugBridge = AndroidDebugBridge.getBridge();
        if (androidDebugBridge == null) {
            AndroidDebugBridge.init(false);
            androidDebugBridge = AndroidDebugBridge.createBridge(ADB_LOCATION, true);
        }
        return androidDebugBridge;
    }

    private IDevice getDevice(AndroidDebugBridge androidDebugBridge) {
        LOG.info("Init devices via ADB.");
        IDevice iDevice = null;
        for (int i = 0; i < 3; i++) {
            IDevice[] idevices = androidDebugBridge.getDevices();
            if (idevices.length != 0) {
                iDevice = idevices[0];
                LOG.info("Currently connected " + idevices.length + " device.");
                break;
            }
            SysUtils.sleep(1000);
        }
        assert iDevice != null : "The are no device connected !";
        return iDevice;
    }

    public void devDriverSwitchTo(String window) {
        Set<String> contextHandles = ((AndroidDriver) webDriver).getContextHandles();
        LOG.info("Available context size = " + contextHandles.size());
        for (String context : contextHandles) {
            if (context.contains(window)) {
                ((AndroidDriver) webDriver).context(context);
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
        if (webDriver != null) {
            webDriver.quit();
            webDriver = null;
        }
        if (isAppiumServerRunning()) {
            appiumDriverLocalService.stop();
        }
        killAllProcessThatUsedPort();
        AndroidDebugBridge.disconnectBridge();
    }

    /**
     * Implement Mobile Special methods.
     */

    public void takePhoto() {
        LOG.info("Making photo on the android device:");
        devDriverSwitchTo(DriverWindows.NATIVE_APP.getView());
        if (isDeviceLocked()) {
            unLockDeviceScreen();
        }
        allowCameraAccess();
        clickShutterButton();
        clickOnDoneButton();
        devDriverSwitchTo(DriverWindows.WEBVIEW.getView());
    }

    private void allowCameraAccess() {
        setImplicitlyWait(1, TimeUnit.SECONDS);
        if (isElementDisplayed(By.id("com.android.packageinstaller:id/permission_message"))) {
            LOG.info("Allow camera permission for application.");
            click(By.id("com.android.packageinstaller:id/permission_allow_button"));
        }
        setImplicitlyWait(60, TimeUnit.SECONDS);
    }

    private void unLockDeviceScreen() {
        executeRuntimeRequest("adb shell input keyevent 82", true);
    }

    private boolean isDeviceLocked() {
        String deviceScreenState = executeRuntimeRequest("adb shell dumpsys nfc | findstr mScreenState", true);
        return !(deviceScreenState.contains("mScreenState=ON") || deviceScreenState.isEmpty());
    }

    private void clickShutterButton() {
        By shutterButtonBy = By.id("com.android.camera2:id/shutter_button");
        By transitionModeBy = By.id("com.android.camera2:id/mode_transition_view");
        waitForElementDisplayed(shutterButtonBy);
        waitForElementNotVisibleByTime(transitionModeBy, 5);
        click(shutterButtonBy);
        waitForElementNotVisibleByTime(shutterButtonBy, 5);
    }

    private void clickOnDoneButton() {
        By doneButtonBy = By.id("com.android.camera2:id/done_button");
        waitForElementDisplayed(doneButtonBy);
        click(doneButtonBy);
        waitForElementNotVisibleByTime(doneButtonBy, 5);
    }

    public void verifyUploadedFileName(String actualFileName, String expectedFileName) {
        assert actualFileName.matches("\\d{13}.jpg") : "Filename " + actualFileName + " doesn't match regex";
    }

    public boolean isElementDisplayed(By by) {
        try {
            return webDriver.findElement(by).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private void waitForElementDisplayed(final By locator) {
        WebDriverWait wait = new WebDriverWait(webDriver, 60);
        wait.until(result -> webDriver.findElement(locator).isDisplayed());
    }

    private void click(By locator) {
        WebElement element = webDriver.findElement(locator);
        element.click();
    }

    private void waitForElementNotVisibleByTime(final By element, long seconds) {
        setImplicitlyWait(seconds, TimeUnit.SECONDS);
        final Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withMessage("Element " + element.toString() + " is still visible")
                .withTimeout(seconds, TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS);
        wait.until(result -> {
            try {
                return !webDriver.findElement(element).isDisplayed();
            } catch (NoSuchElementException | StaleElementReferenceException var3) {
                return Boolean.TRUE;
            }
        });
        setImplicitlyWait(60, TimeUnit.SECONDS);
    }

    public void hideKeyboard() {
        try {
            ((AndroidDriver) webDriver).hideKeyboard();
        } catch (Exception e) {
            LOG.info("Keyboard is not visible.");
        }
    }

    public void selectElementNative(By by, int index) {
        final WebElement element = webDriver.findElement(by);
        scrollTo(by);
        hideTopButtons();
        Select select = new Select(element);
        select.selectByIndex(index);
        unHideTopButtons();
    }

    public void sendKeys(By by, CharSequence message) {
        final WebElement element = webDriver.findElement(by);
        scrollTo(by);
        element.sendKeys(message);
        hideKeyboard();
    }

    public void clearAndSendKeys(By by, CharSequence message) {
        final WebElement element = webDriver.findElement(by);
        scrollTo(by);
        element.clear();
        element.sendKeys(message);
        hideKeyboard();
    }

    public void clearAndSendKeysViaKeyboard(By by, CharSequence message) {
        clearAndSendKeys(by, message);
    }

    public void typeKeysWithTrigger(By by, CharSequence message) {
        sendKeys(by, message);
    }

    public void scrollTo(By by) {
        final WebElement element = webDriver.findElement(by);
        new TouchAction((AndroidDriver) webDriver).moveTo(element).release();
    }

    /**
     * Other.
     */

    private void hideTopButtons() {
        try {
            executeJavaScript("document.getElementById('formBackCont').style.display='none';");
        } catch (Exception e) {
            LOG.info("Can't hide top buttons. " + e.getMessage());
        }
        SysUtils.sleep(1000);
    }

    private void unHideTopButtons() {
        executeJavaScript("document.getElementById('formBackCont').style.display='';");
    }


}
