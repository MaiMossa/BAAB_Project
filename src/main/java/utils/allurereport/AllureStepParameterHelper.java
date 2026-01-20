package utils.allurereport;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Parameter;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class to add parameters to Allure steps.
 *
 * This is useful for reporting dynamic data in steps
 * of your Allure test report, allowing better traceability
 * and readability.
 */
public class AllureStepParameterHelper {

    /**
     * Adds multiple parameters to the current Allure step.
     *
     * Each element in the input array should contain two values:
     * - name of the parameter
     * - value of the parameter
     *
     * Example:
     * String[][] params = { {"Username", "testUser"}, {"Password", "secret"} };
     * addStepParameters(params);
     *
     * @param nameValuePairs a 2D array of parameter name-value pairs
     */
    public static void addStepParameters(String[][] nameValuePairs) {
        List<Parameter> parameters = Arrays.stream(nameValuePairs)
                .map(pair -> new Parameter().setName(pair[0]).setValue(pair[1]))
                .toList();
        Allure.getLifecycle().updateStep(step -> step.setParameters(parameters));
    }
}
