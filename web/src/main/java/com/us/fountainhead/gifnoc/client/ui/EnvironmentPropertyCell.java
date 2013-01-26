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

    private void init() {
        addStyleName("environmentPropertyCell");
        label = new Label();
        label.setText(environmentProperty.getValue());
        setWidget(label);
        addClickHandler(
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent clickEvent) {
                        update();
                    }
                }
        );
    }

    public void setEnvironmentProperty(EnvironmentProperty environmentProperty) {
        this.environmentProperty = environmentProperty;
        label.setText(environmentProperty.getValue());
    }

    public EnvironmentProperty getEnvironmentProperty() {
        return environmentProperty;
    }

    public void update() {
        new UpdateEnvironmentPropertyView(this).center();
    }

}
