package pageObjPattern.tests;

import org.apache.log4j.Logger;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import utils.AppUtil;
import utils.WorkWithFiles.FileManager;

public class TestsAnalyser extends TestListenerAdapter {

    protected static Logger LOG = Logger.getLogger(TestsAnalyser.class);
    private static String location = AppUtil.getRelativePathToImageWithClass(TestsAnalyser.class.getClassLoader().getResource("rerun/failedTests.txt"));

    @Override
    public void onTestSuccess(ITestResult result){
        super.onTestSuccess(result);

        if (result.getMethod().getRetryAnalyzer()!=null&&result.getMethod().getRetryAnalyzer() instanceof Analyzer){
            if (((Analyzer)result.getMethod().getRetryAnalyzer()).getCount()>0){
                LOG.info("Retry attempt # "+((Analyzer)result.getMethod().getRetryAnalyzer()).getCount()+" for " + result.getName()+" is successful");
            }
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        super.onTestFailure(result);
        writeToFile(result);

        if (result.getMethod().getRetryAnalyzer() != null) {
            Reporter.setCurrentTestResult(result);
            if(result.getMethod().getRetryAnalyzer().retry(result)) {
                Reporter.log("Next attempt #"+(((Analyzer)result.getMethod().getRetryAnalyzer()).getCount())+" to execute "+result.getName()+". \r");
               LOG.info("Setting test run attempt status to SUCCESS_PERCENTAGE_FAILURE");
               result.setStatus(ITestResult.SUCCESS_PERCENTAGE_FAILURE);
            } else {
               LOG.info("Retry limit exceeded for " + result.getName());
            }

            Reporter.setCurrentTestResult(null);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        super.onTestSkipped(result);
        writeToFile(result);
    }

    public void writeToFile(ITestResult result){
        String[] dependMethods = result.getMethod().getMethodsDependedUpon();
        if(dependMethods.length != 0){
            FileManager.addLineToFile(location, result.getInstanceName() + ":" + result.getMethod().getMethodName() + ":" + dependMethods[0]);
        }
        else{
            FileManager.addLineToFile(location, result.getInstanceName() + ":" + result.getMethod().getMethodName());
        }
    }

}
