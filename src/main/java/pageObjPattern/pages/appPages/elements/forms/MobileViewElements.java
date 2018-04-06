package pageObjPattern.pages.appPages.elements.forms;

import org.openqa.selenium.By;

public class MobileViewElements  {

    /**
     * Download file attachments.
     */

    public By getDownloadAllAttachedFilesButton() {
        return By.id("downloadAllAttachedFiles");
    }

    public By getAnswerDownloadFileFromServerLink(long questionId, long answerId) {
        return By.id("downloadFileFromServer_" + questionId + "_" + answerId);
    }

}
