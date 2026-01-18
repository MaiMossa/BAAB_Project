package pages;

import drivers.GUIDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class partnerLoginPage {
    private GUIDriver driver;
    public partnerLoginPage(GUIDriver driver){
        this.driver=driver;
    }
    //locators
    private final By username= By.cssSelector("#Input_Username");
    private final By continueBtn=By.cssSelector("button[type='submit']");
    private final By password=By.id("password");
    private final By loginBtn=By.xpath("//button[normalize-space()='Login']");

    //Actions
    @Step("Enter username in username field")
    public partnerLoginPage enterUsername(String email){
        driver.element().type(username,email);
        return this;
    }
    @Step("Click continue button")
    public partnerLoginPage clickOnContinue(){
        driver.element().click(continueBtn);
        return this;
    }
    @Step("Enter password in password field")
    public partnerLoginPage enterPassword(String pass){
        driver.element().type(password,pass);
        return this;
    }
    @Step("Click login button")
    public partnerHomePage clickOnLogin(){
        driver.element().click(loginBtn);
        return new partnerHomePage(driver);
    }




}
