/*
package utils.mobile.drivers;

import config.AppConfig;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
*/
/*import org.sikuli.script.App;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;*//*

import utils.AppUtil;
import utils.SysUtils;
*/
/*import utils.factories.BuildAgentsFactory;*//*

import utils.mobile.MobileDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class DesktopDeviceDriver extends MobileDriver {

    private static Logger LOG = Logger.getLogger(DesktopDeviceDriver.class);
    private static final String CHROME_DRIVER_FILE_NAME = "chromedriver.exe";
    private static final String CHROME_DRIVER_FOLDER = "chrome_profile";
    private static final String EXECUTION_FILE = "runWinDesktop.bat";
    private final static String pathToChromeDriverFile = AppUtil.getRelativePathToImageWithClass(DesktopDeviceDriver.class.getClassLoader().getResource("" + CHROME_DRIVER_FOLDER + "/" + CHROME_DRIVER_FILE_NAME));

    public DesktopDeviceDriver() {
        System.setProperty("webdriver.chrome.driver", pathToChromeDriverFile);
    }

    */
/**
 * Application Maintenance methods.
 * <p>
 * Overwrite WebDriver methods.
 * <p>
 * Implement Mobile Special methods.
 *//*


    private void runDesktopApplication(String appLocation) {
        List<String> command = AppConfig.isUseGrid() ? getSSHCommandWithParams("schtasks /Run /TN runWindowsDesktop") : Arrays.asList("cmd", "/c", EXECUTION_FILE);
        File dir1 = new File(appLocation);
        ProcessBuilder pb1 = new ProcessBuilder(command);
        try {
            SysUtils.sleep(1000);
            pb1.directory(dir1).start().waitFor();
        } catch (Exception e) {
            LOG.error("Can not run process from file: " + appLocation);
        }
    }

    private WebDriver startDesktopDriver() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("debuggerAddress", "localhost:9999");
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--enable-app-install-alerts");
        chromeOptions.addArguments("--disable-popup-blocking");
        chromeOptions.addArguments("--no-sandbox");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(CapabilityType.SUPPORTS_ALERTS, true);
        capabilities.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
        capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.DISMISS);
        if (AppConfig.isUseGrid()) {
            webDriver = new RemoteWebDriver(getUrlForRemoteWebDriver(), capabilities);
            ((RemoteWebDriver) webDriver).setFileDetector(new LocalFileDetector());
            return webDriver;
        } else {
            return new ChromeDriver(capabilities);
        }
    }

    public void startMobileDriver(String appLocation) {
        LOG.info("Start Desktop Application");
        runDesktopApplication(appLocation);
        SysUtils.sleep(5000);
        webDriver = startDesktopDriver();
    }

    public void startMobileServer() {
    }

    public void devDriverSwitchTo(String window) {
    }

    */
/**
 * Overwrite WebDriver methods.
 *//*


 */
/* public void close() {
        if (AppConfig.isUseGrid()) {
            executeSSHCommand("cmd /c taskkill /F /IM WorldAPPDeskTop.exe /T");
            webDriver.quit();
        } else {
            executeRuntimeRequest("taskkill /F /IM WorldAPPDeskTop.exe /T", false);
        }
    }*//*


    public void quit() {
        webDriver.quit();
    }

    */
/**
 * Implement Mobile Special methods.
 *//*


    public void takePhoto() {
    }

    public void verifyUploadedFileName(String actualFileName, String expectedFileName) {
        assert actualFileName.equals(expectedFileName) : "Expected: " + expectedFileName + " but found " + actualFileName;
    }

    public void hideKeyboard() {
    }

    public void selectElementNative(By by, int index) {
        new Select(webDriver.findElement(by)).selectByIndex(index);
    }

    public void sendKeys(By by, CharSequence message) {
        webDriver.findElement(by).sendKeys(message);
    }

    public void clearAndSendKeys(By by, CharSequence message) {
        webDriver.findElement(by).clear();
        webDriver.findElement(by).sendKeys(message);
    }

    public void clearAndSendKeysViaKeyboard(By by, CharSequence message) {
        clearAndSendKeys(by, message);
    }

    public void typeKeysWithTrigger(By by, CharSequence message) {
        sendKeys(by, message);
    }

    public void scrollTo(By by) {
    }

    public void takeConfirm() {
    }

   */
/* public void takeConfirm(String expectedAlert) {
        String uploadFileLocation = AppUtil.getRelativePathToImageWithClass(DesktopDeviceDriver.class.getClassLoader().getResource("test/sikuliImages/alerts/"));
        Screen screen = new Screen();
        try {
            LOG.info(Screen.getNumberScreens());
            App desktop = new App("Form.com Desktop");
            desktop.focus();
            screen.wait(uploadFileLocation + expectedAlert, 20);
            screen.click(uploadFileLocation + "OkButtonInAlert.png");
        } catch (FindFailed findFailed) {
            findFailed.printStackTrace();
            assert false;
        }
    }*//*


 */
/* private void executeSSHCommand(String command) {
        List<String> commandWithParams = getSSHCommandWithParams(command);
        ProcessBuilder pb1 = new ProcessBuilder(commandWithParams);
        try {
            pb1.start().waitFor();
        } catch (IOException | InterruptedException e) {
            LOG.error("Can not execute command: " + command);
        }
    }*//*


 */
/* private List<String> getSSHCommandWithParams(String command) {
        String nodeIp = BuildAgentsFactory.getBuildAgent(AppConfig.getBuildAgentName()).getNodeIp();
        return Arrays.asList("sshpass", "-p", "Abs3tree6mount42", "ssh", "teamcity-automation@" + nodeIp, command);
    }*//*


    private URL getUrlForRemoteWebDriver() {
        String ipAddress = SysUtils.getLocalIP();
        URL url = null;
        try {
            url = new URL("http://" + ipAddress + ":4444/wd/hub");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}
*/
