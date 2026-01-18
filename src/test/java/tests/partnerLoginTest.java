package tests;

import org.testng.annotations.Test;
import pages.partnerLoginPage;

public class partnerLoginTest extends BaseTest {

    @Test
    public void validLoginTC(){
        new partnerLoginPage(driver)
                .enterUsername(testData.getJsonData("login.valid.email"))
                .clickOnContinue()
                .enterPassword(testData.getJsonData("login.valid.password"))
                .clickOnLogin()
                .verifyLoginSuccessfully();

    }

}
