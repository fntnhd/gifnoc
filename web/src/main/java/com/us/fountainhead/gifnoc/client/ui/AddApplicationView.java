package com.us.fountainhead.gifnoc.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.us.fountainhead.gifnoc.client.entity.Application;
import com.us.fountainhead.gifnoc.client.service.ApplicationEntityServiceClient;

/**
 */
public class AddApplicationView extends PopupPanel implements ApplicationEntityServiceClient.Create  {

    private VerticalPanel layout;
    private TextBox name;
    private Button add;
    private ApplicationPropertyView parent;

    public AddApplicationView(ApplicationPropertyView parent) {
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
        Application application = (Application) Application.createObject();
        application.setName(name);

        new ApplicationEntityServiceClient().create(application, this);
    }

    @Override
    public void onCreateApplicationResponse(ApplicationEntityServiceClient.CreateResponse response) {
        hide();
        parent.findAllApplications();
    }

    @Override
    public void onCreateApplicationError(Throwable exception) {
        layout.add(new Label(exception.getMessage()));
    }
}
