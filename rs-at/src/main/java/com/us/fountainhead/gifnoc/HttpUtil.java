package com.us.fountainhead.gifnoc;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * Performs basic HTTP functions
 */
@Component
public final class HttpUtil {

    private static String SERVICE_HOST;

    public HttpUtil() {
        SERVICE_HOST = System.getProperty("SERVICE_HOST");
    }

    /**
     * Executes an HTTP POST
     *
     * @param url - URL suffix
     * @param body - content to post
     * @return response object
     * @throws IOException
     */
    public JSONObject post(String url, String body) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVICE_HOST + url);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(body));
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        InputStream is = httpEntity.getContent();
        String content = IOUtils.toString(is, "UTF-8");
        return (JSONObject) JSONSerializer.toJSON(content);
    }

    /**
     * Executes an HTTP GET
     * @param url - URL suffix
     * @return response object
     * @throws IOException
     */
    public JSONObject get(String url) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(SERVICE_HOST + url);
        httpGet.setHeader("Content-Type", "application/json");
        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
        InputStream is = httpEntity.getContent();
        String content = IOUtils.toString(is, "UTF-8");
        return (JSONObject) JSONSerializer.toJSON(content);
    }

    /**
     * Executes a HTTP DELETE
     * @param url - URL suffix
     * @return response object
     * @throws IOException
     */
    public JSONObject delete(String url) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpDelete httpDelete = new HttpDelete(SERVICE_HOST + url);
        httpDelete.setHeader("Content-Type", "application/json");
        HttpResponse httpResponse = httpClient.execute(httpDelete);
        HttpEntity httpEntity = httpResponse.getEntity();
        InputStream is = httpEntity.getContent();
        String content = IOUtils.toString(is, "UTF-8");
        return (JSONObject) JSONSerializer.toJSON(content);
    }

}
