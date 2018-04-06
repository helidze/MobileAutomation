package pageObjPattern.tests;


import config.AppConfig;
import org.apache.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pageObjPattern.pages.appPages.MobileLoginPage;
import utils.AppUtil;
import utils.TeamCityUtils;
import utils.account.UserLogins;
import utils.mobile.MobileDriver;
import utils.mobile.drivers.AndroidDeviceDriver;
/*import utils.mobile.drivers.DesktopDeviceDriver;*/
import utils.mobile.drivers.IOSDeviceDriver;

import java.io.File;
import java.lang.reflect.Method;

public class MobileApplicationExecutor extends AccountBasicTestsExecutor {

    private static Logger LOG = Logger.getLogger(MobileApplicationExecutor.class);

    protected static MobileDriver mobileDriver;

    //APPLICATION RESOURCES
    private static final String APPLICATION_RESOURCES = "/Users/georgehelidze/Fabulive_Automation/src/test/resources/";
    private static final String DESKTOP_FOLDER = "desktop";
    private static final String ANDROID_FOLDER = "android/";
    private static final String IOS_FOLDER = "ios/";
    private static String appLocation;

    //SURVEY FIELDS
    protected String surveyName;


    //TASK DEFINITION FIELDS
    protected long taskDefinitionId;
    protected String taskDefinitionName;

    //CONTACT MANAGER FIELDS
    protected String contactManagerName;
    protected long contactManagerId;
    protected long lookupContactManagerId;

    protected String respondentLogin;
    protected String respondentPassword;

    //REST SERVICES


    //MOBILE PAGES
    protected MobileLoginPage mobileLoginPage;

    //TEST RESOURCES
    protected final static String UPLOAD_IMAGE_PATH = "/Users/georgehelidze/Fabulive_Automation/src/test/resources/ios/Mobile.iOS.ipa";

    static {
        String appVersion = AppConfig.getAppVersion();
        LOG.info("# MOBILE VERSION: " + appVersion);
        if (AppConfig.isAndroid()) {
            appLocation = APPLICATION_RESOURCES + ANDROID_FOLDER + "Mobile" + appVersion + ".apk";
            checkApplicationFileForExisting(appLocation);
            mobileDriver = new AndroidDeviceDriver();
        }
        if (AppConfig.isIOS()) {
            appLocation = APPLICATION_RESOURCES + IOS_FOLDER + "Mobile.IOS.ipa";
            checkApplicationFileForExisting(appLocation);
            mobileDriver = new IOSDeviceDriver();
        }
        /*if (AppConfig.isDesktop()) {
            appLocation = APPLICATION_RESOURCES + DESKTOP_FOLDER;
            mobileDriver = new DesktopDeviceDriver();
        }*/
        LOG.info("# APPLICATION LOCATION: " + appLocation);
    }

    @BeforeSuite
    public void basicBeforeSuite() {
        LOG.info("# BEFORE SUITE. Check mobileDriver Server.");
        if (mobileDriver != null) {
            mobileDriver.startMobileServer();
        }
    }

    @BeforeClass(alwaysRun = true)
    public void basicBeforeTestCase() {
        userLogin = UserLogins.USER_PORTAL.getUserLogin();
        userPassword = UserLogins.USER_PORTAL.getUserPassword();
    }

    @BeforeMethod(alwaysRun = true)
    public void basicBeforeMethod(Method method) {
        LOG.info("# BEFORE METHOD. Check mobileDriver instance.");
        if (mobileDriver != null) {
            mobileDriver.startMobileDriver(appLocation);
            mobileLoginPage = new MobileLoginPage(mobileDriver);
        }
        surveyName = method.getName() + "_" + System.currentTimeMillis();
        taskDefinitionName = surveyName;
    }

   /* @AfterMethod(alwaysRun = true)
    public void basicAfterMethod(Method method, ITestResult result) throws Exception {
        LOG.info("# AFTER METHOD. Prepare for next test execution.");
        if (mobileDriver instanceof DesktopDeviceDriver) {
            mobileDriver.takeScreenShot(result);
        }
        clearDataAfterTest(surveyId, taskDefinitionId, null);
        mobileDriver.close();
    }*/

  /*  @AfterClass(alwaysRun = true)
    public void basicAfterClass() {
        clearDataAfterTest(null, null, contactManagerId);
    }*/

    @AfterSuite
    public void basicAfterSuite() {
        LOG.info("# AFTER SUITE. Quite mobile driver.");
        if (mobileDriver != null) {
            mobileDriver.quit();
        }
    }

    private static void checkApplicationFileForExisting(String pathToFile) {
        File file = new File(pathToFile);
        if (!file.exists()) {
            if (!AppConfig.getTeamcityUrl().equals("")) {
                new TeamCityUtils().downloadApplication(AppConfig.getUrlFromDownloading(), pathToFile);
            }
        }
    }

}
