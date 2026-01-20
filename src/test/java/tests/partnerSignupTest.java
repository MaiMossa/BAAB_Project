package tests;



import drivers.GUIDriver;

import org.testng.annotations.Test;

import pages.TempEmailPage;

import pages.partnerSignupPage;

import utils.FakerUtils;



public class partnerSignupTest extends BaseTest {



    @Test

    public void validSignupTC() {

        String firstName = FakerUtils.generateName();

        String lastName = FakerUtils.generateName();

        String phone = FakerUtils.generatePhoneNumber();

        String password = FakerUtils.generateStrongPassword();

        String tempEmailURL = System.getProperty("tempEmail");

        driver.browser().openNewTab();

        driver.browser().switchToLastTab();

        TempEmailPage tempPage = new TempEmailPage(driver);

        tempPage.navigateToTempEmail(tempEmailURL);

        String tempEmail = tempPage.getTempEmail();

        driver.browser().switchToFirstTab();

        new partnerSignupPage(driver)

                .navigateToSignUP()

                .enterEmail(tempEmail)

                .clickContinueBtn()

                .enterFirstname(firstName)

                .enterLast(lastName)

                .selectCountryCode(testData.getJsonData("signUp.valid.countryCode"))

                .enterPhone(phone)

                .clickContinueBtn()

                .enterPassword(password)

                .enterPasswordConfirmation(password)

                .clickCreatePass();

    }

}