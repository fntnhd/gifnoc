package com.us.fountainhead.gifnoc.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.us.fountainhead.gifnoc.client.entity.Application;
import com.us.fountainhead.gifnoc.client.service.ApplicationEntityServiceClient;
import com.us.fountainhead.gifnoc.client.service.PropertyServiceClient;

/**
 */
public class AddApplicationView extends PopupPanel implements PropertyServiceClient.AddApplication {

    private VerticalPanel layout;
    private TextBox name;
    private Button add;
    private ApplicationPropertyView parent;

    public AddApplicationView(ApplicationPropertyView parent) {
        this.parent = parent;
        init();
    }

    private void init() {
        setModal(true);

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
        new PropertyServiceClient().addApplication(name, this);
    }

    @Override
    public void onAddApplication(PropertyServiceClient.AddApplicationResponse response) {
        if(response.hasErrors()) {
            ServiceResponseErrorHandler.addErrorMessages(response, layout);
        }
        else {
            hide();
            parent.findAllApplications();
        }
    }

    @Override
    public void onAddApplicationError(Throwable exception) {
        layout.add(new Label(exception.getMessage()));
    }
}
