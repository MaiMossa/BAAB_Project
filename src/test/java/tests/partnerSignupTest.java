package tests;

import org.testng.annotations.Test;
import pages.partnerSignupPage;
import utils.FakerUtils;
import utils.GuerrillaMailUtils;

public class partnerSignupTest extends BaseTest {

    @Test
    public void validSignupWithEmailVerificationTC() {

        String firstName = FakerUtils.generateName();
        String lastName  = FakerUtils.generateName();
        String phone     = FakerUtils.generatePhoneNumber();
        String password  = FakerUtils.generateStrongPassword();

        GuerrillaMailUtils gm = new GuerrillaMailUtils().enableDebug(true);
        GuerrillaMailUtils.GuerrillaInbox inbox = gm.createInbox();

        partnerSignupPage signup = new partnerSignupPage(driver)
                .navigateToSignUP()
                .enterEmail(inbox.getEmailAddress())
                .clickContinueBtn()
                .enterFirstname(firstName)
                .enterLast(lastName)
                .selectCountryCode(testData.getJsonData("signUp.valid.countryCode"))
                .enterPhone(phone)
                .clickContinueBtn()
                .enterPassword(password)
                .enterPasswordConfirmation(password)
                .clickCreatePass();

        Integer emailId = gm.tryWaitForVerificationEmailId(inbox.getSidToken(), 60_000, 2_000);

        if (emailId == null) {
            signup.clickResendConfirmationEmail();
            emailId = gm.tryWaitForVerificationEmailId(inbox.getSidToken(), 180_000, 2_000);
        }

        if (emailId == null) {
            throw new RuntimeException("Verification email not received even after resend.");
        }

        System.out.println("EMAIL ID = " + emailId);

        String body = gm.fetchEmailBody(inbox.getSidToken(), emailId);
        String confirmationLink = gm.extractConfirmEmailLink(body);
        if (confirmationLink == null || confirmationLink.isBlank()) {
            throw new RuntimeException("No confirmation link found in email body.\nBody:\n" + body);
        }
        driver.browser().navigate(confirmationLink);
        new partnerSignupPage(driver)
                .assertThankYouMessageDisplayed();
    }
}
