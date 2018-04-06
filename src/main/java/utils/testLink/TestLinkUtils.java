package utils.testLink;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionStatus;
import br.eti.kinoshita.testlinkjavaapi.model.Build;
import br.eti.kinoshita.testlinkjavaapi.model.TestPlan;
import br.eti.kinoshita.testlinkjavaapi.util.TestLinkAPIException;
import config.AppConfig;
import org.apache.log4j.Logger;
import org.testng.ITestResult;
import pageObjPattern.CustomWriterAppender;
import utils.SysUtils;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class TestLinkUtils {

    private static TestLinkAPI apiTestLink;
    private static int testPlanId;
    private static int buildId;
    private static String buildName;

    private static Map<String, String> platformMap = new HashMap<String, String>();

    private static final String PROJECT_NAME = "form.com";
    private static final String DEV_KEY = "8e3d2bef10be7d6fdd6ea3625f33f37f";
    private static final String SERVER_URL = "http://testlink.b1.tenet:80/lib/api/xmlrpc/v1/xmlrpc.php";

    protected static Logger LOG = Logger.getLogger(TestLinkUtils.class);

    public TestLinkUtils(String buildName) {
        TestLinkUtils.buildName = buildName;
        createTLApi();
    }

    private void createTLApi() {
        URL testlinkURL = null;
        try {
            testlinkURL = new URL(SERVER_URL);
        } catch (MalformedURLException e) {
            System.err.println("-> " + e);
        }
        apiTestLink = new TestLinkAPI(testlinkURL, DEV_KEY);
        TestPlan testPlan = apiTestLink.getTestPlanByName(AppConfig.getPlanName(), PROJECT_NAME);
        testPlanId = testPlan.getId();
        apiTestLink.createBuild(testPlanId, buildName, "Auto generated. " );
        Build[] build = apiTestLink.getBuildsForTestPlan(testPlanId);
        for (Build buildCurrent : build) {
            String buildCurrentName = buildCurrent.getName();
            if (buildCurrentName.equals(buildName)) {
                buildId = buildCurrent.getId();
            }
        }

        String osVersionAndDevice = System.getProperty("os.name");
        platformMap.put("System environment: ", osVersionAndDevice);
    }

    private String getScreenShotForTestLink(ITestResult result) {
        return "http://teamcity.t1.tenet:8111/repository/download/" + AppConfig.getTeamcityBuildTypeId() + "/" + AppConfig.getTeamcityBuildId() + ":id/second_rerun.zip!/surefire-rerun/html/screenshots/failed/" + result.getName() + "/" + "screenshot.png";
    }

    public void passResultsToTestLink(ITestResult result, int internalTestId, int externalTestId, String notes) {
        char execStatus = 'n';
        if (result.isSuccess()) {
            execStatus = 'p';
        }
        if (!result.isSuccess()) {
            execStatus = 'f';
        }
        try {
            apiTestLink.setTestCaseExecutionResult(internalTestId, externalTestId, testPlanId, ExecutionStatus.getExecutionStatus(execStatus), buildId, buildName, notes, true, null, null, null, platformMap, true);
        } catch (TestLinkAPIException ex) {
            System.out.println("-> Can not pass results to TestLink :" + ex.getMessage());
            SysUtils.sleep(3000);
            apiTestLink.setTestCaseExecutionResult(internalTestId, externalTestId, testPlanId, ExecutionStatus.getExecutionStatus(execStatus), buildId, buildName, notes, true, null, null, null, platformMap, true);
        }
    }

    public String generateNotesMessageForTestLink(Method method, ITestResult result, int errorMessageLength) {
        String separator = "----------------------------------\n\n";
        String testCaseNotes;
        if (result.isSuccess()) {
            testCaseNotes = separator + "Test passed successfully.\n";
        } else {
            testCaseNotes = getNotesForFailedTestCase(method, result, errorMessageLength, separator);
        }
        return testCaseNotes;
    }

    private String getNotesForFailedTestCase(Method method, ITestResult result, int errorMessageLength, String separator) {
        String testCaseNotes;
        if (result.getThrowable() != null) {
            testCaseNotes = generateNotesForFailedTests(method, result, errorMessageLength, separator);
        } else {
            String errorMsg = "Can't get exception. Possible reason test skipped on before or after method.\n";
            System.out.println(errorMsg);
            testCaseNotes = separator + errorMsg;
        }
        return testCaseNotes;
    }

    private String generateNotesForFailedTests(Method method, ITestResult result, int errorMessageLength, String separator) {
        String exception = result.getThrowable().toString();
        if ((exception != null) && (exception.length() > errorMessageLength)) {
            exception = exception.substring(0, errorMessageLength) + "...";
        }
        String testLog = CustomWriterAppender.getBufferContents(method.getName()).replace("</br>", "");
        String resolution = separator + "Test failed due to error:\n" + exception + "\n" + separator + testLog;
        String pathToScreenShot = separator + "Link to screenshot :\n" + getScreenShotForTestLink(result) + "\n" + separator;
        System.out.println("Path to screenshot: " + pathToScreenShot);
        return resolution + pathToScreenShot;
    }

}
