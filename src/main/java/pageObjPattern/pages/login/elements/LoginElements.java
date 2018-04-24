package pageObjPattern.pages.login.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class LoginElements {


    public LoginElements() {
    }

    public By getSkipButton(){
        return By.xpath("");
    }

    public By getSignInButtonLobby(){
        return By.className("btn--pink");
    }

    public By getSignInButton(){
        return By.id("sign_in");
    }

    public By getSkipButton2(){
        return By.xpath("");
    }

    public By getSignUpAtTheBottom(){
        return By.xpath("");
    }

    public By getDisplayedVideo(){
        return By.xpath("//*[@id=\"advert-video\"]");
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

    public By getEmptyLoginError() {
        return By.className("text-field__error");
    }

    public By getPlayVideoButton() {
        return By.className("btn--play");
    }

    public By getIncorrectPassError() {
        return By.className("popup__text");
    }

    public String loginField = "email-field";

    public String passwordField = "pass-field";



}
