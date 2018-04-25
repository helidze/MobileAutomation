package pageObjPattern.pages.login.elements;

import org.openqa.selenium.By;

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

    public By getFacebookInputLoginById() {
        return By.id("email");
    }

    public By getFacebookInputPasswordById() {
        return By.id("pass");

    }

    public By getFacebookLoginButton() {
        return By.id("pass");
    }

    public By getFabFacebookButton() {
        return By.xpath("/html/body/div[1]/div[1]/main/div/div/div[1]/form/button[2]");
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

    public By getCategories() {
        return By.id("categories");
    }

    public By getNailsCategory() {
        return By.id("cat_Nails");
    }

    public By getCategoriesAlert() {
        return By.className("alert__message");
    }

    public By getForgotPassButton() {
        return By.xpath("/html/body/div[1]/div[1]/main/div/div/div[1]/form/div[2]/a");
    }


    public By getIncorrectPassError() {
        return By.className("popup__text");
    }
    public By getUserBlockName() {
        return By.className("userblock__username");
    }

    public By getResetPasswordButton() {
        return By.xpath(restPasswordButton);
    }

    public String restPasswordButton = "/html/body/div[1]/div[1]/main/div/div/div/div[1]/form/button";

    public String loginField = "email-field";

    public String passwordField = "pass-field";



}
