package com.us.fountainhead.gifnoc.client.ui;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.us.fountainhead.gifnoc.client.entity.Application;
import com.us.fountainhead.gifnoc.client.entity.Environment;
import com.us.fountainhead.gifnoc.client.entity.EnvironmentProperty;
import com.us.fountainhead.gifnoc.client.entity.Property;

/**
 *
 */
public class ApplicationPropertyEditor extends VerticalPanel {

    private Application application;
    private FlexTable table;
    private Button addProperty, addEnvironment;

    public ApplicationPropertyEditor(Application application) {
        this.application = application;
        init();
    }

    public Application getApplication() {
        return application;
    }

    private void init() {
        add(new Label(application.json()));
        table = new FlexTable();
        table.addStyleName("propertyTable");
        add(table);

        addProperty = new Button();
        addProperty.setText("+");
        addProperty.addClickHandler(
            new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    addProperty();
                }
            }
        );

        addEnvironment = new Button();
        addEnvironment.setText("+");
        addEnvironment.addClickHandler(
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent clickEvent) {
                        addEnvironment();
                    }
                }
        );

        JsArray<Property> propertyList = application.getPropertyList();
        for(int i=0; i<propertyList.length(); i++) {
            Property property = propertyList.get(i);
            PropertyCell cell = new PropertyCell(property);
            table.setWidget(i+1, 0, cell);
        }
        table.setWidget(propertyList.length()+1, 0, addProperty);

        JsArray<Environment> environmentList = application.getEnvironmentList();
        for(int i=0; i<environmentList.length(); i++) {
            Environment env = environmentList.get(i);
            table.setWidget(0, i+1, new EnvironmentCell(env));
        }
        table.setWidget(0, environmentList.length()+1, addEnvironment);
    }

    private void addEnvironment() {
        new AddEnvironmentView(this).center();
    }

    private void addProperty() {
        new AddPropertyView(this).center();
        
    }

    public void insertProperty(Property property) {
        int rowCount = table.getRowCount();
        table.setWidget(rowCount-1, 0, new PropertyCell(property));
        table.setWidget(rowCount, 0, addProperty);
        int colCount = table.getCellCount(0);
        for(int i=1; i<colCount-1; i++) {
            EnvironmentCell environmentCell = (EnvironmentCell) table.getWidget(0, i);
            EnvironmentProperty environmentProperty = (EnvironmentProperty) EnvironmentProperty.createObject();
            environmentProperty.setProperty(property);
            environmentProperty.setEnvironment(environmentCell.getEnvironment());

            EnvironmentPropertyCell cell = new EnvironmentPropertyCell(environmentProperty);
            table.setWidget(rowCount-1, i, cell);
        }
    }

    public void insertEnvironment(Environment environment) {
        int colCount = table.getCellCount(0);
        table.setWidget(0, colCount-1, new EnvironmentCell(environment) );
        table.setWidget(0, colCount, addEnvironment);
        int rowCount = table.getRowCount();
        for(int i=1; i<rowCount-1; i++) {
            PropertyCell propertyCell = (PropertyCell) table.getWidget(i, 0);
            EnvironmentProperty environmentProperty = (EnvironmentProperty) EnvironmentProperty.createObject();
            environmentProperty.setProperty(propertyCell.getProperty());
            environmentProperty.setEnvironment(environment);

            EnvironmentPropertyCell cell = new EnvironmentPropertyCell(environmentProperty);
            table.setWidget(i, colCount-1, cell);
        }
    }
}
