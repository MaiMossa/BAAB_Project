package pages;

import drivers.GUIDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.Waits;
import java.util.List;

public class TempEmailPage {

    private GUIDriver driver;
    private Waits waits;

    public TempEmailPage(GUIDriver driver) {
        this.driver = driver;
        this.waits = new Waits(driver.get());
    }

    private final By emailField = By.id("mail");
    private final By emailRows = By.cssSelector(".inbox-dataList .title-subject");
    private final By emailBodyCode = By.cssSelector(".inbox-data-content .code");

    @Step("Navigate to Temp Mail")
    public TempEmailPage navigateToTempEmail(String url){
        driver.browser().navigate(url);
        return this;
    }

    @Step("Get temporary email from Temp Mail")
    public String getTempEmail() {
        waits.waitForElementVisible(emailField);
        // Wait until the "Loading" placeholder disappears from the value attribute
        waits.waitForElementTextNotToContain(emailField, "Loading");
        return driver.element().findElement(emailField).getAttribute("value");
    }


}