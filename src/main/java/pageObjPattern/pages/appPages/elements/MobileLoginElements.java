package pageObjPattern.pages.appPages.elements;

import org.openqa.selenium.By;

public class MobileLoginElements  {

    public By getServerUrl() {
        return By.id("serverUrlField");
    }

    public By getChangeServerURLButton() {
        return By.id("changeServerUrlBtn");
    }

    public By getDoneChangeServerURLButton() {
        return By.id("saveServerUrlBtn");
    }

}
