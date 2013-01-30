package com.us.fountainhead.gifnoc.client.ui;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.us.fountainhead.gifnoc.client.service.ServiceResponse;
import com.us.fountainhead.gifnoc.client.service.ValidationError;

/**
 * Adds error messages onto a specified panel
 */
public class ServiceResponseErrorHandler {

    /**
     * Add response error messages to a panel
     *
     * @param response
     * @param panel
     */
    public static void addErrorMessages(ServiceResponse response, Panel panel) {
        JsArray<ValidationError> errorList = response.getErrorList();
        if(errorList.length() > 0) {
            for(int i=0; i<errorList.length(); i++) {
                String error = errorList.get(i).getText();
                panel.add(new Label(error));
            }
        }
    }

}
