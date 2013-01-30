package com.us.fountainhead.gifnoc.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.us.fountainhead.gifnoc.client.entity.EnvironmentProperty;

/**
 *
 */
public class EnvironmentPropertyCell extends FocusPanel {

    private EnvironmentProperty environmentProperty;
    private Label label;

    public EnvironmentPropertyCell(EnvironmentProperty environmentProperty) {
        this.environmentProperty = environmentProperty;
        init();
    }

    /**
     * Initialize the view
     */
    private void init() {
        addStyleName("environmentPropertyCell");
        label = new Label();
        label.setText(environmentProperty.getValue());
        setWidget(label);
        addClickHandler(
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent clickEvent) {
                        setEnvironmentPropertyValue();
                    }
                }
        );
    }

    /**
     * Set the environment property
     * @param environmentProperty
     */
    public void setEnvironmentProperty(EnvironmentProperty environmentProperty) {
        this.environmentProperty = environmentProperty;
        label.setText(environmentProperty.getValue());
    }

    /**
     *
     * @return EnvironmentProperty
     */
    public EnvironmentProperty getEnvironmentProperty() {
        return environmentProperty;
    }

    public void setEnvironmentPropertyValue() {
        new SetEnvironmentPropertyValueView(this).center();
    }
}
