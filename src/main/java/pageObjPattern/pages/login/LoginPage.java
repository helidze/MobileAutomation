package pageObjPattern.pages.login;

import config.AppConfig;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import pageObjPattern.basePage.BasePage;

import pageObjPattern.pages.login.elements.LoginElements;
import utils.AlertsText;
import utils.SysUtils;

public class LoginPage extends BasePage {

    private static Logger LOG = Logger.getLogger(LoginPage.class);
    private LoginElements loginElements;

    public LoginPage(WebDriver webDriver) {
        super(webDriver);
        loginElements = new LoginElements();
    }

    /**
     * Login to application.
     */

    public void typeLoginField(String value) {
        LOG.info("CLEAR and TYPE login field.");
        waitForElementDisplayed(loginElements.getInputLoginById());
        clear(loginElements.getInputLoginById());
        typeValueWithJSToFieldWithId(loginElements.getLoginField(), value);
    }

    public boolean isLoginFieldVisible() {
        return isElementDisplayed(loginElements.getLoginButtonById());
    }

    public void typePasswordField(String value) {
        LOG.info("CLEAR and TYPE password field.");
        clear(loginElements.getInputPasswordById());
        typeValueWithJSToFieldWithId(loginElements.getPasswordField(), value);
    }

    public boolean isPasswordFieldVisible() {
        return isElementDisplayed(loginElements.getInputPasswordById());
    }

    public void clickLoginButton() {
        LOG.info("CLICK Login button.");
        waitForElementDisplayed(loginElements.getLoginButtonById());
        click(loginElements.getLoginButtonById());
    }
    public void clickSkipButton() {
        LOG.info("CLICK Skip button.");
        waitForElementDisplayed(loginElements.getSkipButton());
        click(loginElements.getSkipButton());
    }

    public void clickSkipButton2() {
        LOG.info("CLICK Skip button.");
        waitForElementDisplayed(loginElements.getSkipButton2());
        click(loginElements.getSkipButton2());
    }

    public boolean isLoginButtonVisible() {
        return isElementDisplayed(loginElements.getInputLoginById());
    }

    public void typeLoginAndPassword(String userLogin, String userPassword) {
        typeLoginField(userLogin);
        SysUtils.sleep(1000);
        typePasswordField(userPassword);
        SysUtils.sleep(1000);
    }

    public void loginWithAlertCheck(String userLogin, String userPassword) {
        LOG.info("Login to account with user login: '" + userLogin + "'.");
        typeLoginAndPassword(userLogin, userPassword);
        clickLoginButton();
        Alert alert = returnAlertIfPresent();
        if (alert != null) {
            String alertMessage = alert.getText();
            LOG.info("ALERT : " + alertMessage);
            assertNotEquals(alertMessage,AlertsText.getWrongLoginOrPasswordAlertText(), "Alert: " + alertMessage);
            alert.accept();
        }
        LOG.info("Server login is successful.");
    }








    private boolean isLogoutLinkDisplayed() {
        return isElementDisplayed(getLogoutLinkNameByName());
    }



    /**
     * Change Temporary password.
     */

    public void clearAndTypeOldPassword(String value) {
        LOG.info("CLEAR and TYPE old password.");
        clearAndType(loginElements.getOldPasswordField(), value);
    }

    public void clearAndTypeNewPassword(String value) {
        LOG.info("CLEAR and TYPE new password.");
        clearAndType(loginElements.getNewPasswordField(), value);
    }

    public void clearAndTypeNewPasswordConfirm(String value) {
        LOG.info("CLEAR and TYPE confirm new password.");
        clearAndType(loginElements.getNewPasswordConfirmField(), value);
    }

    public void clickSubmitButtonForChangePassword() {
        LOG.info("CLICK Submit button.");
        click(loginElements.getSubmitButtonForChangePassword());
    }

    public void updateTempPasswordWithNew(String tempPass, String newPass) {
        LOG.info("Change password from temporary password to new.");
        clearAndTypeOldPassword(tempPass);
        clearAndTypeNewPassword(newPass);
        clearAndTypeNewPasswordConfirm(newPass);
        clickSubmitButtonForChangePassword();
        waitForElementDisplayedByTime(getLogoutLinkNameByName(), 60);
        LOG.info("Login is successful.");
    }


}
