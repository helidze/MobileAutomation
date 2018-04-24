package FabSiteAutomation;

import config.AppConfig;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pageObjPattern.basePage.MainPage;
import pageObjPattern.pages.login.LoginPage;
import pageObjPattern.pages.login.elements.LoginElements;
import pageObjPattern.tests.AccountBasicTestsExecutor;

import java.io.File;
import java.lang.reflect.Method;
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
    public void positiveLogin() {
        loginIntoApp();
        LOG.info("Check that user logged in");
        Assert.assertEquals(webDriver.findElement(By.className("userblock__username")).getText(), "NameNewName");

    }

    @Test(priority = 2)
    public void emptyFieldsLogin() {
        loginIntoAppWithEmptyLogin();
        LOG.info("Check that exception displayed");
        Assert.assertEquals(webDriver.findElement(loginElements.getEmptyLoginError()).getText(), "Please, enter a valid data");
    }

    @Test(priority = 3)
    public void incorrectPassword() {
        loginIntoAppWithIncorrectPass();
        if (!webDriver.findElement(loginElements.getIncorrectPassError()).isDisplayed()) {
            webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        } else {
            Assert.assertEquals(webDriver.findElement(loginElements.getIncorrectPassError()).getText(), "The password you’ve entered is incorrect.\n" +
                    "Please try again.");
        }
    }

    @Test(priority = 4)
    public void bannerPreview() {
        playVideo();
        LOG.info("Check video");
        if (!webDriver.findElement(loginElements.getDisplayedVideo()).isDisplayed()) {
            webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        } else {
            Assert.assertTrue(webDriver.findElement(loginElements.getDisplayedVideo()).isDisplayed());
        }
    }

    @Test(priority = 5)
     public void listOfCategories() throws InterruptedException{
        loginIntoApp();
        Thread.sleep(2500);
        WebElement categories = webDriver.findElement(By.id("categories"));
        LOG.info("Open categories");
        categories.click();
        LOG.info("Choose Nails");
        findElement((By.id("cat_Nails"))).click();
        LOG.info("Check that broadcast in Nails category doesn't exist");
        Assert.assertEquals(findElement(By.className("alert__message")).getText(),"There are no broadcasts in this category. Please, check Home page to explore another broadcasts");

    }

    @Test(priority = 6)
    public void checkVideoUpload() throws InterruptedException {
        loginIntoApp();
        Thread.sleep(2500);
        LOG.info("Click Create Content");
        webDriver.findElement(By.id("create_menu")).click();
        LOG.info("Click Upload Video");
        webDriver.findElement(By.id("create_video")).click();
        WebElement fileUpload = webDriver.findElement(By.xpath("//*[@id=\"upload_area\"]/div/form/input"));
        String filePath = SiteLoginPageTest.class.getClassLoader().getResource("videoplayback.mp4").getPath();
        //String filePath = "/Users/georgehelidze/Fabulive_Automation/src/test/resources/videoplayback.mp4";
        LOG.info("Add video file");
        fileUpload.sendKeys(filePath);
        Thread.sleep(2500);
        LOG.info("Choose thumbnail");
        webDriver.findElement(By.xpath("//*[@id=\"thumb_1\"]")).click();
        LOG.info("Add video name");
        webDriver.findElement(By.xpath("//*[@id=\"video_name_input\"]")).sendKeys("Video" );
        LOG.info("Add video description");
        webDriver.findElement(By.xpath("//*[@id=\"video_description_input\"]")).sendKeys("AutomationTestCreatedBy" );

        ((JavascriptExecutor) webDriver).executeScript(
                "arguments[0].scrollIntoView();", webDriver.findElement(By.xpath("//*[@id=\"video_save_btn\"]")));
        Thread.sleep(2500);
        LOG.info("Click save button");
        webDriver.findElement(By.xpath("//*[@id=\"video_save_btn\"]")).click();
        Thread.sleep(2500);
        LOG.info("Click on avatar button");
        webDriver.findElement(By.xpath("//*[@id=\"account_menu\"]")).click();
        LOG.info("Open profile");
        webDriver.findElement(By.xpath("//*[@id=\"user_link\"]")).click();
        LOG.info("Open video that was added");
        webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/main/div/div/div[2]/section[2]/ul/li/div/article/div[4]/a")).click();
        Thread.sleep(1000);
        LOG.info("Click on video menu button");
        webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/main/div/div/div[1]/div[2]/div/button")).click();
        LOG.info("Click Delete button");
        webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/main/div/div/div[1]/div[2]/div/ul/li/button")).click();
        LOG.info("Accept deletion");
        webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div/div/div/button[2]")).click();
        Thread.sleep(2500);
        LOG.info("Check that video file doesn't exist");
        Assert.assertTrue(!webDriver.getPageSource().contains("/html/body/div[1]/div[1]/main/div/div/div[2]/section[2]/ul/li/div/article/div[4]/footer"));


    }



    @AfterMethod
    public void doAfterMethod(Method method, ITestResult result) throws Exception {
        addScreenShotToReport(result);
        closeBrowserSession();
    }

    @AfterClass
    public void tearDown() {
        webDriver.quit();
    }
}
