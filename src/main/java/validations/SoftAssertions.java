package validations;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;
import utils.ElementActions;
import utils.LogUtils;
import utils.Waits;

public class SoftAssertions extends BaseAssertions {
    private static boolean used = false; // Flag to track usage
    private static SoftAssert softAssert = new SoftAssert();

    public SoftAssertions(ElementActions elementActions, Waits wait, WebDriver driver) {
        super(elementActions, wait, driver);
    }

    public static void AssertIfUsed() {
        if (!used) return;
        assertAll();
        // if soft assertion is used , it will work
        // if sof assertion not used, will not work
    }


    @Step("Assert all soft assertions")
    private static void assertAll() {
        try {
            softAssert.assertAll();
        } catch (AssertionError e) {
            LogUtils.error("Assertion failed:", e.getMessage());
            throw e;
        } finally {
            resetSoftAssert();
        }
    }


    private static void resetSoftAssert() {
        softAssert = new SoftAssert();
        used = false; // Reset the flag
    }

    @Override
    protected void assertTrue(boolean condition, String message) {
        used = true;
        softAssert.assertTrue(condition, message);
    }

    @Override
    protected void assertFalse(boolean condition, String message) {
        used = true;
        softAssert.assertFalse(condition, message);
    }

    @Override
    protected void assertEquals(String actual, String expected, String message) {
        used = true;
        softAssert.assertEquals(actual, expected, message);
    }

}
