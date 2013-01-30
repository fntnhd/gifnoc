package com.us.fountainhead.gifnoc.client.ui;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.us.fountainhead.gifnoc.client.entity.Application;
import com.us.fountainhead.gifnoc.client.service.ApplicationEntityServiceClient;

/**
 * ApplicationPropertyView
 */
public class ApplicationPropertyView extends VerticalPanel implements ApplicationEntityServiceClient.FindAll,
                                                                      ApplicationEntityServiceClient.FindById {

    private VerticalPanel applicationPanel;
    private VerticalPanel propertyPanel;
    private Button addApplication;

    public ApplicationPropertyView() {
        init();
        findAllApplications();
    }

    private void init() {
        RootPanel.get("ApplicationPropertyView-layout").add(this);
        initApplicationPanel();
        initPropertyPanel();
    }

    private void initApplicationPanel() {
        VerticalPanel p = new VerticalPanel();
        add(p);
        p.addStyleName("section");

        applicationPanel = new VerticalPanel();
        p.add(applicationPanel);

        addApplication = new Button();
        addApplication.setText("+");
        addApplication.addClickHandler(
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent clickEvent) {
                        addApplication();
                    }
                }
        );

        p.add(addApplication);
    }

    private void addApplication() {
        new AddApplicationView(this).center();
    }

    public void findAllApplications() {
        for(int i=applicationPanel.getWidgetCount(); i>0; i--) {
            applicationPanel.remove(i-1);
        }
        new ApplicationEntityServiceClient().findAll(this);
    }

    @Override
    public void onFindAllApplicationResponse(ApplicationEntityServiceClient.FindAllResponse response) {
        JsArray<Application> applicationList = response.getApplicationList();
        for(int i=0; i<applicationList.length(); i++) {
            Application application = applicationList.get(i);
            final ApplicationAnchor anchor = new ApplicationAnchor(application);
            anchor.addClickHandler(
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent clickEvent) {
                        applicationSelected(anchor.getApplication());
                    }
                }
            );

            applicationPanel.add(anchor);
        }
    }

    @Override
    public void onFindAllApplicationError(Throwable exception) {
        add(new Label(exception.getMessage()));
    }

    private void initPropertyPanel() {
        propertyPanel = new VerticalPanel();
        propertyPanel.addStyleName("section");
        add(propertyPanel);
    }

    /**
     * Handles application selected event
     *
     * @param application
     */
    private void applicationSelected(Application application) {
        if(propertyPanel.getWidgetCount()>0) {
            propertyPanel.remove(0);
        }

    // Refresh the application on the page
        new ApplicationEntityServiceClient().findById(application.getId(), this);
    }

    /**
     * Update the property editor
     *
     * @param response
     */
    @Override
    public void onFindApplicationByIdResponse(ApplicationEntityServiceClient.FindByIdResponse response) {
        propertyPanel.add(new ApplicationPropertyEditor(response.getApplication()));
    }

    @Override
    public void onFindApplicationByIdError(Throwable exception) {
        add(new Label(exception.getMessage()));
    }
}