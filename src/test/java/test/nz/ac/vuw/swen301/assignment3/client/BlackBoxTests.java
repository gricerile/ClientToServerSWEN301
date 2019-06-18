package test.nz.ac.vuw.swen301.assignment3.client;

import nz.ac.vuw.swen301.assignment3.client.Resthome4LogsAppender;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URI;

import static org.apache.log4j.LogManager.getLogger;
import static org.junit.Assert.*;

public class BlackBoxTests {

    public void doLogs(Logger log){
        log.error("The error was me all along.");
        log.trace("The error was me all along.");
        log.fatal("The error was me all along.");
        log.warn("The error was me all along.");
        log.info("The error was me all along.");
        log.debug("The error was me all along.");
        log.trace("The error was me all along.");
        log.fatal("The error was me all along.");
        log.error("The error was me all along.");
        log.info("The error was me all along.");
    }

    @Test
    public void testDoPost() throws Exception {
        //make appender that creates logs
        Logger log = getLogger("logT1");
        log.setLevel(Level.ALL);
        Resthome4LogsAppender m = new Resthome4LogsAppender();
        log.addAppender(m);
        doLogs(log);
        assertEquals(200, m.getStatus());

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
        //System.out.println(getContent);
        assertTrue(getContent.contains("The error was me all along."));
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

    @Test
    public void testBadDoGet1() throws Exception {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("localhost").setPort(8080).setPath("/resthome4logs/logs")
                .setParameter("level", "BAD").setParameter("limit", "1");
        URI uri = builder.build();
        // http://localhost:8080/resthome4logs
        HttpClient httpClient = HttpClientBuilder.create().build();

        //get request to server
        HttpGet getRequest = new HttpGet(uri);
        HttpResponse getResponse = httpClient.execute(getRequest);
        String getContent = EntityUtils.toString(getResponse.getEntity());
        assertEquals(400,getResponse.getStatusLine().getStatusCode());

    }

    @Test
    public void testBadDoGet2() throws Exception {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("localhost").setPort(8080).setPath("/resthome4logs/logs")
                .setParameter("level", "DEBUG").setParameter("limit", "0");
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
