package com.us.fountainhead.gifnoc.client.ui;

import com.google.gwt.user.client.ui.Anchor;
import com.us.fountainhead.gifnoc.client.entity.Application;

/**
 *
 */
public class ApplicationAnchor extends Anchor {

    private Application application;

    public ApplicationAnchor(Application application) {
        this.application = application;
        setText(application.getName());
    }

    public Application getApplication() {
        return application;
    }

}
