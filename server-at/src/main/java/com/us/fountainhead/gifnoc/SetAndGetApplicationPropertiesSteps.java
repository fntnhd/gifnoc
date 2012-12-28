package com.us.fountainhead.gifnoc;

import com.us.fountainhead.gifnoc.server.entity.*;
import com.us.fountainhead.gifnoc.server.service.PropertyService;
import com.us.fountainhead.gifnoc.server.validate.ValidationException;
import org.jbehave.core.annotations.AfterStory;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 *  Makes the steps executable
 */
@Component
public class SetAndGetApplicationPropertiesSteps {

    @Autowired
    PropertyService propertyService;

    @Autowired
    ApplicationDAO applicationDAO;

    @Autowired
    EnvironmentPropertyDAO environmentPropertyDAO;

    @Autowired
    EnvironmentDAO environmentDAO;

    @Autowired
    PropertyDAO propertyDAO;

    String appName;
    List<String> actualValues;


    @Given("an application named: $appName with environments: $environmentsTable that has properties named: $propertiesTable with property values: $valuesTable")
    public void setupProperties(String appName, ExamplesTable environmentsTable, ExamplesTable propertiesTable, ExamplesTable valuesTable) throws ValidationException {
        String envName = null;
        String propertyName = null;
        String propertyValue = null;
        this.appName = appName;
        Application application = new Application();
        application.setName(appName);

        for(Parameters row : environmentsTable.getRowsAsParameters()) {
            envName = row.valueAs("environment", String.class);
            Environment environment = new Environment();
            environment.setName(envName);
            application.addEnvironment(environment);
        }

        for(Parameters row : propertiesTable.getRowsAsParameters()) {
            propertyName = row.valueAs("property", String.class);
            Property property = new Property();
            property.setName(propertyName);
            application.addProperty(property);
        }

        propertyService.createApplication(application);

        for(Parameters row : valuesTable.getRowsAsParameters()) {
            envName = row.valueAs("environment", String.class);
            propertyName = row.valueAs("property name", String.class);
            propertyValue = row.valueAs("property value", String.class);

            propertyService.setPropertyValue(appName, envName, propertyName, propertyValue);
        }
    }

    @When("I request the values of the following properties: $propertyTable")
    public void setPropertyValues(ExamplesTable propertyTable) throws ValidationException {
        String envName = null;
        String propertyName = null;
        String propertyValue = null;
        actualValues = new ArrayList<String>();

        for(Parameters row : propertyTable.getRowsAsParameters()) {
            envName = row.valueAs("environment", String.class);
            propertyName = row.valueAs("property name", String.class);
            propertyValue = propertyService.getPropertyValue(appName, envName, propertyName);
            actualValues.add(propertyValue);
        }
    }

    @Then("the following property values are returned: $propertyValuesTable")
    public void validatePropertyValues(ExamplesTable propertyValuesTable) throws ValidationException {
        String expectedPropertyValue = null;
        int i = 0;
        for(Parameters row : propertyValuesTable.getRowsAsParameters()) {
            expectedPropertyValue = row.valueAs("property value", String.class);
            assertEquals(expectedPropertyValue, actualValues.get(i));
            i++;
        }
    }

    @AfterStory
    public void tearDown() throws ValidationException {
        environmentPropertyDAO.deleteAll();
        environmentDAO.deleteAll();
        propertyDAO.deleteAll();
        applicationDAO.deleteAll();
    }
}
