package utils.webDriverUtils;

import com.google.common.collect.ImmutableMap;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import utils.SysUtils;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class WebDriverUtils {

    public synchronized static WebDriver createWebDriverChrome(String fileDownloadPath) {
        WebDriver webDriver = null;
        try {
            HashMap<String, Object> chromePrefs = new HashMap<>();
            chromePrefs.put("profile.default_content_settings.popups", 0);
            chromePrefs.put("credentials_enable_service", false);
            chromePrefs.put("profile.password_manager_enabled", false);
            if (fileDownloadPath != null) {
                chromePrefs.put("download.default_directory", fileDownloadPath);
            }

            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("prefs", chromePrefs);
            options.addArguments("--start-maximized");
            options.addArguments("--disable-extensions");
            options.addArguments("--enable-app-install-alerts");
            options.addArguments("--disable-popup-blocking");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-infobars");

            LoggingPreferences loggingPrefs = new LoggingPreferences();
            loggingPrefs.enable(LogType.BROWSER, Level.ALL);

            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            capabilities.setCapability(CapabilityType.SUPPORTS_ALERTS, true);
            capabilities.setJavascriptEnabled(true);
            capabilities.setCapability(CapabilityType.LOGGING_PREFS, loggingPrefs);

            String pathToChromeDriver;

            for (int i = 0; i < 3; i++) {
                try {
                    String hostAddress = InetAddress.getLocalHost().getHostAddress();
                    String session;
                    if (hostAddress.equals("RemoteChromeDriver")) {
                        System.out.println("-> Try create RemoteChromeDriver!... ");
                        webDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
                        session = ((RemoteWebDriver) webDriver).getSessionId().toString();
                        System.out.println("-> RemoteChromeDriver created!");
                    } else {
                        if (SysUtils.isLinux()) {
                            pathToChromeDriver = WebDriverUtils.class.getClassLoader().getResource("chrome_profile/chromedriver.linux").getPath();
                            System.out.println("-> ChromeDriver for Linux!... ");
                            ChromeDriverService.Builder builder = new ChromeDriverService.Builder()
                                    .usingDriverExecutable(new File(pathToChromeDriver))
                                    .usingAnyFreePort()
                                    .withEnvironment(ImmutableMap.of("DISPLAY", ":0"));

                            ChromeDriverService service = builder.build();
                            webDriver = new ChromeDriver(service, options);
                        } else {
                            String chromeDriver = "chromedriver.exe";
                            if (SysUtils.isMacOS()) {
                                chromeDriver = "chromedriver";
                            }

                            pathToChromeDriver = "/Users/georgehelidze/Fabulive_Automation/src/main/resources/chrome_profile/chromedriver";//WebDriverUtils.class.getClassLoader().getResource("chrome_profile/" + chromeDriver).getPath();


                            System.out.println("-> Try create ChromeDriver!... ");
                            System.setProperty("webdriver.chrome.driver", pathToChromeDriver);
                            webDriver = new ChromeDriver(capabilities);
                        }
                        session = ((ChromeDriver) webDriver).getSessionId().toString();
                    }
                    WebDriversCollection.addWebDriver(session, webDriver);
                    System.out.println("-> ChromeDriver created!");
                    break;
                } catch (Exception e) {
                    System.err.println("Can't create ChromeDriver due to error: " + e.getMessage());
                    SysUtils.sleep(30000);
                }
            }
            Assert.assertTrue(webDriver != null);
            webDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        } catch (Exception e) {
            Assert.fail("Driver can't be created due to !!!" + e);
        }

        return webDriver;
    }

}
