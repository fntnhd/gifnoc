package com.us.fountainhead.gifnoc.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.us.fountainhead.gifnoc.client.service.PropertyServiceClient;

/**
 *
 */
public class AddEnvironmentView extends PopupPanel implements PropertyServiceClient.AddEnvironment {

    private VerticalPanel layout;
    private TextBox name;
    private Button add;
    private ApplicationPropertyEditor parent;

    public AddEnvironmentView(ApplicationPropertyEditor parent) {
        this.parent = parent;
        init();
    }

    private void init() {
        setModal(true);

        layout = new VerticalPanel();
        setWidget(layout);

        name = new TextBox();
        layout.add(new Label("Environment Name"));
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
        new PropertyServiceClient().addEnvironment(parent.getApplication(), name, this);
    }

    @Override
    public void onAddEnvironment(PropertyServiceClient.AddEnvironmentResponse response) {
        if(response.hasErrors()) {
            ServiceResponseErrorHandler.addErrorMessages(response, layout);
        }
        else {
            hide();
            parent.insertEnvironment(response.getValue());
        }
    }

    @Override
    public void onAddEnvironmentError(Throwable exception) {
        layout.add(new Label(exception.getMessage()));
    }
}
