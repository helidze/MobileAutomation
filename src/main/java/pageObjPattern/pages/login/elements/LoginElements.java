package pageObjPattern.pages.login.elements;

import org.openqa.selenium.By;

public class LoginElements {

    public LoginElements() {
    }

    public By getSkipButton(){
        return By.xpath("");
    }

    public By getSkipButton2(){
        return By.xpath("");
    }

    public By getSignUpAtTheBottom(){
        return By.xpath("");
    }

    public By getSignUpButton(){
        return By.xpath("");
    }

    public By getSignUpWithFacebook(){
        return By.xpath("");
    }

    public By getInputLoginById() {
        return By.id(loginField);
    }

    public String getLoginField() {
        return loginField;
    }

    public String getPasswordField() {
        return passwordField;
    }

    public By getInputPasswordById() {
        return By.id(passwordField);
    }

    public By getLoginButtonById() {
        return By.id("loginButton");
    }

    public By getOldPasswordField() {
        return By.id("oldPassword");
    }

    public By getNewPasswordField() {
        return By.id("newPassword");
    }

    public By getNewPasswordConfirmField() {
        return By.id("newPasswordConfirm");
    }

    public By getSubmitButtonForChangePassword() {
        return By.id("submit");
    }

    public String loginField = "login";

    public String passwordField = "password";

}
