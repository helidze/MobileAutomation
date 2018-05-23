package pageObjPattern.tests;

import config.AppConfig;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import utils.WorkWithFiles.FileManager;
import utils.webDriverUtils.WebDriversCollection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PlatformSuite implements ISuiteListener {

    protected static final Logger LOG = Logger.getLogger(PlatformSuite.class);
    protected static String resourseLocation = PlatformSuite.class.getClassLoader().getResource("").getPath();
    private int counter = 0;

    @Override
    public void onStart(ISuite suite) {
        if (counter == 0) {
            LOG.info("Platform-Suite start...");
            FileManager.cleanFolderByLocation(resourseLocation + "rerun/");
            FileManager.createEmptyFile(resourseLocation + "rerun/failedTests.txt");
            counter++;

            }
        }


    @Override
    public void onFinish(ISuite suite) {
        LOG.info("Platform-Suite end...");
        ConcurrentHashMap concurrentHashMap = WebDriversCollection.getConcurrentMap();
        Enumeration enumeration = concurrentHashMap.elements();
        Object webDriver;
        int i = 0;
        while (enumeration.hasMoreElements()) {
            i++;
            if (i > 1000) {
                break;
            }
            try {
                webDriver = enumeration.nextElement();
                if (webDriver != null) {
                    ((WebDriver) webDriver).quit();
                    LOG.info("Success close WebDriver: " + webDriver.toString());
                }
            } catch (WebDriverException we) {
                we.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        FileManager.createEmptyFile(resourseLocation + "rerun/rerun_suite.xml");
        List<String> toWriteInFile = new ArrayList<>();
        String mainString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE suite SYSTEM \"http://testng.org/testng-1.0.dtd\">\n" +
                "<suite name=\"WorldApp acceptance rerunning\" parallel=\"tests\" thread-count=\"10\">\n" +
                "    <listeners>\n" +
                "        <listener class-name=\"pageObjPattern.tests.TestsAnalyser\"/>\n" +
                "        <listener class-name=\"pageObjPattern.tests.PlatformSuite\"/>\n" +
                "        <listener class-name=\"org.uncommons.reportng.HTMLReporter\"/>\n" +
                "        <listener class-name=\"org.uncommons.reportng.JUnitXMLReporter\"/>\n" +
                "    </listeners>\n";
        toWriteInFile.add(mainString);

        List<String> stringsFileNoSort = FileManager.parseLocalFileToList("rerun/failedTests.txt");
        Set<String> sortList = new HashSet<>(stringsFileNoSort);
        List<String> stringsFile = new ArrayList<>(sortList);

        if (!stringsFile.isEmpty()) {
            if (!stringsFile.get(0).equals("")) {
                LOG.info("Start create rerun-suite.xml...");
                List<String> uniqueClasses = new ArrayList<>();
                for (String s : stringsFile) {
                    uniqueClasses.add(s.split(":")[0]);
                }
                Set<String> newSortClasses = new HashSet<>(uniqueClasses);
                List<String> uniqueClassesSort = new ArrayList<>(newSortClasses);

                List<List<String>> toAddClassesWithMethods = new ArrayList<>();
                for (String uC : uniqueClassesSort) {
                    List<String> list = new ArrayList<>();
                    for (String s : stringsFile) {
                        if (s.split(":")[0].equals(uC)) {
                            list.add(s);
                        }
                    }
                    toAddClassesWithMethods.add(list);
                }

                for (List<String> l1 : toAddClassesWithMethods) {
                    List<String> methods = new ArrayList<>();
                    String className = "";
                    for (String s : l1) {
                        className = s.split(":")[0];
                        String methodName = null;
                        if (s.split(":").length >= 2) {
                            methodName = s.split(":")[1];
                        }
                        String dependMethod = "";
                        if (s.split(":").length == 3) {
                            LOG.info("!!!" + s);
                            if (s.split(":")[2].split(className).length == 2) {
                                dependMethod = s.split(":")[2].split(className)[1].replace(".", "");
                            } else {
                                try {
                                    dependMethod = s.split(":")[2].split(className)[0].replace(".", "");
                                } catch (ArrayIndexOutOfBoundsException ae) {
                                    ae.printStackTrace();
                                }
                            }
                            methods.add(dependMethod);
                        }
                        methods.add(methodName);
                    }
                    Set<String> methodsSet = new HashSet<>(methods);
                    List<String> sortedMethods = new ArrayList<>(methodsSet);
                    toWriteInFile.add("    <test verbose=\"3\" name=\"" + className + "\">");
                    toWriteInFile.add("        <classes><class name=\"" + className + "\"><methods>");
                    for (String method : sortedMethods) {
                        toWriteInFile.add("            <include name=\"" + method + "\"/>");
                    }
                    toWriteInFile.add("    </methods></class></classes></test>\n");
                }
                toWriteInFile.add("</suite>");

                for (int w = toWriteInFile.size() - 1; w >= 0; w--) {
                    FileManager.addLineToFile(resourseLocation + "rerun/rerun_suite.xml", toWriteInFile.get(w));
                }
                LOG.info("Success create rerun-suite.xml.");
            }
        }
    }
}
