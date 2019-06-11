package test.nz.ac.vuw.swen301.assignment3.client;

import nz.ac.vuw.swen301.assignment3.client.Appender;
import nz.ac.vuw.swen301.assignment3.client.T1Layout;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.pattern.LogEvent;
import org.junit.Test;

import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import static org.apache.log4j.LogManager.getLogger;
import static org.junit.Assert.*;

public class BlackBoxTests {

    @Test
    public void testDoPost() throws Exception {

        //make appender that creates logs
        Logger log = getLogger("logT1");
        Appender m = new Appender();
        log.addAppender(m);
        log.error("The error was me all along.");
        //assertEquals(201,postResponse.getStatusLine().getStatusCode());

        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("localhost").setPort(8080).setPath("/resthome4logs/logs")
                .setParameter("level", "ERROR").setParameter("limit", "1");
        URI uri = builder.build();
        // http://localhost:8080/resthome4logs
        HttpClient httpClient = HttpClientBuilder.create().build();

        //get request to server
        HttpGet getRequest = new HttpGet(uri);
        HttpResponse getResponse = httpClient.execute(getRequest);
        String getContent = EntityUtils.toString(getResponse.getEntity());
        assertEquals(200,getResponse.getStatusLine().getStatusCode());
        //assertNotNull(getResponse.getHeaders("logs"));
        //System.out.print(getResponse.getHeaders("logs"));
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
        assertNotNull(getResponse.getHeaders("logs"));

    }

}
