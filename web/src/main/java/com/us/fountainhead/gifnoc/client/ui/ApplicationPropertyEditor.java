package com.us.fountainhead.gifnoc.client.ui;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.us.fountainhead.gifnoc.client.entity.Application;
import com.us.fountainhead.gifnoc.client.entity.Environment;
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
            table.setWidget(i+1, 0, new Label(property.getName()));
        }
        table.setWidget(propertyList.length()+1, 0, addProperty);

        JsArray<Environment> environmentList = application.getEnvironmentList();
        for(int i=0; i<environmentList.length(); i++) {
            Environment env = environmentList.get(i);
            table.setWidget(0, i+1, new Label(env.getName()));
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
        table.setWidget(rowCount, 0, new Label(property.getName()));
        table.setWidget(rowCount+1, 0, addProperty);
    }

    public void insertEnvironment(Environment environment) {
        int colCount = table.getCellCount(0);
        table.setWidget(0, colCount, new Label(environment.getName()) );
        table.setWidget(0, colCount+1, addEnvironment);
    }
}
