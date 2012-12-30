package com.us.fountainhead.gifnoc;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jbehave.core.annotations.*;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 *  Makes the steps executable
 */
@Component
public class SetAndGetApplicationPropertiesSteps {

    @Autowired
    private HttpUtil httpUtil;

    @Autowired
    private TearDown tearDown;

    private List<String> actualValues;

    @BeforeStory
    public void beforeStory() throws IOException {
        actualValues = new ArrayList<String>();
    }

    /**
     * Sets up initial conditions for the test
     *
     * @param appName - name of the application
     * @param environments - list of environments for the application
     * @param propertyNames - list of property names for the application
     * @param propertyValues - list of property values
     */
    @Given("an application named: $appName with environments: $environments that has properties named: $propertyNames with property values: $propertyValues")
    public void setupProperties(String appName, ExamplesTable environments, ExamplesTable propertyNames, ExamplesTable propertyValues) throws IOException {
        String envName, propertyName, propertyValue;

        JSONObject application = new JSONObject();
        application.put("name", appName);

        JSONArray environmentList = new JSONArray();
        for(Parameters row : environments.getRowsAsParameters()) {
            envName = row.valueAs("environment", String.class);
            JSONObject environment = new JSONObject();
            environment.put("name", envName);
            environmentList.add(environment);
        }
        application.put("environmentList", environmentList);

        JSONArray propertyList = new JSONArray();
        for(Parameters row : propertyNames.getRowsAsParameters()) {
            propertyName = row.valueAs("property", String.class);
            JSONObject property = new JSONObject();
            property.put("name", propertyName);
            propertyList.add(property);
        }
        application.put("propertyList", propertyList);

        httpUtil.post("/application/create", application.toString());

        for(Parameters row : propertyValues.getRowsAsParameters()) {
            envName = row.valueAs("environment", String.class);
            propertyName = row.valueAs("property name", String.class);
            propertyValue = row.valueAs("property value", String.class);

            JSONObject request = new JSONObject();
            request.put("app", appName);
            request.put("environment", envName);
            request.put("propertyName", propertyName);
            request.put("propertyValue", propertyValue);

            httpUtil.post("/propertyService/setPropertyValue", request.toString());
        }
    }

    /**
     * Get the value of valid properties
     * @param propertyNames - Names of properties to get
     */
    @When("I request the values of the following properties: $propertyNames")
    public void getValidPropertyValues(ExamplesTable propertyNames) throws IOException {
        String appName, envName, propertyName, propertyValue;

        for(Parameters row : propertyNames.getRowsAsParameters()) {
            appName = row.valueAs("application", String.class);
            envName = row.valueAs("environment", String.class);
            propertyName = row.valueAs("property name", String.class);

            JSONObject request = new JSONObject();
            request.put("app", appName);
            request.put("environment", envName);
            request.put("property", propertyName);

            JSONObject response = httpUtil.post("/propertyService/getPropertyValue", request.toString());
            actualValues.add(response.getString("value"));
        }
    }

    /**
     * Undefined property case
     *
     * @param propertyNames - Names of properties to get
     */
    @When("I request the values of the following undefined properties: $propertyNames")
    public void getInvalidPropertyNames(ExamplesTable propertyNames) throws IOException {
        String appName, envName, propertyName, actualValue;

        for(Parameters row : propertyNames.getRowsAsParameters()) {
            appName = row.valueAs("application", String.class);
            envName = row.valueAs("environment", String.class);
            propertyName = row.valueAs("property name", String.class);

            JSONObject request = new JSONObject();
            request.put("app", appName);
            request.put("environment", envName);
            request.put("property", propertyName);

            JSONObject response = httpUtil.post("/propertyService/getPropertyValue", request.toString());
            JSONArray errorList = response.getJSONArray("errorList");
            JSONObject error = errorList.getJSONObject(0);

            actualValues.add(error.getString("text"));
        }
    }

    /**
     *
     * @param propertyValues - expected property values
     */
    @Then("the following property values are returned: $propertyValues")
    public void validatePropertyValues(ExamplesTable propertyValues) {
        String expectedPropertyValue;
        int i = 0;
        for(Parameters row : propertyValues.getRowsAsParameters()) {
            expectedPropertyValue = row.valueAs("property value", String.class);
            assertEquals(expectedPropertyValue, actualValues.get(i));
            i++;
        }
    }

    /**
     * Check for expected error messages
     *
     * @param errorMessages - expected error messages
     */
    @Then("the following error messages are received: $errorMessages")
    public void validateErrorMessages(ExamplesTable errorMessages) {
        String expectedErrorMessage;
        int i = 0;
        for(Parameters row : errorMessages.getRowsAsParameters()) {
            expectedErrorMessage = row.valueAs("error message", String.class);
            assertEquals(expectedErrorMessage, actualValues.get(i));
            i++;
        }
    }

    @AfterStory
    public void tearDown() throws IOException {
        tearDown.execute();
    }

}
