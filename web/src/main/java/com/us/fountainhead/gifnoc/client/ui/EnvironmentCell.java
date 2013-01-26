package com.us.fountainhead.gifnoc.client.ui;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.us.fountainhead.gifnoc.client.entity.Environment;
import com.us.fountainhead.gifnoc.client.entity.Property;

/**
 *
 */
public class EnvironmentCell extends SimplePanel {

    private Environment environment;

    public EnvironmentCell(Environment environment) {
        this.environment = environment;
        addStyleName("environmentCell");
        setWidget(new Label(environment.getName()));
    }

    public Environment getEnvironment() {
        return environment;
    }

}
