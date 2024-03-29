package com.us.fountainhead.gifnoc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.us.fountainhead.gifnoc.client.entity.User;
import com.us.fountainhead.gifnoc.client.service.SessionService;
import com.us.fountainhead.gifnoc.client.service.SessionServiceAsync;
import com.us.fountainhead.gifnoc.client.service.UserEntityServiceClient;

/**
 * Example home page that is navigated to upon successful login.
 *
 * Demonstrates proper usage of SessionService and REST service clients.
 *
 * Replace guts of this class with your desired landing page functionality.
 */
public class HomeView extends VerticalPanel implements UserEntityServiceClient.FindAll {

    private FlexTable table;

    public HomeView() {
        layout();
        getCurrentUser();
        getAllUsers();
    }

    /**
     * Layout the widgets on the page
     */
    private void layout() {
        CaptionPanel allUsers = new CaptionPanel("All Users");
        RootPanel.get("HomeView-layout").add(allUsers);
        table = new FlexTable();
        allUsers.add(table);
    }

    /**
     * Get the current user out of the session and display the user name
     */
    private void getCurrentUser() {
        SessionServiceAsync async = GWT.create(SessionService.class);
        AsyncCallback<String> callback = new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable throwable) {
               Window.alert(throwable.getMessage());
            }

            @Override
            public void onSuccess(String json) {
                User user = JsonUtils.safeEval(json);
                RootPanel.get("current-user").add(new Label(user.getName()));
            }
        };

        async.getCurrentUser(callback);
    }

    /**
     * Make a REST service call to get the list of all users in the system
     */
    private void getAllUsers() {
        new UserEntityServiceClient().findAll(this);
    }

    /**
     * FindAllUser service callback.  Present the list of all users in a table
     * @param response
     */
    @Override
    public void onFindAllUserResponse(UserEntityServiceClient.FindAllResponse response) {
        JsArray<User> userList = response.getUserList();

        for (int i = 0; i < userList.length(); i++) {
            User user = userList.get(i);
            table.setWidget(i, 1, new Label(user.getName()));
            table.setWidget(i, 2, new Label(user.getUsername()));
        }
    }

    /**
     * FindAllUser service failure callback.  Just display an error message.
     * @param exception
     */
     @Override
     public void onFindAllUserError(Throwable exception) {
        Window.alert(exception.getMessage());
     }

}
