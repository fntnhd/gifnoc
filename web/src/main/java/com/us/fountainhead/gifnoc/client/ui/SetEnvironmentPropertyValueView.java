package com.us.fountainhead.gifnoc.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.us.fountainhead.gifnoc.client.entity.EnvironmentProperty;
import com.us.fountainhead.gifnoc.client.service.PropertyServiceClient;

/**
 *
 */
public class SetEnvironmentPropertyValueView extends PopupPanel implements PropertyServiceClient.SetEnvironmentPropertyValue {

    private EnvironmentPropertyCell cell;
    private TextBox propertyValue;
    private Button update;
    private VerticalPanel layout;

    public SetEnvironmentPropertyValueView(EnvironmentPropertyCell environmentPropertyCell) {
        this.cell = environmentPropertyCell;
        init();
    }

    private void init() {
        setModal(true);

        EnvironmentProperty environmentProperty = cell.getEnvironmentProperty();
        propertyValue = new TextBox();
        propertyValue.setText(environmentProperty.getValue());

        update = new Button();
        update.setText("Update");
        update.addClickHandler(
            new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    setEnvironmentPropertyValue();
                }
            }
        );

        layout = new VerticalPanel();
        Label label = new Label();
        label.setText(environmentProperty.getProperty().getName() + " [" + environmentProperty.getEnvironment().getName() + "] Value");
        layout.add(label);
        layout.add(propertyValue);
        layout.add(update);
        setWidget(layout);
    }

    private void setEnvironmentPropertyValue() {
        EnvironmentProperty ep = cell.getEnvironmentProperty();
        new PropertyServiceClient().setEnvironmentPropertyValue(ep.getEnvironment(), ep.getProperty(), propertyValue.getText(), this);
    }

    @Override
    public void onSetEnvironmentPropertyValue(PropertyServiceClient.SetEnvironmentPropertyValueResponse response) {
        if(response.hasErrors()) {
            ServiceResponseErrorHandler.addErrorMessages(response, layout);
        }
        else {
            hide();
            cell.setEnvironmentProperty(response.getValue());
        }
    }

    @Override
    public void onSetEnvironmentPropertyValueError(Throwable exception) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
