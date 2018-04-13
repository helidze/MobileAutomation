package config;

import utils.AppUtil;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AppConfig {

    private static final String PROPERTY_FILE_NAME = "config1.properties";
    private static final String DEFAULT_ADMIN_LOGIN = "1@1.com";
    private static final String DEFAULT_ADMIN_PASS = "111111";
    private static final String START_URL_MAIN;
    private static final String HOST;



    private static final String BUILD_NAME;
    private static final String PROCESSING_RESULT;
    private static final String PLAN_NAME;
    private static final String TEAMCITY_BUILD_ID;
    private static final String TEAMCITY_BUILD_TYPE_ID;


    private static final String USE_GRID;
    private static final String IOS;
    private static final String ANDROID;
    private static final String DESKTOP;
    private static final String TEAMCITY_URL;
    private static final String APP_VERSION;
    private static final String URL_FROM_DOWNLOADING;






    static {
        System.out.println("==> LOAD PROPERTY FILE : " + PROPERTY_FILE_NAME + "\n");


        Properties properties = AppUtil.loadPropertiesFromClassPath(PROPERTY_FILE_NAME);
        assert properties != null;
        String hostPropertyValue = properties.getProperty("START_URL_MAIN");
        HOST = hostPropertyValue;
        System.err.println("==> HOST : " + HOST + "\n");
        START_URL_MAIN = "https://" + hostPropertyValue;

        PLAN_NAME = properties.getProperty("PLAN_NAME");
        System.err.println("==> PLAN_NAME : " + PLAN_NAME + "\n");

        BUILD_NAME = properties.getProperty("BUILD_NAME");
        System.err.println("==> BUILD_NAME : " + BUILD_NAME + "\n");

        PROCESSING_RESULT = properties.getProperty("PROCESSING_RESULT");
        System.err.println("==> PROCESSING_RESULT : " + PROCESSING_RESULT + "\n");

        TEAMCITY_BUILD_ID = "";
        TEAMCITY_BUILD_TYPE_ID = "";

        USE_GRID = properties.getProperty("USE_GRID_FOR_DESKTOP");


        IOS = properties.getProperty("IOS");
        ANDROID = properties.getProperty("ANDROID");
        DESKTOP = properties.getProperty("DESKTOP");
        TEAMCITY_URL = properties.getProperty("TEAMCITY_URL");
        URL_FROM_DOWNLOADING = getURLForFileDownloading();
        APP_VERSION = getVersion();


    }
    public static String getAppVersion() {
        return APP_VERSION;
    }

    public static String getDesktop() {
        return DESKTOP;
    }

    public static String getTeamcityUrl() {
        return TEAMCITY_URL;
    }

    public static String getUrlFromDownloading() {
        return URL_FROM_DOWNLOADING;
    }



    public static String getPlanName() {
        return PLAN_NAME;
    }

    public static boolean getProcessingResult() {
        return PROCESSING_RESULT.equals("true");
    }

    public static String getBuildName() {
        return BUILD_NAME;
    }

    public static String getTeamcityBuildId() {
        return TEAMCITY_BUILD_ID;
    }

    public static String getTeamcityBuildTypeId() {
        return TEAMCITY_BUILD_TYPE_ID;
    }

    public static String getAndroid() {
        return ANDROID;
    }

    public static String getIOS() {
        return IOS;
    }



    private static String getUseGrid() {
        return USE_GRID;
    }

    public static boolean isUseGrid() {
        return Boolean.valueOf(AppConfig.getUseGrid());
    }



    public static boolean isIOS() {
        return AppConfig.getIOS().equals("true");
    }

    public static boolean isAndroid() {
        return AppConfig.getAndroid().equals("true");
    }

    public static boolean isDesktop() {
        return AppConfig.getDesktop().equals("true");
    }

    private static String getURLForFileDownloading() {
        String buildVersion = getVersion();
        String teamName = getTeamName();
        String teamCityName = getTeamCityName();
        String teamCityURL = teamCityName + "/repository/download/FabuliveClientMobile_AppBuild/264";
        String urlForDownloading = teamCityName + "/repository/download/" + teamName + "/" + buildVersion;
        if (AppConfig.getAndroid().equals("true")) {
            urlForDownloading = teamCityURL + ":id/Android/Form.com-debug.apk";
        }
        if (AppConfig.getIOS().equals("true")) {
            urlForDownloading = teamCityURL + ":id/Mobile.IOS.ipa";
        }
        return urlForDownloading;
    }

    private static String getVersion() {
        Pattern pat = Pattern.compile("buildId=\\d+");
        Matcher matcher = pat.matcher(TEAMCITY_URL);
        String version = "";
        while (matcher.find()) {
            version = matcher.group().split("buildId=")[1];
        }
        return version;
    }

    private static String getTeamName() {
        Pattern pat = Pattern.compile("\\w+?_FabuliveClientMobile_AppBuild");
        Matcher matcher = pat.matcher(TEAMCITY_URL);
        String version = "";
        while (matcher.find()) {
            version = matcher.group().split(":id")[0];
        }
        return version;
    }

    public static String getStartUrl() {
        return START_URL_MAIN;
    }

    private static String getTeamCityName() {
        return TEAMCITY_URL.split("/viewLog")[0];
    }

}