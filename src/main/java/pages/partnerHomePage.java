package pages;

import drivers.GUIDriver;
import org.openqa.selenium.By;

public class partnerHomePage {
    private GUIDriver driver;

    private final By username=By.cssSelector("div[class='cursor-pointer prfile-data wed-view'] div div[class='prfile-user']");

    public partnerHomePage(GUIDriver driver) {
        this.driver=driver;    }

   public partnerHomePage verifyLoginSuccessfully(){
        driver.validate().validateElementVisible(username);
        return this;}


}
