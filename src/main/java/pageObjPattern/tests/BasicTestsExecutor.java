package pageObjPattern.tests;


import config.AppConfig;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pageObjPattern.CustomWriterAppender;
import pageObjPattern.basePage.MainPage;
import pageObjPattern.pages.login.LoginPage;
import utils.AppUtil;
import utils.SysUtils;
import utils.testLink.TestLinkUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicTestsExecutor extends Assert {

    protected Logger LOG = Logger.getLogger(pageObjPattern.tests.BasicTestsExecutor.class);

    protected WebDriver webDriver;
    protected MainPage mainPage;
    protected long userID;

    private String testName;
    private long testStartTime;

    private static TestLinkUtils testLinkUtils;
    private int testCaseId;
    private int testCaseExternalId;

    public String getTestName() {
        return testName;
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public BasicTestsExecutor() {
    }

    public BasicTestsExecutor(WebDriver webDriver) {
        this.webDriver = webDriver;
    }



    @BeforeMethod
    public void doBeforeMethod(Method method) {
        testName = method.getName();
        testStartTime = System.currentTimeMillis() / 1000;
        Thread.currentThread().setName(testName);

        LOG.info("\nTest method: " + testName + " from Class :" + this.getClass().getSimpleName() + " is start execution!\n");

        if (AppConfig.getProcessingResult()) {
            setTestLinkCaseIds(method);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void doAfterMethodBasic(Method method, ITestResult result) throws Exception {
        long testEndTime = System.currentTimeMillis() / 1000;

        if (result.isSuccess()) {
            LOG.info("\nTest method: " + method.getName() + " from Class :" + this.getClass().getSimpleName() + " is passed. Test execution time " + (testEndTime - testStartTime) + " sec.");
        } else {
            LOG.info("\nTest method: " + method.getName() + " from Class :" + this.getClass().getSimpleName() + " is failed. Test execution time " + (testEndTime - testStartTime) + " sec.");
        }

        if (AppConfig.getProcessingResult()) {
            addResultsToTestLink(method, result);
        }

        addScreenShotToReport(result);
        addWarningBlock(result);
        addLogBlock(result);
    }

    @AfterClass(alwaysRun = true)
    public void haltSessions() {
        closeBrowserSession();
    }

    public void closeBrowserSession() {
        if (webDriver != null) {
            if (((RemoteWebDriver) webDriver).getSessionId() != null) {
                ifAlertPresentAcceptHim(webDriver);
                try {
                    webDriver.close();
                } catch (NoSuchSessionException nsse) {
                    LOG.info("Can't close chrome driver due to: " + nsse.getMessage());
                }
            }
            webDriver.quit();
        }
    }


    public WebDriver checkWebDriver(MainPage mainPage) {
        try {
            if (mainPage == null) {
                return null;
            }
            if (mainPage.getWebDriver() == null) {
                return null;
            }
            mainPage.getWebDriver().getTitle();
            return webDriver;
        } catch (WebDriverException e) {
            return null;
        }
    }

    private void addResultsToTestLink(Method method, ITestResult result) {
        if ((testCaseId != 0) || (testCaseExternalId != 0)) {
            String notes = testLinkUtils.generateNotesMessageForTestLink(method, result, 250);
            testLinkUtils.passResultsToTestLink(result, testCaseId, testCaseExternalId, notes);
            System.out.println(method.getName() + "-> Testlink. Test result is added to TestLink!");
        } else {
            System.out.println(method.getName() + "-> Testlink. Test result do not send to Testlink! Test Case id is not define.");
            LOG.info("Internal Id: " + testCaseId);
            LOG.info("External Id: " + testCaseExternalId);
        }
    }

    private int[] getUniqueIdsByTestCaseName(String tcValue) {
        int[] result = new int[2];
        boolean found = false;
        try {
            List<String> csvExportList = FileUtils.readLines(new File("target/test-classes/test/testlinkSuite/AutomationTestsProcessing.txt"), "utf-8");
            LOG.info("Count of test cases: " + csvExportList.size());
            for (String value : csvExportList) {
                String[] params = value.split(",");
                String testCaseName = params[0];
                if (testCaseName.equals(tcValue)) {
                    LOG.info("Processing value: " + value);
                    result[0] = Integer.valueOf(params[1]);
                    result[1] = Integer.valueOf(params[2]);
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        } catch (Exception e) {
            LOG.info("Can not get test case id: " + e.getMessage());
        }
        return result;
    }

    private void setTestLinkCaseIds(Method method) {
        int[] idList = getUniqueIdsByTestCaseName("AUT - " + testName);
        if (idList != null) {
            testCaseId = idList[0];
            testCaseExternalId = idList[1];
        } else {
            System.err.println(method.getName() + "-> Testlink. Test case id is undefined. Please check it existing in Testlink.");
        }
    }

    private String getScreenShotAndGenerateLinkForReport(ITestResult result) {
        String screenShotReportBlock = "";
        if (!result.isSuccess()) {
            String projectAbsolutePath = System.getProperty("user.dir");
            String screenShotTargetFolderPath = projectAbsolutePath + "/target/surefire-reports/html/screenshots/" + result.getName();
            getScreenShot(screenShotTargetFolderPath);
            try {
                FileUtils.copyDirectory(new File(screenShotTargetFolderPath), new File(projectAbsolutePath + "/target/test-classes/screenshots/" + result.getName()));
            } catch (IOException e) {
                LOG.error("Can not copy screenshots folder from target  to resources, due to error: " + e.getMessage());
            }
            String screenShotImagePath = "screenshots/" + result.getName() + "/screenshot.png";
            screenShotReportBlock = String.format("<div> <a href='%s'><img src='%s' hight='100' width='100'/></a></div>", screenShotImagePath, screenShotImagePath);
        }
        return screenShotReportBlock;
    }

    private String generateLogBlockForReport(ITestResult result) {
        String link = "<a href=\"javascript:toggleElement('" + result.getName() + "-log', 'block')\" title=\"Click to expand/collapse\">" +
                "<b style=\"color:green\">Test LOG</b>" +
                "</a>";
        String block = "<div id = \"" + result.getName() + "-log\" style = \"display: none;\">" + CustomWriterAppender.getBufferContents(result.getName()) + "</div>";
        return link + block;
    }

    private String generateWarningBlock() {
        String fileName = "warning.png";
        File file = new File("");
        try {
            FileUtils.copyFileToDirectory(new File(file.getAbsolutePath() + "/target/test-classes/" + fileName ), new File(file.getAbsolutePath() + "/target/surefire-reports/html/"));
        } catch (IOException e) {
            LOG.info(e.getMessage());
        }
        String path = "warning.png";
        return String.format("<div> <a href='%s'><img src='%s' hight='100' width='100'/></a></div>", path, path);
    }

    public void addScreenShotToReport(ITestResult result) {
        Reporter.setCurrentTestResult(result);
        addScreenshotToReport(result);
    }

    private void addWarningBlock(ITestResult result) {
        long executionTime = (result.getEndMillis() - result.getStartMillis()) / 1000;
        if (executionTime > 450) {
            Reporter.log(generateWarningBlock());
            LOG.info("WARNING. EXECUTION TIME LIMIT: " + result.getMethod().getMethodName());
        }
    }

    private void addLogBlock(ITestResult result) {
        Reporter.log(generateLogBlockForReport(result));
    }

    private void addScreenshotToReport(ITestResult result) {
        if (webDriver != null) {
            if (((RemoteWebDriver) webDriver).getSessionId() != null) {
                Reporter.log(getScreenShotAndGenerateLinkForReport(result));
                ifAlertPresentAcceptHim(webDriver);
                webDriver.getTitle();

            }
        }
    }




    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    protected boolean isElementPresent(By element) {
        try {
            webDriver.findElement(element);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    protected boolean isElementPresent(WebElement element) {
        try {
            return element != null;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isTextPresentFromElement(WebDriver webDriver, String text) {
        webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        boolean v = false;
        try {
            v = webDriver.findElement(By.xpath("//*[contains(text(),'" + text + "')]")).isDisplayed();
            webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            return v;
        } catch (Exception e) {
            webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            return v;
        }
    }



    protected boolean isTextPresentInBody(String text) {
        try {
            return webDriver.findElement(By.tagName("body")).getText().contains(text);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected boolean isTextPresentFromPageSource(String text) {
        try {
            return webDriver.getPageSource().contains(text);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void waitForTextPresent(String text) {
        final int waitRetryDelayMs = 100; //шаг итерации (задержка)
        final int timeOut = 500;  //время тайм маута
        boolean first = true;
        try {
            for (int milliSecond = 0; ; milliSecond += waitRetryDelayMs) {
                if (milliSecond > timeOut * 100) {
                    LOG.info("Timeout: Text " + text + " is not found during " + milliSecond / 1000 + " sec.");
                    break; //если время ожидания закончилось (элемент за выделенное время не был найден)
                }
                if (webDriver.getPageSource().contains(text)) {
                    if (!first) LOG.info("Text is found: " + text + "");
                    break; //если элемент найден
                }
                if (first) LOG.info("Waiting for text is present: " + text + "");
                first = false;
                Thread.sleep(waitRetryDelayMs);
            }
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    protected void waitForTextNotPresent(String text) throws Exception {
        final int waitRetryDelayMs = 100; //шаг итерации (задержка)
        final int timeOut = 500;  //время тайм маута
        boolean first = true;
        try {
            for (int milliSecond = 0; ; milliSecond += waitRetryDelayMs) {
                if (milliSecond > timeOut * 100) {
                    LOG.info("Timeout: Text '" + text + "' is still here after " + milliSecond / 1000 + " sec.");
                    break;
                }
                if (!webDriver.getPageSource().contains(text)) {
                    if (first) LOG.info("Text is gone: '" + text + "");
                    break; //если элемент найден
                }
                if (!first) LOG.info("Waiting for text not present: '" + text + "");
                first = false;
                Thread.sleep(waitRetryDelayMs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void moveToElement(By path) {
        Actions actions = new Actions(webDriver);
        actions.moveToElement(webDriver.findElement(path)).build().perform();
    }

    public void moveToElement(WebElement element) {
        Actions actions = new Actions(webDriver);
        actions.moveToElement(element).build().perform();
    }

    public void setImplicitlyWait(long time, TimeUnit unit) {
        webDriver.manage().timeouts().implicitlyWait(time, unit);
    }

    public void waitForElementDisplayed(final WebElement element) {
        WebDriverWait wait = new WebDriverWait(webDriver, 30);
        wait.until(result -> element.isDisplayed());
    }

    public void waitForElementDisplayed(final WebElement element, long time) {
        WebDriverWait wait = new WebDriverWait(webDriver, time);
        wait.until(result -> element.isDisplayed());
    }

    public void waitForElementDisplayed(final By locator) {
        WebDriverWait wait = new WebDriverWait(webDriver, 60);
        wait.until(result -> webDriver.findElement(locator).isDisplayed());
    }

    public void waitForElementDisplayed(final By locator, long time) {
        WebDriverWait wait = new WebDriverWait(webDriver, time);
        wait.until(result -> webDriver.findElement(locator).isDisplayed());
    }

    public void waitForElementDisplayedByTime(final By locator, int seconds) {
        WebDriverWait wait = new WebDriverWait(webDriver, seconds);
        wait.until(result -> webDriver.findElement(locator).isDisplayed());
    }

    protected boolean isElementVisibleByTime(By locator, long MILLISECONDS) {
        webDriver.manage().timeouts().implicitlyWait(MILLISECONDS, TimeUnit.MILLISECONDS);
        try {
            return webDriver.findElement(locator).isDisplayed();//elementIsVisible(findElement(locator, webDriver));
        } catch (NoSuchElementException e) {
            LOG.debug("Unable to locate element: " + locator.toString());
            return false;
        } catch (ElementNotVisibleException env) {
            LOG.debug("Element is not visible: " + locator.toString());
            return false;
        } finally {
            webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        }
    }

    public void waitForElementNotVisible(final WebElement element) {
        final Wait<WebDriver> wait = new FluentWait<>(webDriver).withMessage("Element " + element.getTagName() + " is still visible")
                .withTimeout(10, TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS);
        wait.until(result -> {
            try {
                return !element.isDisplayed();
            } catch (NoSuchElementException | StaleElementReferenceException var3) {
                return Boolean.TRUE;
            }
        });
    }

    public void waitForElementNotVisible(final By element) {
        final Wait<WebDriver> wait = new FluentWait<>(webDriver).withMessage("Element " + element + " is still visible")
                .withTimeout(30, TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS);
        wait.until(result -> {
            try {
                return !webDriver.findElement(element).isDisplayed();
            } catch (NoSuchElementException | StaleElementReferenceException var3) {
                return Boolean.TRUE;
            }
        });
    }

    public void waitForElementNotVisibleByTime(final By element, long seconds) {
        final Wait<WebDriver> wait = new FluentWait<>(webDriver).withMessage("Element " + element.toString() + " is still visible")
                .withTimeout(seconds, TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS);
        wait.until(result -> {
            try {
                return !webDriver.findElement(element).isDisplayed();
            } catch (NoSuchElementException | StaleElementReferenceException var3) {
                return Boolean.TRUE;
            }
        });
    }

    public static Alert waitForAlert(WebDriver webDriver, int seconds) {
        Wait<WebDriver> wait = new WebDriverWait(webDriver, seconds);
        return wait.until(alert -> webDriver.switchTo().alert());
    }

    protected void waitForNewWindow(int previousNumOfWindows, int timeOutInSeconds) {
        for (int i = 0; i < timeOutInSeconds; i++) {
            int waitForCountWindows = previousNumOfWindows + 1;
            if (webDriver.getWindowHandles().size() == waitForCountWindows) {
                LOG.info("New window is opened!");
                break;
            } else {
                LOG.info("Wait " + i + " for new window...");
                SysUtils.sleep(1000);
            }
        }
    }

    protected void switchToNewWindow(String currentWindow) {
        webDriver.getWindowHandles().stream().filter(winHandle -> !winHandle.equals(currentWindow))
                .forEach(winHandle -> webDriver.switchTo().window(winHandle).manage().window().maximize());
    }

    public void ifAlertPresentAcceptHim(WebDriver webDriver) {
        if (isAlertPresent(webDriver)) {
            Alert al = webDriver.switchTo().alert();
            al.accept();
        }
    }

    public Alert returnAlertIfPresent() {
        try {
            return webDriver.switchTo().alert();
        } catch (NoAlertPresentException e) {
            return null;
        }
    }

    public boolean isAlertPresent(WebDriver webDriver) {
        try {
            webDriver.switchTo().alert();
            return true;
        } catch (Exception Ex) {
            return false;
        }
    }

    public void getConfirmation() {
        try {
            Alert alert = webDriver.switchTo().alert();
            if (alert != null) {
                alert.accept();
            }
        } catch (NoAlertPresentException e) {
            LOG.info("Alert is not present here.");
        }
    }

    public void takeConfirm() {
        try {
            Alert alert = waitForAlert(webDriver, 15);
            if (alert != null) {
                LOG.info("Accept alert with text : '" + alert.getText() + "'.");
                webDriver.switchTo().alert().accept();
            }
        } catch (NoAlertPresentException | TimeoutException timeoutException) {
            LOG.error("Alert is not present.");
        }
    }

    public void dismissConfirm() {
        try {
            Alert alert = waitForAlert(webDriver, 5);
            if (alert != null) {
                LOG.info("Decline alert with text : '" + alert.getText() + "'.");
                alert.dismiss();
            }
        } catch (NoAlertPresentException e) {
            LOG.error("Alert is not present.");
        }
    }

    private void getScreenShot(String pathToFile) {
        try {
            File failedTestCaseFolder = new File(pathToFile);
            failedTestCaseFolder.mkdirs();
            File scrFile;
            if (webDriver.getClass().getSimpleName().equals("RemoteWebDriver")) {
                Augmenter augmenter = new Augmenter();
                TakesScreenshot ts = (TakesScreenshot) augmenter.augment(webDriver);
                scrFile = ts.getScreenshotAs(OutputType.FILE);
            } else {
                scrFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
            }
            FileUtils.copyFile(scrFile, new File(failedTestCaseFolder, "screenshot.png"));
            FileOutputStream fos = new FileOutputStream(new File(failedTestCaseFolder, "page_source.html"));
            fos.write(webDriver.getPageSource().getBytes());
            fos.close();

        } catch (Exception e) {
            System.err.println("An error occurred during screen shot taking: " + e.getMessage());
        }
    }

    public void refreshPage() {
        LOG.info("Refreshing the page");
        webDriver.navigate().refresh();
        waitForPageLoaded(webDriver);
    }

    public void refreshJavaScript() {
        LOG.info("Performing javascript refresh in browser...");
        new Actions(webDriver).keyDown(Keys.CONTROL).sendKeys(Keys.F5).keyUp(Keys.CONTROL).perform();
    }

    protected boolean isChromeDriver(WebDriver webDriver) {
        Capabilities cap = ((RemoteWebDriver) webDriver).getCapabilities();
        String browsername = cap.getBrowserName();
        return "chrome".equalsIgnoreCase(browsername);
    }

    public boolean isElementVisible(final By locator) {
        try {
            return webDriver.findElement(locator).isDisplayed();//elementIsVisible(findElement(locator, webDriver));
        } catch (Exception e) {
            LOG.error("Unable to locate element: " + locator.toString());
            return false;
        }
    }

    public boolean isElementVisible(final By locator, String info) {
        try {
            return webDriver.findElement(locator).isDisplayed();//elementIsVisible(findElement(locator, webDriver));
        } catch (NoSuchElementException | ElementNotVisibleException e) {
            LOG.error(e.getClass().getSimpleName() + " ." + info);
        }
        return false;
    }

    public boolean isElementVisible(final WebElement element) {
        try {
            return element.isDisplayed();
        } catch (ElementNotVisibleException env) {
            LOG.error(env.getMessage());
            return false;
        }
    }

    public boolean isElementVisible(WebDriver webDriver, final WebElement element) {
        this.webDriver = webDriver;
        try {
            return element.isDisplayed();
        } catch (ElementNotVisibleException env) {
            LOG.error(env.getMessage());
            return false;
        }
    }

    protected WebElement findElement(By by) {
        try {
            return webDriver.findElement(by);
        } catch (NoSuchElementException e) {
            return null;
        } catch (WebDriverException e) {
            LOG.info(String.format("WebDriverException thrown by findElement(%s)", by), e);
            return null;
        }
    }

    public void setSelectStatusForElement(boolean selectStatus, WebElement element) {
        if (selectStatus) {
            if (!element.isSelected()) {
                element.click();
            }
        } else {
            if (element.isSelected()) {
                element.click();
            }
        }
    }

    public void executeJavaScript(String script, Object... objects) {
        if (objects == null) ((JavascriptExecutor) webDriver).executeScript(script);
        else ((JavascriptExecutor) webDriver).executeScript(script, objects);
    }

    public Object executeJavaScript(String script) {
        return ((JavascriptExecutor) webDriver).executeScript(script);
    }

    public void executeScript(String file) throws Throwable {
        // create a script engine manager
        ScriptEngineManager factory = new ScriptEngineManager();
        // create a JavaScript engine
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        // evaluate JavaScript code from String
        engine.eval(new FileReader(file));
    }

    public void scrollToElementWithJS(By element) {
        Point point = webDriver.findElement(element).getLocation();
        executeJavaScript("window.scrollTo(" + point.getX() + "," + (point.getY() - 200) + ");");
    }

    protected void scrollIntoViewById(String elementId) {
        executeJavaScript("return document.getElementById('" + elementId + "').scrollIntoView()");
    }

    public void switchToWindowByWindowTitle(String windowTitle) {
        for (String windowHandle : webDriver.getWindowHandles()) {
            WebDriver popup = webDriver.switchTo().window(windowHandle);
            if (popup.getTitle().contains(windowTitle)) {
                break;
            }
        }
    }

    public void switchToFrameByName(String frameName) {
        WebElement element = webDriver.findElement(By.id(frameName));
        webDriver.switchTo().frame(element);
    }


    public void switchToDefaultContent() {
        webDriver.switchTo().defaultContent();
    }

    protected void maximizeWindow() {
        webDriver.manage().window().maximize();
    }

    public void waitForPageLoaded(WebDriver webDriver) {
        Wait<WebDriver> wait = new WebDriverWait(webDriver, 100);
        try {
            wait.until(result -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        } catch (Throwable error) {
            fail("Timeout waiting for Page Load Request to complete.", error);
        }
    }

    public void waitForPageLoadedByTime(WebDriver webDriver, long TIME) {
        Wait<WebDriver> wait = new WebDriverWait(webDriver, TIME);
        try {
            wait.until(result -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        } catch (Throwable error) {
            fail("Timeout waiting for Page Load Request to complete.", error);
        }
    }

    public void waitAjax(int maxCounter) {
        SysUtils.sleep(1500);
        setImplicitlyWait(2, TimeUnit.SECONDS);
        if (isElementVisible(By.id("wait_div_load"))) {
            String[] str = webDriver.findElement(By.id("wait_div_load")).getAttribute("style").split("; ");
            String rightStatus = "visibility: hidden";
            int counter = 0;

            if (str[7].equals(rightStatus)) {
            } else {
                while (str[7].equals("visibility: visible")) {
                    counter++;
                    str = webDriver.findElement(By.id("wait_div_load")).getAttribute("style").split("; ");
                    if (counter == maxCounter) {
                        break;
                    }
                    if (str[7].equals(rightStatus)) {
                        break;
                    }
                    SysUtils.sleep(1500);
                }
            }
        } else {
            LOG.info("Sunload element isn't present");
        }
        setImplicitlyWait(30, TimeUnit.SECONDS);
    }

    public void waitForAjax(int timeoutInSeconds) {
        LOG.info("Wait for ajax ..");
        Wait<WebDriver> wait = new WebDriverWait(webDriver, timeoutInSeconds);
        try {
            wait.until(result -> {
                JavascriptExecutor js = (JavascriptExecutor) webDriver;
                return (Boolean) js.executeScript("return (window.jQuery != null) && (jQuery.active == 0)");
            });
        } catch (TimeoutException exception) {
            LOG.info("Ajax not finished");
        }
    }

    public void waitForAjax(WebDriver webDriver, int timeoutInSeconds) {
        LOG.info("Wait for ajax ..");
        Wait<WebDriver> wait = new WebDriverWait(webDriver, timeoutInSeconds);
        try {
            wait.until(result -> {
                JavascriptExecutor js = (JavascriptExecutor) webDriver;
                return (Boolean) js.executeScript("return (window.jQuery != null) && (jQuery.active == 0)");
            });
        } catch (TimeoutException exception) {
            LOG.info("Ajax not finished");
        }
    }

    public void waitForFinishResizePopup() {
        int counter = 0;
        while (counter < 3) {
            long beforeWaitTimeMillis = System.currentTimeMillis();
            waitForResizeFinishing();
            long afterWaitTimeMillis = System.currentTimeMillis();
            SysUtils.sleep(1000);
            counter++;
            LOG.info("Try # " + counter + " waiting: " + (afterWaitTimeMillis - beforeWaitTimeMillis) + " ms");
        }
    }

    private void waitForResizeFinishing() {
        Wait<WebDriver> wait = new WebDriverWait(webDriver, 2);
        try {
            wait.until(result -> {
                JavascriptExecutor js = (JavascriptExecutor) webDriver;
                return (Long) js.executeScript("return document.getElementsByClassName('ui_FinishResizePopup').length") == 1;
            });
        } catch (TimeoutException exception) {
            LOG.info("Ajax not finished");
        }
    }

    public void waitForAjax(int timeoutInSeconds, String ajaxInfo) {
        LOG.info("Wait for ajax ..");
        if (AppUtil.isStringEmpty(ajaxInfo)) waitForAjax(timeoutInSeconds);
        try {
            if (webDriver instanceof JavascriptExecutor) {
                JavascriptExecutor jsDriver = (JavascriptExecutor) webDriver;
                for (int i = 0; i < timeoutInSeconds; i++) {
                    Object numberOfAjaxConnections = jsDriver.executeScript("return jQuery.active");
                    if (numberOfAjaxConnections instanceof Long) {
                        Long n = (Long) numberOfAjaxConnections;
                        if (0L == n)
                            break;
                        else LOG.info(ajaxInfo + i);
                    }
                    Thread.sleep(1000);

                }
            } else {
                LOG.info("Web driver: cannot execute javascript");
                throw new TimeoutException();
            }
        } catch (InterruptedException e) {
            LOG.info(e);
        }
    }

    public void waitForNewWindowIsOpened(final int TIMETOWAITINSECONDS, final int previousNumOfWindows) {
        FluentWait myWait = new WebDriverWait(webDriver, TIMETOWAITINSECONDS).pollingEvery(1, TimeUnit.SECONDS);
        myWait.until(result -> webDriver.getWindowHandles().size() == previousNumOfWindows + 1);
    }

    public void waitForNewWindow(WebDriver webDriver, int previousNumOfWindows, int TIMETOWAITINSECONDS) {
        for (int i = 0; i < TIMETOWAITINSECONDS; i++) {
            int waitForCountWindows = previousNumOfWindows + 1;
            if (webDriver.getWindowHandles().size() == waitForCountWindows) {
                LOG.info("New window is opened!");
                break;
            } else {
                LOG.info("Wait " + i + " for new window...");
                SysUtils.sleep(1000);
            }
        }
    }

    public enum CONDITION_TO_CHECK {
        GREATER_THAN,
        LOWER_THAN,
        EQUAL,
        NOT_EQUAL
    }





    private static String getBranchFromSelfInfo(String selfInfoResponse) {
        String branch = "";

        Matcher matcherBranch = Pattern.compile("<span id=\"appBranch\">(.*?)</span>").matcher(selfInfoResponse);
        if (matcherBranch.find()) {
            branch = matcherBranch.group(1);
        }
        return branch;
    }

    private static String getBuildFromSelfInfo(String selfInfoResponse) {
        String build = "";
        Matcher matcherBuild = Pattern.compile("Build: <span id=\"appBuild\">\\d+</span>").matcher(selfInfoResponse);
        while (matcherBuild.find()) {
            build = matcherBuild.group(0).split("Build: <span id=\"appBuild\">")[1].split("</span>")[0];
        }
        return build;
    }

}
