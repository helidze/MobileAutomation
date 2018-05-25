package FabSiteAutomation;


import com.google.common.base.Verify;
import config.AppConfig;
import org.apache.log4j.Logger;
import org.junit.experimental.theories.Theories;
import org.openqa.selenium.*;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.Locatable;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pageObjPattern.basePage.MainPage;

import pageObjPattern.tests.AccountBasicTestsExecutor;

import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class SiteLoginPageTest extends AccountBasicTestsExecutor {

    private static Logger LOG = Logger.getLogger(SiteLoginPageTest.class);


    @BeforeClass
    public void setUp() throws Exception {
        mainPage = MainPage.chrome(null);
        webDriver = mainPage.getWebDriver();
    }

    @BeforeMethod
    public void startTest() throws Exception {
        if (checkWebDriver(mainPage) == null) {
            mainPage = MainPage.chrome(null);
        }
        webDriver = mainPage.getWebDriver();
        webDriver.manage().window().fullscreen();

    }


    @Test(priority = 1)
    public void positiveLoginTest() {
        loginIntoApp();
        LOG.info("Check that user logged in");
        Assert.assertEquals(webDriver.findElement(By.className("userblock__username")).getText(), "NameNewName");

    }

    @Test(priority = 2)
    public void emptyFieldsLoginTest() {
        loginIntoAppWithEmptyLogin();
        LOG.info("Check that exception displayed");
        Assert.assertEquals(webDriver.findElement(loginElements.getEmptyLoginError()).getText(), "Please, enter a valid data");

    }

    @Test(priority = 3)
    public void incorrectPasswordTest() {
        loginIntoAppWithIncorrectPass();
        if (!webDriver.findElement(loginElements.getIncorrectPassError()).isDisplayed()) {
            webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        } else {
            Assert.assertEquals(webDriver.findElement(loginElements.getIncorrectPassError()).getText(), "The password you’ve entered is incorrect.\n" +
                    "Please try again.");
        }
    }

    @Test(priority = 4)
    public void bannerPreviewTest() throws InterruptedException {
        playVideo();
        LOG.info("Check video");
        if (!webDriver.findElement(loginElements.getDisplayedVideo()).isDisplayed()) {
            webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        } else {
            Assert.assertTrue(webDriver.findElement(loginElements.getDisplayedVideo()).isDisplayed());
        }

    }

    @Test(priority = 5)
    public void listOfCategoriesTest() throws InterruptedException {
        loginIntoApp();
        Thread.sleep(2500);
        WebElement categories = webDriver.findElement(loginElements.getCategories());
        LOG.info("Open categories");
        categories.click();
        LOG.info("Choose Nails");
        findElement((loginElements.getNailsCategory())).click();
        LOG.info("Check that broadcast in Nails category doesn't exist");
        Assert.assertEquals(findElement(loginElements.getCategoriesAlert()).getText(), "There are no broadcasts in this category. Please, check Home page to explore another broadcasts");

    }

    @Test(priority = 6)
    public void checkVideoUploadAndDeleteFromVideoView() throws InterruptedException {
        loginIntoApp();
        Thread.sleep(2500);
        addVideoFile();
        Thread.sleep(2500);
        removeVideoFileFromVideoView();
        Thread.sleep(2500);
        LOG.info("Check that video file doesn't exist");
        Assert.assertTrue(!webDriver.getPageSource().contains("/html/body/div[1]/div[1]/main/div/div/div[2]/section[2]/ul/li/div/article/div[4]/footer"));

    }

    @Test(priority = 7)
    public void checkVideoUploadAndDeleteFromProfilePage() throws InterruptedException {
        loginIntoApp();
        Thread.sleep(2500);
        addVideoFile();
        Thread.sleep(2500);
        removeVideoFileFromProfilePage();
        Thread.sleep(2500);
        LOG.info("Check that video file doesn't exist");
        Assert.assertTrue(!webDriver.getPageSource().contains("/html/body/div[1]/div[1]/main/div/div/div[2]/section[2]/ul/li/div/article/div[4]/footer"));

    }

    @Test(priority = 10)
    public void resetPasswordTest(){
        goToResetPassPage();
        Assert.assertEquals(webDriver.findElement(loginElements.getInputLoginById()).getAttribute("value"),"1@1.com");
        Assert.assertTrue(webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/main/div/div/div/div[1]/form/button")).isEnabled());

    }

    @Test(priority = 8)
    public void facebookLoginTest(){
        loginIntoAppByFacebook();
        LOG.info("Check that user logged in");
        Assert.assertEquals(webDriver.findElement(loginElements.getUserBlockName()).getText(), "Ace TestBase");

    }

    @Test (priority = 9)
    public void createStreamAndCloseTest() throws InterruptedException{
        loginIntoApp();
        Thread.sleep(2500);
        createAndCloseStream();
        Thread.sleep(1000);
        LOG.info("Check that broadcast ended");
        Assert.assertEquals(webDriver.findElement(By.className("details__title")).getText(),"Broadcast ended");

    }

    @Test(priority = 12)
    public void searchTest() throws InterruptedException {
        loginIntoApp();
        Thread.sleep(1500);
        webDriver.findElement(loginElements.getSearchField()).sendKeys("a");
        ((ChromeDriver) webDriver).getKeyboard().pressKey(Keys.ENTER);
        Thread.sleep(500);
        Assert.assertEquals(getCurrentFollowers().get(0),"Admin");

    }

    @Test(priority = 13)
    public void newUserRegistrationTest() throws InterruptedException {
        createNewUser();
        Thread.sleep(1500);
        LOG.info("Check that user created");
        Assert.assertTrue(webDriver.findElement(By.className("avatar__inner")).isDisplayed());

    }

    @Test(priority = 18)
    public void viewStreamBySubscriberTest() throws InterruptedException {
        loginIntoApp();
        Thread.sleep(2500);
        LOG.info("Click Create Content");
        webDriver.findElement(By.id("create_menu")).click();
        LOG.info("Click Go Live");
        webDriver.findElement(loginElements.getGoLiveButton()).click();
        Thread.sleep(1000);
        LOG.info("Scroll to the bottom of the page");
        ((JavascriptExecutor) webDriver).executeScript(
                "arguments[0].scrollIntoView();", webDriver.findElement(loginElements.getStartLiveButton()));
        Thread.sleep(2000);
        LOG.info("Click Start Live");
        webDriver.findElement(loginElements.getStartLiveButton()).click();
        Thread.sleep(2500);
        String winHandleBefore = webDriver.getWindowHandle();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        for (String winHandle:driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }
        LOG.info("Open url");
        driver.get(AppConfig.getStartUrl());
        waitForPageLoaded1();
        LOG.info("Click SignIn button");
        driver.findElement(By.className("btn--pink")).click();
        Thread.sleep(1000);
        LOG.info("Enter login");
        driver.findElement(By.id("email-field")).sendKeys("2@2.com");
        LOG.info("Enter password");
        driver.findElement(By.id("pass-field")).sendKeys("222222");
        LOG.info("Click Login button");
        driver.findElement(By.id("sign_in")).click();
        Thread.sleep(1000);
        LOG.info("Scroll to live card position");
        ((JavascriptExecutor)driver).executeScript("window.scrollBy(" + 60 + ","
                + 631 + ");");
        Thread.sleep(500);
        LOG.info("Click on Live video created before");
        driver.findElement(By.partialLinkText("TestStream")).click();
        Thread.sleep(20000);
        LOG.info("Check that video displayed for subscriber");
        Assert.assertEquals(driver.findElement(By.className("video-stat__item")).getText(),"1");
        driver.close();
        webDriver.switchTo().window(winHandleBefore);
        LOG.info("Click Stop Live Button");
        webDriver.findElement(loginElements.getPublisherStopButton()).click();
        LOG.info("Accept Broadcast Stop");
        webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div/div/div/button[2]")).click();
        Thread.sleep(1000);
        LOG.info("Check that broadcast ended");
        Assert.assertEquals(webDriver.findElement(By.className("stats__value")).getText(),"1");

    }

    @Test(priority = 19)
    public void viewStreamBy5Subscribers() throws InterruptedException {
        loginIntoApp();
        Thread.sleep(2500);
        LOG.info("Click Create Content");
        webDriver.findElement(By.id("create_menu")).click();
        LOG.info("Click Go Live");
        webDriver.findElement(loginElements.getGoLiveButton()).click();
        Thread.sleep(1000);
        LOG.info("Scroll to the bottom of the page");
        ((JavascriptExecutor) webDriver).executeScript(
                "arguments[0].scrollIntoView();", webDriver.findElement(loginElements.getStartLiveButton()));
        Thread.sleep(2000);
        LOG.info("Click Start Live");
        webDriver.findElement(loginElements.getStartLiveButton()).click();
        Thread.sleep(2500);
        String winHandleBefore = webDriver.getWindowHandle();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        for (String winHandle:driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }
        LOG.info("Open url");
        driver.get(AppConfig.getStartUrl());
        waitForPageLoaded1();
        LOG.info("Click SignIn button");
        driver.findElement(By.className("btn--pink")).click();
        Thread.sleep(1000);
        LOG.info("Enter login");
        driver.findElement(By.id("email-field")).sendKeys("2@2.com");
        LOG.info("Enter password");
        driver.findElement(By.id("pass-field")).sendKeys("222222");
        LOG.info("Click Login button");
        driver.findElement(By.id("sign_in")).click();
        Thread.sleep(1000);
        LOG.info("Scroll to live card position");
        ((JavascriptExecutor)driver).executeScript("window.scrollBy(" + 60 + ","
                + 631 + ");");
        Thread.sleep(500);
        driver.findElement(By.partialLinkText("TestStream")).click();
        Thread.sleep(20000);
        LOG.info("Check that video displayed for subscriber");
        Assert.assertEquals(driver.findElement(By.className("video-stat__item")).getText(),"1");
        WebDriver driver1 = new ChromeDriver();
        driver1.manage().window().maximize();
        for (String winHandle:driver1.getWindowHandles()) {
            driver1.switchTo().window(winHandle);
        }
        LOG.info("Open url");
        driver1.get(AppConfig.getStartUrl());
        waitForPageLoaded1();
        LOG.info("Click SignIn button");
        driver1.findElement(By.className("btn--pink")).click();
        Thread.sleep(1000);
        LOG.info("Enter login");
        driver1.findElement(By.id("email-field")).sendKeys("3@3.com");
        LOG.info("Enter password");
        driver1.findElement(By.id("pass-field")).sendKeys("333333");
        LOG.info("Click Login button");
        driver1.findElement(By.id("sign_in")).click();
        Thread.sleep(1000);
        LOG.info("Scroll to live card position");
        ((JavascriptExecutor)driver1).executeScript("window.scrollBy(" + 60 + ","
                + 631 + ");");
        Thread.sleep(500);

        LOG.info("Click on Live video created before");
        driver1.findElement(By.partialLinkText("TestStream")).click();
        Thread.sleep(20000);
        LOG.info("Check that video displayed for subscriber");
        Assert.assertEquals(driver1.findElement(By.className("video-stat__item")).getText(),"2");
        WebDriver driver2 = new ChromeDriver();
        driver2.manage().window().maximize();
        for (String winHandle:driver2.getWindowHandles()) {
            driver2.switchTo().window(winHandle);
        }
        LOG.info("Open url");
        driver2.get(AppConfig.getStartUrl());
        waitForPageLoaded1();
        LOG.info("Click SignIn button");
        driver2.findElement(By.className("btn--pink")).click();
        Thread.sleep(1000);
        LOG.info("Enter login");
        driver2.findElement(By.id("email-field")).sendKeys("4@4.com");
        LOG.info("Enter password");
        driver2.findElement(By.id("pass-field")).sendKeys("444444");
        LOG.info("Click Login button");
        driver2.findElement(By.id("sign_in")).click();
        Thread.sleep(1000);
        LOG.info("Scroll to live card position");
        ((JavascriptExecutor)driver2).executeScript("window.scrollBy(" + 60 + ","
                + 631 + ");");
        Thread.sleep(500);

        LOG.info("Click on Live video created before");
        driver2.findElement(By.partialLinkText("TestStream")).click();
        Thread.sleep(800000);
        LOG.info("Check that video displayed for subscriber");
        Assert.assertEquals(driver2.findElement(By.className("video-stat__item")).getText(),"3");
        driver.close();
        driver1.close();
        driver2.close();
        webDriver.switchTo().window(winHandleBefore);
        LOG.info("Click Stop Live Button");
        webDriver.findElement(loginElements.getPublisherStopButton()).click();
        LOG.info("Accept Broadcast Stop");
        webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div/div/div/button[2]")).click();
        Thread.sleep(1000);
        LOG.info("Check that broadcast ended");
        Assert.assertEquals(webDriver.findElement(By.className("stats__value")).getText(),"3");

    }

    @Test(priority = 14)
    public void mandatoryGiftsTest() throws InterruptedException {
        loginIntoApp();
        Thread.sleep(1500);
        webDriver.findElement(By.id("gifts")).click();
        Thread.sleep(4000);
        LOG.info("Add main gifts to list");
        ArrayList<String> expectedGifts = new ArrayList<>();
        expectedGifts.add("Extra Small Package (2 Gifts)");
        expectedGifts.add("Small Package (6 Gifts)");
        expectedGifts.add("Medium Package (12 Gifts)");
        expectedGifts.add("Big Package (60 Gifts)");
        expectedGifts.add("Huge Package (99 Gifts)");
        LOG.info("Check that all gifts is exist on site");
        Assert.assertTrue(getCurrentGifts().containsAll(expectedGifts));

    }

    @Test(priority = 15)
    public void popularVideos()throws InterruptedException{
        loginIntoApp();
        Thread.sleep(1500);
        LOG.info("Check that popular video isn't empty");
        Assert.assertTrue(!getCurrentPopularVideos().isEmpty());

    }

    @Test(priority = 16)
    public void checkThatAvatarAddedToNewUser() throws InterruptedException {
        createNewUser();
        Thread.sleep(1500);
        LOG.info("Click on avatar button");
        webDriver.findElement(By.xpath("//*[@id=\"account_menu\"]")).click();
        LOG.info("Click edit profile");
        webDriver.findElement(By.id("edit_profile")).click();
        WebElement fileUpload = webDriver.findElement(By.xpath("//*[@id=\"avatar-field\"]"));
        String filePath = AccountBasicTestsExecutor.class.getClassLoader().getResource("avatarTest.jpg").getPath();
        LOG.info("Add Photo file");
        fileUpload.sendKeys(filePath);
        Thread.sleep(2500);
        LOG.info("Save Avatar");
        webDriver.findElement(By.className("btn--pink")).click();
        Thread.sleep(1500);
        LOG.info("Check that Avatar added");
        Assert.assertEquals(webDriver.findElement(By.className("alert__message")).getText(),"Avatar successfully updated");


    }

    @Test(priority = 17)
    public void editProfileGeneralInfoTest() throws InterruptedException {
        createNewUser();
        Date d = new Date(System.currentTimeMillis());
        Thread.sleep(1500);
        LOG.info("Click on avatar button");
        webDriver.findElement(By.xpath("//*[@id=\"account_menu\"]")).click();
        LOG.info("Click edit profile");
        webDriver.findElement(By.id("edit_profile")).click();
        LOG.info("Click on general info window");
        webDriver.findElement(By.xpath("//*[@id=\"general-info\"]/div/form")).click();
        LOG.info("Change fullName");
        webDriver.findElement(By.id("name-field")).sendKeys("testFullName");
        webDriver.findElement(By.id("nickname-field")).click();
        webDriver.findElement(By.id("nickname-field")).clear();
        LOG.info("Change nickname");
        webDriver.findElement(By.id("nickname-field")).sendKeys("testNickName" + d.getMinutes() + d.getSeconds() + d.getHours());
        LOG.info("Change UserInfo");
        webDriver.findElement(By.id("info-field")).sendKeys("testUserInfo");
        webDriver.findElement(By.className("btn--pink")).click();
        LOG.info("Check that UserInfoCorrect");
        Assert.assertEquals(webDriver.findElement(By.id("info-field")).getAttribute("value"),"testUserInfo");
    }

    @Test(priority = 20)
    public void changeProfilePasswordTest() throws InterruptedException {
        loginIntoApp();
        Thread.sleep(2500);
        LOG.info("Click on avatar button");
        webDriver.findElement(By.xpath("//*[@id=\"account_menu\"]")).click();
        Thread.sleep(500);
        LOG.info("Click edit profile");
        webDriver.findElement(By.id("edit_profile")).click();
        Thread.sleep(1500);
        LOG.info("Scroll to the bottom of the page");
        ((JavascriptExecutor) webDriver).executeScript(
                "arguments[0].scrollIntoView();", webDriver.findElement(By.id("old-pass-field")));
        webDriver.findElement(By.id("old-pass-field")).click();
        LOG.info("Enter old password");
        webDriver.findElement(By.id("old-pass-field")).sendKeys("111111");
        LOG.info("Enter new password");
        webDriver.findElement(By.id("new-pass-field")).sendKeys("222222");
        LOG.info("Confirm new password");
        webDriver.findElement(By.id("confirm-pass-field")).sendKeys("222222");
        LOG.info("Click save");
        webDriver.findElement(By.className("btn--pink")).click();
        Thread.sleep(1500);
        LOG.info("Click on avatar");
        webDriver.findElement(By.xpath("//*[@id=\"account_menu\"]")).click();
        LOG.info("Click Log Out");
        webDriver.findElement(By.id("log_out")).click();
        Thread.sleep(500);
        loginIntoAppWithChangedPassword();
        Thread.sleep(2500);
        LOG.info("Click on avatar button");
        webDriver.findElement(By.xpath("//*[@id=\"account_menu\"]")).click();
        Thread.sleep(500);
        LOG.info("Click edit profile");
        webDriver.findElement(By.id("edit_profile")).click();
        Thread.sleep(1500);
        LOG.info("Scroll to the bottom of the page");
        ((JavascriptExecutor) webDriver).executeScript(
                "arguments[0].scrollIntoView();", webDriver.findElement(By.id("old-pass-field")));
        webDriver.findElement(By.id("old-pass-field")).click();
        LOG.info("Enter old password");
        webDriver.findElement(By.id("old-pass-field")).sendKeys("222222");
        LOG.info("Enter new password");
        webDriver.findElement(By.id("new-pass-field")).sendKeys("111111");
        LOG.info("Confirm new password");
        webDriver.findElement(By.id("confirm-pass-field")).sendKeys("111111");
        LOG.info("Click save");
        webDriver.findElement(By.className("btn--pink")).click();
        LOG.info("Check that password changed correct");
        Assert.assertEquals(webDriver.findElement(By.className("alert--success")).getText(),"Password successfully updated");

    }



    @AfterMethod
    public void closeSession(){
        haltSessions();
    }

    }

