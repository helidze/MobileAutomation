package utils.mobile;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.*;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class MobileDriver {

    protected static final int APPIUM_PORT = 4723;
    protected static final long COMMAND_TIMEOUT = 12000;
    protected static final String BUNDLE_ID = "com.streamtechltd.fabulive1";
    private static final Logger LOG = Logger.getLogger(MobileDriver.class);
    protected WebDriver webDriver;
    protected AppiumDriver appiumDriver;

    public WebDriver getWebDriver() {
        return webDriver;
    }

    /**
     * Application Maintenance methods.
     */

    public abstract void startMobileServer();

    public abstract void startMobileDriver(String appLocation);

    public abstract void devDriverSwitchTo(String window);

    /**
     * WebDriver methods implements.
     */

    public void close() {
    }

    public void quit() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    public void waitForElement() {
        setImplicitlyWait(3, TimeUnit.SECONDS);
    }

    public String getCurrentUrl() {
        return webDriver.getCurrentUrl();
    }

    public String getTitle() {
        return webDriver.getTitle();
    }

    public List<WebElement> findElements(By by) {
        return webDriver.findElements(by);
    }

    public WebElement findElement(By by) {
        return webDriver.findElement(by);
    }

    public String getPageSource() {
        return webDriver.getPageSource();
    }

    public Set<String> getWindowHandles() {
        return webDriver.getWindowHandles();
    }

    public String getWindowHandle() {
        return webDriver.getWindowHandle();
    }

    public WebDriver.TargetLocator switchTo() {
        return webDriver.switchTo();
    }

    public WebDriver.Navigation navigate() {
        return webDriver.navigate();
    }

    public WebDriver.Options manage() {
        return webDriver.manage();
    }

    public void setImplicitlyWait(long time, TimeUnit unit) {
        webDriver.manage().timeouts().implicitlyWait(time, unit);
    }

    public Object executeJavaScript(String script, Object object) {
        return ((JavascriptExecutor) webDriver).executeScript(script, object);
    }

    public Object executeJavaScript(String script) {
        return ((JavascriptExecutor) webDriver).executeScript(script);
    }

    /**
     * Mobile Special methods.
     */

    public abstract void takePhoto();

    public abstract void verifyUploadedFileName(String actualFileName, String expectedFileName);

    public abstract void hideKeyboard();

    public abstract void selectElementNative(By by, int index);

    public abstract void sendKeys(By by, CharSequence message);

    public abstract void clearAndSendKeys(By by, CharSequence message);

    public abstract void clearAndSendKeysViaKeyboard(By by, CharSequence message);

    public abstract void typeKeysWithTrigger(By by, CharSequence message);

    public abstract void scrollTo(By by);

    public abstract void takeConfirm(String alertText);

    /**
     * Other.
     */

    public void takeScreenShot(ITestResult result) {
        addScreenShotToReport(result);
    }

    private String getScreenShotAndGenerateLinkForReport(WebDriver webDriver, ITestResult result) {
        String folder;
        if (result.isSuccess()) {
            folder = "success";
        } else {
            folder = "failed";
        }
        File file = new File("");
        getScreenShot(webDriver, file.getAbsolutePath() + "/target/surefire-reports/html/screenshots/" + folder + "/" + result.getName());
        try {
            FileUtils.copyDirectory(new File(file.getAbsolutePath() + "/target/surefire-reports/html/screenshots/" + folder + "/" + result.getName()), new File(file.getAbsolutePath() + "/resources/screenshots/" + folder + "/" + result.getName()));
        } catch (IOException e) {
            System.err.println(e);
        }
        String path = "screenshots/" + folder + "/" + result.getName() + "/screenshotMobile.png";

        String block = "<div> <a href='" + path + "'>" +
                "<img src='" + path + "' hight='100' width='100'/></a>" +
                "</div>";
        return block;
    }

    private void addScreenShotToReport(ITestResult result) {
        if (webDriver != null) {
            Reporter.setCurrentTestResult(result);
            Reporter.log(getScreenShotAndGenerateLinkForReport(webDriver, result));
        }
    }

    private void getScreenShot(WebDriver webDriver, String pathToFile) {
        LOG.info("Create screenshot for BrowserDriver..");
        if (webDriver == null) throw new NullPointerException("MobileDriver is undefined.");
        try {
            File failedTestCaseFolder = new File(pathToFile);
            failedTestCaseFolder.mkdirs();
            File scrFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File(failedTestCaseFolder, "screenshotMobile.png"));
            FileOutputStream fos = new FileOutputStream(new File(failedTestCaseFolder, "page_sourceMobile.html"));
            if (webDriver instanceof AndroidDriver) {
                devDriverSwitchTo(DriverWindows.WEBVIEW.getView());
            }
            fos.write(webDriver.getPageSource().getBytes());
            fos.close();
            LOG.info("Create folder: " + pathToFile);
            LOG.info("Next files are added to folder: screenshot and page_source");

        } catch (Exception e) {
            LOG.error("An error occurred during screenshot taking: " + e.getMessage());
        } finally {
            if (webDriver instanceof AndroidDriver) {
                devDriverSwitchTo(DriverWindows.NATIVE_APP.getView());
            }
        }
    }

    protected String executeRuntimeRequest(String request, boolean isWaitForResponse) {
        String environment = System.getProperty("os.name");
        LOG.info("OS NAME -> " + environment);

        String commandLine = null;
        String commandLineParameter = null;
        if (environment.startsWith("Windows")) {
            commandLine = "cmd";
            commandLineParameter = "/c";
        }
        if (environment.startsWith("Mac")) {
            commandLine = "/bin/sh";
            commandLineParameter = "-c";
        }
        assert commandLine != null;

        LOG.info("Execute request -> " + request);
        String processResponse = "";
        try {
            Process process = Runtime.getRuntime().exec(new String[]{commandLine, commandLineParameter, request});
            if (isWaitForResponse) {
                int processCode = process.waitFor();
                LOG.info("Process finished with code : " + processCode);

                BufferedReader stdOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String responseLine;
                while ((responseLine = stdOutput.readLine()) != null) {
                    processResponse = processResponse + responseLine + "\n";
                }
                if (!processResponse.equals("")) {
                    LOG.debug("Process response : " + processResponse);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return processResponse;
    }
}