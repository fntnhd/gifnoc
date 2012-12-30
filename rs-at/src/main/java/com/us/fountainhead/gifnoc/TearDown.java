package com.us.fountainhead.gifnoc;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Cleans up after a test
 */
@Component
public class TearDown {

    @Autowired
    private HttpUtil httpUtil;

    public void execute() throws IOException {
    // Delete all the environment properties
        JSONObject response = httpUtil.get("/environmentProperty/findAll");
        JSONArray environmentPropertyList = response.getJSONArray("environmentPropertyList");
        for(int i=0; i<environmentPropertyList.size(); i++) {
            JSONObject environment = (JSONObject) environmentPropertyList.get(i);
            String id = environment.getString("id");
            httpUtil.delete("/environmentProperty/delete/" + id);
        }

        // Delete all the applications
        response = httpUtil.get("/application/findAll");
        JSONArray applicationList = response.getJSONArray("applicationList");
        for(int i=0; i<applicationList.size(); i++) {
            JSONObject application = (JSONObject) applicationList.get(i);
            String id = application.getString("id");
            httpUtil.delete("/application/delete/" + id);
        }

    }

}
