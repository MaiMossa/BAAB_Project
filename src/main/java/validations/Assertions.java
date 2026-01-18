package validations;


import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import utils.ElementActions;
import utils.Waits;
//Hard Assertion
public class Assertions extends BaseAssertions {

    public Assertions(ElementActions elementActions, Waits wait, WebDriver driver) {
        super(elementActions, wait, driver);
    }

    @Override
    protected void assertTrue(boolean condition, String message) {
        Assert.assertTrue(condition, message);
    }

    @Override
    protected void assertFalse(boolean condition, String message) {
        Assert.assertFalse(condition, message);
    }

    @Override
    protected void assertEquals(String actual, String expected, String message) {
        Assert.assertEquals(actual, expected, message);
    }
}
