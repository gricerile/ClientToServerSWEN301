package test.nz.ac.vuw.swen301.assignment3.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BlackBoxTests {

    @Test
    public void testDoPost() throws Exception {
        URIBuilder builder = new URIBuilder();

        //make appender that creates logs
        //logs don't need layout through requireslayout method
        //but lay them out for the server to enjoy
        //make json converter
        //set value in 'logs' parameter as the json string and send
        builder.setScheme("http").setHost("localhost").setPort(8080).setPath("/resthome4logs/logs");
        URI uri = builder.build();
        // http://localhost:8080/resthome4logs
        HttpClient httpClient = HttpClientBuilder.create().build();
        //post request to server
        HttpPost postRequest = new HttpPost(uri);
        String params = "";
        //make entity and place in request
        postRequest.setEntity(new StringEntity(params, "UTF-8"));
        HttpResponse postResponse = httpClient.execute(postRequest);
        String postContent = EntityUtils.toString(postResponse.getEntity());
        // check whether the web page contains the expected answer
        assertEquals(201,postResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testDoGet() throws Exception {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("localhost").setPort(8080).setPath("/resthome4logs/logs")
                .setParameter("level", "DEBUG").setParameter("limit", "1");
        URI uri = builder.build();
        // http://localhost:8080/resthome4logs
        HttpClient httpClient = HttpClientBuilder.create().build();

        //get request to server
        HttpGet getRequest = new HttpGet(uri);
        HttpResponse getResponse = httpClient.execute(getRequest);
        String getContent = EntityUtils.toString(getResponse.getEntity());
        assertEquals(200,getResponse.getStatusLine().getStatusCode());

    }

}
