package pageObjPattern.pages.appPages.elements.forms;

import org.openqa.selenium.By;


public class MobileFormsElements  {

    public By getResponseListElement() {
        return By.id("responsesList");
    }

    public By getFormTab() {
        return By.id("form");
    }

    public By getMobileSyncDialog() {
        return By.xpath("//*[@id='messageDialog' and contains(@class, 'sync-dialog')]");
    }

}
