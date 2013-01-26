package com.us.fountainhead.gifnoc.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.us.fountainhead.gifnoc.client.entity.Property;
import com.us.fountainhead.gifnoc.client.service.PropertyEntityServiceClient;

/**
 */
public class AddPropertyView extends PopupPanel implements PropertyEntityServiceClient.Create  {

    private VerticalPanel layout;
    private TextBox name;
    private Button add;
    private ApplicationPropertyEditor parent;

    public AddPropertyView(ApplicationPropertyEditor parent) {
        this.parent = parent;
        init();
    }

    private void init() {
        layout = new VerticalPanel();
        setWidget(layout);

        name = new TextBox();
        layout.add(new Label("Name"));
        layout.add(name);

        add = new Button();
        add.addClickHandler(
            new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    add(name.getText());
                }
            }
        );
        add.setText("Add");

        layout.add(add);
    }

    private void add(String name) {
        Property property = (Property) Property.createObject();
        property.setName(name);
        property.setApplication(parent.getApplication());

        new PropertyEntityServiceClient().create(property, this);
    }

    @Override
    public void onCreatePropertyResponse(PropertyEntityServiceClient.CreateResponse response) {
        hide();
        parent.insertProperty(response.getProperty());
    }

    @Override
    public void onCreatePropertyError(Throwable exception) {
        layout.add(new Label(exception.getMessage()));
    }
}
