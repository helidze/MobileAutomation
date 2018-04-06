package pageObjPattern.basePage;

import config.AppConfig;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import pageObjPattern.tests.ApplicationPages;
import utils.SysUtils;
import utils.color.ColorUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BasePage extends ApplicationPages {

    private static Logger LOG = Logger.getLogger(BasePage.class);

    public BasePage(WebDriver webDriver) {
        super(webDriver);
    }

    /**
     * Operation with WebElements.
     */

    protected WebElement getWebElement(By by) {
        return webDriver.findElement(by);
    }

    public void click(By by) {
        WebElement element = getWebElement(by);
        element.click();
    }

    protected void clickOnVisibleElement(By by) {
        List<WebElement> webElements = getWebElements(by);
        for (WebElement webElement : webElements) {
            if (webElement.isDisplayed()) {
                webElement.click();
                break;
            }
        }
    }

    public void submit(By by) {
        WebElement element = getWebElement(by);
        element.submit();
    }

    protected void moveToAndClick(By by) {
        moveTo(by);
        SysUtils.sleep(500);
        click(by);
    }

    public void clear(By by) {
        WebElement element = getWebElement(by);
        element.clear();
    }

    protected void pressEnterOnElement(By by) {
        WebElement element = getWebElement(by);
        element.sendKeys(Keys.ENTER);
    }

    public void type(By by, CharSequence value) {
        WebElement element = getWebElement(by);
        element.sendKeys(value);
    }

    public void clearAndType(By by, CharSequence value) {
        WebElement element = getWebElement(by);
        element.clear();
        element.sendKeys(value);
    }

    public void setSelectStatusForElement(boolean selectStatus, By by) {
        if (selectStatus) {
            if (!isSelected(by)) {
                click(by);
            }
        } else {
            if (isSelected(by)) {
                click(by);
            }
        }
    }

    public Point getLocation(By by) {
        return getWebElement(by).getLocation();
    }

    public String getAttribute(By by, String attribute) {
        WebElement element = getWebElement(by);
        return element.getAttribute(attribute);
    }

    protected String getCssValue(By by, String attribute) {
        WebElement element = getWebElement(by);
        return element.getCssValue(attribute);
    }

    protected Color getColor(By by) {
        String backgroundColor = getCssValue(by, "background-color");
        return ColorUtils.getColorFromRGBAColorCode(backgroundColor);
    }

    protected Color getColor(By by, int num) {
        String backgroundColor = getCssValue(by, num, "background-color");
        return ColorUtils.getColorFromRGBAColorCode(backgroundColor);
    }

    protected Color getFontColor(By by) {
        String fontColor = getCssValue(by, "color");
        return ColorUtils.getColorFromRGBAColorCode(fontColor);
    }

    public String getText(By by) {
        WebElement element = getWebElement(by);
        return element.getText();
    }

    public String getCurrentUrl() {
        return webDriver.getCurrentUrl();
    }

    public String getTitle() {
        return webDriver.getTitle();
    }

    public String getValue(By by) {
        return getAttribute(by, "value");
    }

    public boolean isSelected(By by) {
        WebElement element = getWebElement(by);
        return element.isSelected();
    }

    protected boolean isAttributeChecked(By by) {
        String attributeValue = getAttribute(by, "checked");
        return attributeValue != null && attributeValue.equals("true");
    }

    protected boolean isAttributeReadOnly(By by) {
        String attributeValue = getAttribute(by, "readonly");
        return attributeValue != null && attributeValue.equals("true");
    }

    protected boolean isAttributeDisabled(By by) {
        String attributeValue = getAttribute(by, "disabled");
        return attributeValue != null && attributeValue.equals("true");
    }

    public boolean isTextPresent(String text) {
        setImplicitlyWait(3, TimeUnit.SECONDS);
        boolean isTextPresent = false;
        try {
            isTextPresent = isElementDisplayed(By.xpath("//*[contains(text(),'" + text + "')]"));
            setImplicitlyWait(20, TimeUnit.SECONDS);
            return isTextPresent;
        } catch (Exception e) {
            setImplicitlyWait(20, TimeUnit.SECONDS);
            return isTextPresent;
        }
    }

    protected int getHeight(By by) {
        WebElement element = getWebElement(by);
        return element.getSize().getHeight();
    }

    protected int getWidth(By by) {
        WebElement element = getWebElement(by);
        return element.getSize().getWidth();
    }

    /**
     * Operation with List of WebElements.
     */

    protected List<WebElement> getWebElements(By by) {
        return webDriver.findElements(by);
    }

    public void click(By by, int item) {
        WebElement element = getWebElements(by).get(item);
        element.click();
    }

    public void type(By by, int index, CharSequence value) {
        WebElement element = getWebElements(by).get(index);
        element.sendKeys(value);
    }

    public void clearAndType(By by, int index, String value) {
        WebElement element = getWebElements(by).get(index);
        element.clear();
        element.sendKeys(value);
    }

    public int getSize(By by) {
        return getWebElements(by).size();
    }

    public String getText(By by, int item) {
        WebElement element = getWebElements(by).get(item);
        return element.getText();
    }

    public String getAttribute(By by, int item, String attribute) {
        WebElement element = getWebElements(by).get(item);
        return element.getAttribute(attribute);
    }

    protected String getCssValue(By by, int item, String attribute) {
        WebElement element = getWebElements(by).get(item);
        return element.getCssValue(attribute);
    }

    protected boolean isAttributeDisabled(By by, int num) {
        String attributeValue = getAttribute(by, num, "disabled");
        return attributeValue != null && attributeValue.equals("true");
    }

    public List<String> getElementsCssValue(By by, String attribute) {
        List<String> optionAttributes = new ArrayList<>();
        List<WebElement> options = getWebElements(by);
        optionAttributes.addAll(options.stream().map(webElement -> webElement.getCssValue(attribute)).collect(Collectors.toList()));
        return optionAttributes;
    }

    public List<String> getElementsText(By by) {
        List<String> optionText = new ArrayList<>();
        List<WebElement> options = getWebElements(by);
        optionText.addAll(options.stream().map(WebElement::getText).collect(Collectors.toList()));
        return optionText;
    }

    public List<String> getElementsAttribute(By by, String attribute) {
        List<String> optionsAttribute = new ArrayList<>();
        List<WebElement> options = getWebElements(by);
        optionsAttribute.addAll(options.stream().map(webElement -> webElement.getAttribute(attribute)).collect(Collectors.toList()));
        return optionsAttribute;
    }

    public boolean isSelected(By by, int num) {
        WebElement webElement = getWebElements(by).get(num);
        return webElement.isSelected();
    }

    /**
     * Operation with Select.
     */

    protected Select getSelect(By by) {
        return new Select(getWebElement(by));
    }

    protected Select getSelect(By by, int num) {
        return new Select(getWebElements(by).get(num));
    }

    public void selectByIndex(By by, int index) {
        Select select = getSelect(by);
        select.selectByIndex(index);
    }

    public void selectByVisibleText(By by, String text) {
        Select select = getSelect(by);
        select.selectByVisibleText(text);
    }

    public void selectByVisibleText(By by, int num, String text) {
        Select select = getSelect(by, num);
        select.selectByVisibleText(text);
    }

    protected void selectVisibleElementByVisibleText(By locator, String visibleValue) {
        List<WebElement> webElements = getWebElements(locator);
        for (WebElement webElement : webElements) {
            if (webElement.isDisplayed()) {
                new Select(webElement).selectByVisibleText(visibleValue);
                break;
            }
        }
    }

    public void selectByValue(By by, String value) {
        Select select = getSelect(by);
        select.selectByValue(value);
    }

    public void selectByValue(By by, int num, String value) {
        Select select = getSelect(by, num);
        select.selectByValue(value);
    }

    public void selectElementByJS(By by, int index) {
        LOG.info("SELECT Element #" + index + ".");
        scrollTo(by);
        executeJavaScript("var elem=arguments[0]; setTimeout(function() {elem.selectedIndex = \"" + index + "\";}, 100)", getWebElement(by));
    }

    protected void deselectByIndex(By by, int index) {
        Select select = getSelect(by);
        select.deselectByIndex(index);
    }

    protected void deselectAll(By by) {
        Select select = getSelect(by);
        select.deselectAll();
    }

    protected int getOptionSize(By by) {
        return getSelect(by).getOptions().size();
    }

    protected int getAllSelectedOptionSize(By by) {
        return getSelect(by).getAllSelectedOptions().size();
    }

    protected String getSelectedOptionText(By by, int index) {
        return getSelect(by).getAllSelectedOptions().get(index).getText();
    }

    protected String getOptionText(By by, int index) {
        return getSelect(by).getOptions().get(index).getText();
    }

    protected List<String> getOptionsText(By by) {
        List<String> optionsText = new ArrayList<>();
        int size = getOptionSize(by);
        for (int i = 0; i < size; i++) {
            optionsText.add(getOptionText(by, i));
        }
        return optionsText;
    }

    /**
     * Operation with Actions.
     */

    protected void clickByAction(By by) {
        new Actions(webDriver).keyDown(Keys.CONTROL).
                click(getWebElement(by)).
                keyUp(Keys.CONTROL).
                perform();
    }

    protected void clickAndHold(By by) {
        Actions action = new Actions(webDriver);
        WebElement element = getWebElement(by);
        action.clickAndHold(element).perform();
    }

    protected void clickAndHold(By by, int element) {
        Actions action = new Actions(webDriver);
        WebElement targetElement = getWebElements(by).get(element);
        action.clickAndHold(targetElement).perform();
    }

    protected void doubleClick(By by) {
        Actions action = new Actions(webDriver);
        WebElement element = getWebElement(by);
        action.doubleClick(element).perform();
    }

    protected void moveToElementWithRelease(By by, int element) {
        Actions action = new Actions(webDriver);
        WebElement targetElement = getWebElements(by).get(element);
        action.moveToElement(targetElement).release(targetElement).build().perform();
    }

    protected void moveToElementWithRelease(By by) {
        Actions action = new Actions(webDriver);
        WebElement element = getWebElement(by);
        action.moveToElement(element).release(element).build().perform();
    }

    public void moveTo(By by) {
        Actions actions = new Actions(webDriver);
        WebElement element = getWebElement(by);
        actions.moveToElement(element).build().perform();
    }

    protected void moveToTheOneOfTheElements(By by, int item) {
        Actions actions = new Actions(webDriver);
        WebElement element = getWebElements(by).get(item);
        actions.moveToElement(element).build().perform();
    }

    protected void moveByOffset(By by) {
        Actions action = new Actions(webDriver);
        WebElement element = getWebElement(by);
        action.moveByOffset(0, element.getLocation().getY()).perform();
    }

    protected void contextClick(By by) {
        LOG.info("CLICK Menu on question.");
        Actions actions = new Actions(webDriver);
        WebElement element = getWebElement(by);
        actions.contextClick(element).perform();
    }

    protected void dragAndDrop(By source, By target) {
        WebElement src = getWebElement(source);
        WebElement trgt = getWebElement(target);
        Actions actions = new Actions(webDriver);
        actions.dragAndDrop(src, trgt).perform();
    }

    public void resizeWebElement(By by, int xOffset, int yOffset) {
        WebElement elementToResize = getWebElement(by);
        resize(elementToResize, xOffset, yOffset);
    }

    public void resizeWebElement(By by, int item, int xOffset, int yOffset) {
        WebElement elementToResize = getWebElements(by).get(item);
        resize(elementToResize, xOffset, yOffset);
    }

    private void resize(WebElement elementToResize, int xOffset, int yOffset) {
        try {
            Actions action = new Actions(webDriver);
            action.clickAndHold(elementToResize).moveByOffset(xOffset, yOffset).release().build().perform();
        } catch (Exception e) {
            System.out.println("Unable to resize" + elementToResize + " - " + e.getMessage());
        }
    }

    /**
     * WebElements availability.
     */

    public boolean isElementDisplayed(By by, int num) {
        try {
            return getWebElements(by).get(num).isDisplayed();
        } catch (NoSuchElementException e) {
            LOG.debug("Unable to locate element: " + by.toString());
            return false;
        }
    }

    public void waitForElementNotVisible(final By locator, int index) {
        final Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withMessage("Element " + locator + " is still visible")
                .withTimeout(30, TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS);
        wait.until(result -> {
            try {
                return !getWebElements(locator).get(index).isDisplayed();
            } catch (NoSuchElementException | StaleElementReferenceException var3) {
                return Boolean.TRUE;
            }
        });
    }

    public void waitForAttributeNotEmpty(final By locator, String attribute) {
        final Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withMessage("Attribute " + locator + " is still not present")
                .withTimeout(30, TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS);
        //wait.until(presenceOfNotEmptyElementAttribute(locator, attribute));
    }

    public void waitForAttribute(final By locator, String attribute, String value) {
        final Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withMessage("Attribute " + locator + " is still not present")
                .withTimeout(30, TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS);
        /*wait.until(presenceOfCertainAttribute(locator, attribute, value));*/
    }

    private ExpectedCondition <Boolean> presenceOfNotEmptyElementAttribute(final By locator, String attribute) {
        return driver -> !getAttribute(locator, attribute).isEmpty();
    }

    private ExpectedCondition<Boolean> presenceOfCertainAttribute(final By locator, String attribute, String value) {
        return driver -> getAttribute(locator, attribute).equals(value);
    }

    private boolean isDisplayed(By by) throws NoSuchElementException, NullPointerException, StaleElementReferenceException {
        return getWebElement(by).isDisplayed();
    }

    public boolean isElementDisplayed(By by) {
        try {
            return isDisplayed(by);
        } catch (NoSuchElementException e) {
            LOG.debug("Unable to locate element: " + by.toString());
            return false;
        }
    }

    protected boolean isElementDisplayedMobile(By by) {
        LOG.debug("Temporary circumvention for Appium bug #6476, #9324.");
        try {
            return isDisplayed(by);
        } catch (WebDriverException | NullPointerException e) {
            LOG.debug("Unable to locate element: " + by.toString());
            return false;
        }
    }

    /**
     * Operation with alerts.
     */


    public Alert waitForAlert(int seconds) {
        try {
            Wait<WebDriver> wait = new WebDriverWait(webDriver, seconds);
            return wait.until(alert -> webDriver.switchTo().alert());
        } catch (TimeoutException ex) {
            LOG.info("Timeout expired");
            return null;
        } catch (NoAlertPresentException ex) {
            LOG.info("Alert is not present");
            return null;
        }
    }

    public Alert returnAlertIfPresent() {
        try {
            return waitForAlert(5);
        } catch (NoAlertPresentException e) {
            return null;
        }
    }

    protected void acceptAlertIfPresent() {
        try {
            Alert alert = webDriver.switchTo().alert();
            LOG.info("ALERT :\n");
            alert.accept();
        } catch (NoAlertPresentException e) {
            LOG.info("ALERT is not present.");
        }
    }

    public void takeConfirm() {
        Alert alert = waitForAlert(15);
        LOG.info("ALERT : " + alert.getText());
        alert.accept();
    }

    public void takeConfirm(String alertText) {
        Alert alert = waitForAlert(15);
        assert alert.getText().equals(alertText) : "Incorrect alert message present ! : " + alert.getText();
        LOG.info("ALERT : " + alert.getText());
        alert.accept();
    }


    /**
     * Explicitly/Implicitly waits.
     */

    public void waitForPageLoaded() {
        Wait<WebDriver> wait = new WebDriverWait(webDriver, 1000);
        try {
            wait.until(result -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        } catch (Throwable error) {
            fail("Timeout waiting for Page Load Request to complete.", error);
        }
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

    public void waitForAjaxOnCMPage(int maxCounter) {
        SysUtils.sleep(1500);
        setImplicitlyWait(2, TimeUnit.SECONDS);
        if (isElementDisplayed(By.id("wait_div_load"))) {
            String[] str = getWebElement(By.id("wait_div_load")).getAttribute("style").split("; ");
            String rightStatus = "visibility: hidden";
            int counter = 0;

            if (!str[7].equals(rightStatus)) {
                while (str[7].equals("visibility: visible")) {
                    counter++;
                    str = getWebElement(By.id("wait_div_load")).getAttribute("style").split("; ");
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

    public void waitForFinishResizePopup() {
        int counter = 0;
        while (counter < 3) {
            waitForResizeFinishing();
            SysUtils.sleep(1000);
            counter++;
        }
    }

    private void waitForResizeFinishing() {
        Wait<WebDriver> wait = new WebDriverWait(webDriver, 15);
        try {
            wait.until(result -> {
                JavascriptExecutor js = (JavascriptExecutor) webDriver;
                return (Long) js.executeScript("return document.getElementsByClassName('ui_FinishResizePopup').length") == 1;
            });
        } catch (TimeoutException exception) {
            LOG.info("Resizing not finished");
        }
    }

    public void waitForElementDisplayed(final By locator) {
        WebDriverWait wait = new WebDriverWait(webDriver, 60);
        wait.until(result -> getWebElement(locator).isDisplayed());
    }

    public void waitForElementDisplayedByTime(final By locator, final long time) {
        WebDriverWait wait = new WebDriverWait(webDriver, time);
        wait.until(result -> getWebElement(locator).isDisplayed());
    }

    public void waitForElementDisplayed(final By locator, final int item) {
        WebDriverWait wait = new WebDriverWait(webDriver, 60);
        wait.until(result -> getWebElements(locator).get(item).isDisplayed());
    }

    public void waitForDisplayedAtLeastOneOfTheElements(final By locator, final long timeInSeconds) {
        final Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withMessage("Any element of elements '" + locator.toString() + "' is not displayed.")
                .withTimeout(timeInSeconds, TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS);
        wait.until(result -> {
            for (WebElement webelement : getWebElements(locator)) {
                if (webelement.isDisplayed())
                    return true;
            }
            return false;
        });
    }

    protected void waitForDisplaySpecificNumberOfElements(final By locator, final int expectedCount) {
        final Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withMessage("Number of displayed elements " + locator.toString() + " still different from " + expectedCount)
                .withTimeout(30, TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS);
        wait.until(result -> {
            List<WebElement> elements = getWebElements(locator);

            if (elements.size() != expectedCount) {
                LOG.info("Current elements size is: " + elements.size());
                return Boolean.FALSE;
            }

            for (WebElement element : elements) {
                if (!element.isDisplayed()) {
                    LOG.info("Element " + element + "is not displayed");
                    return Boolean.FALSE;
                }
            }
            return Boolean.TRUE;
        });
    }

    protected void waitForExistSpecificNumberOfElements(final By locator, final int expectedCount) {
        final Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withMessage("Number of elements " + locator.toString() + " still different from " + expectedCount)
                .withTimeout(60, TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS);
        wait.until(result -> {
            List<WebElement> elements = getWebElements(locator);
            if (elements.size() != expectedCount) {
                return null;
            }
            return elements;
        });
    }

    public void waitForElementNotVisible(final By element) {
        final Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withMessage("Element " + element + " is still visible")
                .withTimeout(30, TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS);
        wait.until(result -> {
            try {
                return !getWebElement(element).isDisplayed();
            } catch (NoSuchElementException | StaleElementReferenceException var3) {
                return Boolean.TRUE;
            }
        });
    }

    protected void waitForElementNotVisibleByTime(final By element, long seconds) {
        setImplicitlyWait(1, TimeUnit.SECONDS);
        final Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withMessage("Element " + element.toString() + " is still visible")
                .withTimeout(seconds, TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS);
        wait.until(result -> {
            try {
                return !isDisplayed(element);
            } catch (NoSuchElementException | StaleElementReferenceException var3) {
                LOG.debug("Element is not found");
                return Boolean.TRUE;
            }
        });
        setImplicitlyWait(60, TimeUnit.SECONDS);
    }

    protected void waitForElementWithRefresh(By by) {
        for (int i = 0; i < 10; i++) {
            if (isElementDisplayed(by)) {
                break;
            } else {
                SysUtils.sleep(2000);
                refreshPage();
            }
        }
    }

    protected void waitForElementEnabled(By by) {
        waitForElementDisplayed(by);
        WebDriverWait wait = new WebDriverWait(webDriver, 60);
        wait.until(result -> getWebElement(by).isEnabled());
    }

    protected void waitForElementDisplayedMobile(final By locator) {
        LOG.debug("Temporary circumvention for Appium bug #6476, #9324.");
        Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withMessage("Element " + locator.toString() + " is still not displayed.")
                .withTimeout(60, TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS)
                .ignoring(WebDriverException.class, NullPointerException.class);
        wait.until(result -> getWebElement(locator).isDisplayed());
    }

    /**
     * Frame methods.
     */

    public void switchToDefaultContent() {
        webDriver.switchTo().defaultContent();
    }

    public void switchToFrame(int frameNumber) {
        webDriver.switchTo().frame(frameNumber);
    }

    public void switchToFrame(String frameName) {
        WebElement element = getWebElement(By.id(frameName));
        webDriver.switchTo().frame(element);
    }

    protected void switchToFrameByLocator(By by) {
        WebElement webElement = getWebElement(by);
        webDriver.switchTo().frame(webElement);
    }

    protected void switchToActiveElement() {
        webDriver.switchTo().activeElement();
    }

    public void switchToNewWindow(String currentWindow) {
        webDriver.getWindowHandles().stream().filter(winHandle -> !winHandle.equals(currentWindow))
                .forEach(winHandle -> webDriver.switchTo().window(winHandle).manage().window().maximize());
    }

    public void waitForNewWindow(int previousNumOfWindows, int timeOutInSeconds) {
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

    public void closeWindowAndSwitchToOther() {
        String currentWindow = webDriver.getWindowHandle();
        webDriver.close();
        switchToNewWindow(currentWindow);
    }

    /**
     * Other methods.
     */

    public void refreshPage() {
        LOG.info("Refreshing the page");
        webDriver.navigate().refresh();
        waitForPageLoaded();
    }

    public void setImplicitlyWait(long time, TimeUnit unit) {
        webDriver.manage().timeouts().implicitlyWait(time, unit);
    }

    public Object executeJavaScript(String script, Object... objects) {
        if (objects == null) {
            return ((JavascriptExecutor) webDriver).executeScript(script);
        } else {
            return ((JavascriptExecutor) webDriver).executeScript(script, objects);
        }
    }

    protected void typeValueWithJSToFieldWithId(String id, String value) {
        executeJavaScript("document.getElementById('" + id + "').value ='" + value + "'");
    }

    protected void typeValueWithJSToFieldByXpathAndNum(By by, int num, String value) {
        WebElement element = getWebElements(by).get(num);
        executeJavaScript("var element=arguments[0]; element.value ='" + value + "'", element);
    }

    public void scrollTo(By by) {
        WebElement element = getWebElement(by);
        Point point = element.getLocation();
        executeJavaScript("window.scrollTo(" + point.getX() + "," + (point.getY() - 200) + ");");
    }

    public void scrollTo(By by, int num) {
        WebElement element = getWebElements(by).get(num);
        Point point = element.getLocation();
        executeJavaScript("window.scrollTo(" + point.getX() + "," + (point.getY() - 200) + ");");
    }

    public void scrollToElementByCoordinates(int x, int y) {
        executeJavaScript("window.scroll(" + x + "," + y + ")");
    }

    protected void scrollIntoViewById(String elementId) {
        executeJavaScript("return document.getElementById('" + elementId + "').scrollIntoView()");
    }

    protected void scrollIntoView(By by) {
        executeJavaScript("return arguments[0].scrollIntoView(false);", getWebElement(by));
    }

    protected boolean isElementContainVerticalScrollBar(By by) {
        return (boolean) executeJavaScript("return arguments[0].scrollHeight > arguments[0].clientHeight;", getWebElement(by));
    }

    protected Object focusOnElementById(String element) {
        return ((JavascriptExecutor) webDriver).executeScript("document.getElementById('" + element + "').focus()");
    }

    protected void focusOnElementWithJS(By by) {
        executeJavaScript("var elem=arguments[0]; setTimeout(function() {elem.focus();}, 100)", getWebElement(by));
    }

    protected void clickOnElementWithJS(By by) {
        executeJavaScript("var elem=arguments[0]; setTimeout(function() {elem.click();}, 100)", getWebElement(by));
    }

    protected List<String> getAttributeValueForVisibleElements(By by, String attribute) {
        List<String> attributes = new ArrayList<>();
        List<WebElement> elements = getWebElements(by);
        attributes.addAll(elements.stream().map(element -> element.getAttribute(attribute))
                .collect(Collectors.toList()));
        return attributes;
    }

    protected void waitForCondition(final By by, final String condition, final int value) {
        WebDriverWait myWait = new WebDriverWait(webDriver, 45);
        myWait.until(result -> {
            switch (condition) {
                case "GREATER_THAN":
                    return getWebElements(by).size() > value;
                case "LOWER_THAN":
                    return (getWebElements(by).size() < value);
                case "EQUAL":
                    return (getWebElements(by).size() == value);
                case "NOT_EQUAL":
                    return (getWebElements(by).size() != value);
                default:
                    return false;
            }
        });
    }

    protected boolean isElementsEmpty(By by) {
        return getWebElements(by).isEmpty();
    }

    public int getWindowHandlesSize() {
        return webDriver.getWindowHandles().size();
    }

    public String getWindowHandle() {
        return webDriver.getWindowHandle();
    }

    public void openPage(String url) {
        webDriver.get(url);
        waitForPageLoaded();
    }
}
