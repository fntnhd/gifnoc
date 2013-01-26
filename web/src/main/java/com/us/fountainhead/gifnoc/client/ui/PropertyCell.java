package com.us.fountainhead.gifnoc.client.ui;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.us.fountainhead.gifnoc.client.entity.Property;

/**
 *
 */
public class PropertyCell extends SimplePanel {

    private Property property;

    public PropertyCell(Property property) {
        this.property = property;
        addStyleName("propertyCell");
        setWidget(new Label(property.getName()));
    }

    public Property getProperty() {
        return property;
    }
}
