package fabuliveMobAutomation;

import org.openqa.selenium.By;
import pageObjPattern.pages.login.LoginPage;
import pageObjPattern.tests.MobileApplicationExecutor;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import utils.mobile.DriverWindows;

import java.util.concurrent.TimeUnit;

public class LoginPageTestCases extends MobileApplicationExecutor {

    private static Logger LOG = Logger.getLogger(LoginPageTestCases.class);
    private LoginPage loginPage;

    @Test
    public void positiveLogin() {
        mobileDriver.devDriverSwitchTo(DriverWindows.NATIVE_APP.getView());
        mobileDriver.findElement(By.name("Next")).click();
        mobileDriver.findElement(By.name("Next")).click();
        mobileDriver.setImplicitlyWait(2, TimeUnit.SECONDS);
        try {
            mobileDriver.findElement(By.name("Get Started")).click();
        } catch (Exception e) {
            System.out.println(e);
        }
        LOG.info("wait for element");

        mobileDriver.setImplicitlyWait(4, TimeUnit.SECONDS);
        LOG.info("click on start broadcast");
        mobileDriver.findElement(By.name("Start Broadcast")).click();
        mobileDriver.findElement(By.name("Sign In")).click();
        mobileDriver.findElement(By.name("Email")).sendKeys("1@1.com");
        mobileDriver.findElement(By.name("Password")).sendKeys("111111");
        mobileDriver.hideKeyboard();

        mobileDriver.findElement(By.name("Sign In")).click();

        try {
            assertTrue(mobileDriver.findElement(By.name("9")).isDisplayed());
        } catch (Exception e) {
            System.out.println(e);
        }
        //webDriver.findElement(By.name("Skip")).click();


        //((IOSDeviceDriver) mobileDriver).clickXCUIElement(By.xpath("(//XCUIElementTypeOther[@name=\"AX error -25205\"])[109]"));
        //  mobileDriver.findElement(By.xpath("//*[@id=\"selectedElementContainer\"]/div/div[2]/div/div[3]/div/div/div/div/div/table/tbody/tr/td[2]/text()"));
        // String Facebook1 = "(//XCUIElementTypeOther[@name=\"AX error -25205\"])[13]";
        //String Logo = "//XCUIElementTypeImage[@name=icoLogoLa]";
        //String Email = "(//XCUIElementTypeOther[@name=\"AX error -25205\"])[18]";
        //String BottomSignup = "(//XCUIElementTypeStaticText[@name=\"AX error -25205\"])[7]";
        //String Skip = "(//XCUIElementTypeOther[@name=\"AX error -25205\"])[109]";
        //((IOSDeviceDriver) mobileDriver).isXCUIElementDisplayed(By.xpath("(//XCUIElementTypeOther[@name=\"AX error -25205\"])[109]"))
        //((IOSDeviceDriver) mobileDriver).clickXCUIElement(By.xpath("(//XCUIElementTypeOther[@name=\"AX error -25205\"])[109]"))
        //   String Skip2 = mobileDriver.findElement(By.xpath("(//XCUIElementTypeOther[@name=\"AX error -25205\"])[109]"));
        //mini knopka menu =((IOSDeviceDriver) mobileDriver).isXCUIElementDisplayed(By.xpath("(//XCUIElementTypeStaticText[@name=\"AX error -25205\"])[20]"))
        //big knopka menu = driver.findElementByXPath("(//XCUIElementTypeStaticText[@name=\"AX error -25205\"])[1]" label =9
        //StartstreamRose=//XCUIElementTypeImage[@name="btnPurpleLaPlus"] or ByAccessibilityId("btnPurpleLaPlus");


    }
}
