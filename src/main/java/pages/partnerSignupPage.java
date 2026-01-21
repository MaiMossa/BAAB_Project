package pages;

import drivers.GUIDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class partnerSignupPage {

    private final GUIDriver driver;

    public partnerSignupPage(GUIDriver driver) {
        this.driver = driver;
    }

    private final By signup = By.xpath("//a[normalize-space()='Sign Up']");
    private final By email = By.id("Input_EmailAddress");
    private final By continueBtn = By.cssSelector("button[type='submit']");
    private final By firstName = By.id("Input_Firstname");
    private final By lastName = By.id("Input_Lastname");

    private final By countryDropdown = By.cssSelector(".col-12.col-md-2.mb-3.country-key");
    private final By countryList = By.cssSelector(".iti__country-list li.iti__country");

    private final By phone = By.id("Input_MobileNumber");
    private final By password = By.id("password");
    private final By confirmPassword = By.id("confirmPassword");
    private final By createPassBTN = By.xpath("//button[normalize-space()='Create Password']");
    private final By resendBtn = By.xpath("//button[contains(.,'Resend Confirmation Email')]");
    private final By welcomeMsg = By.xpath("//h1[normalize-space()='Welcome']");


    @Step("Click on Signup hyperlink")
    public partnerSignupPage navigateToSignUP() {
        driver.element().click(signup);
        return this;
    }

    @Step("Enter User Email: {mail}")
    public partnerSignupPage enterEmail(String mail) {
        driver.element().type(email, mail);
        return this;
    }

    @Step("Click Continue Button")
    public partnerSignupPage clickContinueBtn() {
        driver.element().click(continueBtn);
        return this;
    }

    @Step("Enter User First Name: {first}")
    public partnerSignupPage enterFirstname(String first) {
        driver.element().type(firstName, first);
        return this;
    }

    @Step("Enter User Last Name: {last}")
    public partnerSignupPage enterLast(String last) {
        driver.element().type(lastName, last);
        return this;
    }

    @Step("Select Country Code: {countryName}")
    public partnerSignupPage selectCountryCode(String countryName) {
        driver.element().click(countryDropdown);

        List<WebElement> options = driver.element().findElements(countryList);

        for (WebElement option : options) {
            if (option.getText().contains(countryName)) {
                option.click();
                return this;
            }
        }

        throw new RuntimeException("Country '" + countryName + "' not found!");
    }

    @Step("Enter Phone Number: {number}")
    public partnerSignupPage enterPhone(String number) {
        driver.element().type(phone, number);
        return this;
    }

    @Step("Enter Password")
    public partnerSignupPage enterPassword(String pass) {
        driver.element().type(password, pass);
        return this;
    }

    @Step("Enter Password Confirmation")
    public partnerSignupPage enterPasswordConfirmation(String confirmationPass) {
        driver.element().type(confirmPassword, confirmationPass);
        return this;
    }

    @Step("Click Create Password Button")
    public partnerSignupPage clickCreatePass() {
        driver.element().click(createPassBTN);
        return this;
    }

    @Step("Click Resend Confirmation Email")
    public partnerSignupPage clickResendConfirmationEmail() {
        driver.element().click(resendBtn);
        return this;
    }
    @Step("Assert Thank You message is displayed")
    public partnerSignupPage assertThankYouMessageDisplayed() {
        driver.validate().validateElementVisible(welcomeMsg);
        return this;
    }
}
