package com.us.fountainhead.gifnoc.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.us.fountainhead.gifnoc.client.entity.Environment;
import com.us.fountainhead.gifnoc.client.service.EnvironmentEntityServiceClient;

/**
 */
public class AddEnvironmentView extends PopupPanel implements EnvironmentEntityServiceClient.Create  {

    private VerticalPanel layout;
    private TextBox name;
    private Button add;
    private ApplicationPropertyEditor parent;

    public AddEnvironmentView(ApplicationPropertyEditor parent) {
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
        Environment environment = (Environment) Environment.createObject();
        environment.setName(name);
        environment.setApplication(parent.getApplication());

        new EnvironmentEntityServiceClient().create(environment, this);
    }

    @Override
    public void onCreateEnvironmentResponse(EnvironmentEntityServiceClient.CreateResponse response) {
        hide();
        parent.insertEnvironment(response.getEnvironment());
    }

    @Override
    public void onCreateEnvironmentError(Throwable exception) {
        layout.add(new Label(exception.getMessage()));
    }
}
