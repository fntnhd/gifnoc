package com.us.fountainhead.gifnoc.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.us.fountainhead.gifnoc.client.service.PropertyServiceClient;

/**
 */
public class AddPropertyView extends PopupPanel implements PropertyServiceClient.AddProperty  {

    private VerticalPanel layout;
    private TextBox name;
    private Button add;
    private ApplicationPropertyEditor parent;

    public AddPropertyView(ApplicationPropertyEditor parent) {
        this.parent = parent;
        init();
    }

    private void init() {
        setModal(true);

        layout = new VerticalPanel();
        setWidget(layout);

        name = new TextBox();
        layout.add(new Label("Property Name"));
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
        new PropertyServiceClient().addProperty(parent.getApplication(), name, this);
    }

    @Override
    public void onAddProperty(PropertyServiceClient.AddPropertyResponse response) {
        if(response.hasErrors()) {
            ServiceResponseErrorHandler.addErrorMessages(response, layout);
        }
        else {
            hide();
            parent.insertProperty(response.getValue());
        }
    }

    @Override
    public void onAddPropertyError(Throwable exception) {
        layout.add(new Label(exception.getMessage()));
    }
}
