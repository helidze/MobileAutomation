package pageObjPattern.tests;

import org.apache.log4j.Logger;
import org.testng.ITestResult;
import org.testng.util.RetryAnalyzerCount;

import java.util.concurrent.atomic.AtomicInteger;

public class Analyzer extends RetryAnalyzerCount   {
    protected static Logger LOG = Logger.getLogger(TestsAnalyser.class);
    public int getCount() {
        return this.count.get();
    }

    private AtomicInteger count = new AtomicInteger(0);
    private static final int MAX_COUNT = 2;


    public Analyzer() {
        setCount(MAX_COUNT);
    }

    @Override
    public boolean retryMethod(ITestResult result) {
        if (count.intValue() < MAX_COUNT) {
            LOG.info("Error in " + result.getName() + " with status "
                    + result.getStatus() + " Retrying " +(count.intValue()+1) + " times of "+MAX_COUNT);
            count.getAndIncrement();
            return true;
        }
        return false;
    }
}