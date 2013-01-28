package com.us.fountainhead.gifnoc.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.us.fountainhead.gifnoc.client.entity.EnvironmentProperty;
import com.us.fountainhead.gifnoc.client.service.EnvironmentPropertyEntityServiceClient;

/**
 *
 */
public class UpdateEnvironmentPropertyView extends PopupPanel {

    private EnvironmentPropertyCell cell;
    private TextBox value;
    private Button update;

    public UpdateEnvironmentPropertyView(EnvironmentPropertyCell environmentPropertyCell) {
        this.cell = environmentPropertyCell;
        init();
    }

    private void init() {
        EnvironmentProperty environmentProperty = cell.getEnvironmentProperty();
        value = new TextBox();
        value.setText(environmentProperty.getValue());

        update = new Button();
        update.setText("Update");
        update.addClickHandler(
            new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    update();
                }
            }
        );

        VerticalPanel layout = new VerticalPanel();
        Label label = new Label();
        label.setText(environmentProperty.getProperty().getName() + " [" + environmentProperty.getEnvironment().getName() + "] Value");
        layout.add(label);
        layout.add(value);
        layout.add(update);
        setWidget(layout);
    }

    private void update() {
        hide();
        EnvironmentProperty ep = cell.getEnvironmentProperty();
        ep.setValue(value.getText());
        cell.setEnvironmentProperty(ep);
    }

}
