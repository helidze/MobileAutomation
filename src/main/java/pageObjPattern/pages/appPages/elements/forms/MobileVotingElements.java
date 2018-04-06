package pageObjPattern.pages.appPages.elements.forms;

import org.openqa.selenium.By;

public class MobileVotingElements  {

    public By getInsertFromCameraButtonById(long qid, long aid) {
        return By.xpath("//*[@class='htmlButton dev_insertCameraButton' and @questionid='" + qid + "' and @answerid='" + aid + "']");
    }

    public By getInsertFromGalleryButtonById(long qid, long aid) {
        return By.xpath("//input[@type='file' and @questionid='" + qid + "' and @answerid='" + aid + "']");
    }

    public By getSubmitAndCopyButton() {
        return By.id("goSubmitCopyPage");
    }

    public By getSaveWhenBackButtonDialog() {
        return By.xpath("//*[@class='BTNoffline saveButton']");
    }

    public By getStarRatingAnswerElementByXpath(String QxAyCz, String classNew) {
        return By.xpath("//div[@class='" + classNew + "']/a[contains(text(), '" + QxAyCz + "')]");
    }

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
