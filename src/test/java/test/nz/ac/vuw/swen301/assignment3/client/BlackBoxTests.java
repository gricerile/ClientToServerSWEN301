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
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URI;

import static org.apache.log4j.LogManager.getLogger;
import static org.junit.Assert.*;

public class BlackBoxTests {

    private static Process process;
    private static final String TEST_HOST = "localhost";
    private static final int TEST_PORT = 8080;
    private static final String TEST_PATH = "/resthome4logs"; // as defined in pom.xml
    private static final String SERVICE_PATH = TEST_PATH + "/logs"; // as defined in pom.xml and web.xml
    private static final String SERVICE_STATS_PATH = TEST_PATH + "/stats";


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
        Logger log = getLogger("logT1");
        log.setLevel(Level.ALL);
        Resthome4LogsAppender m = new Resthome4LogsAppender();
        log.addAppender(m);
        doLogs(log);
        assertEquals(200, m.getStatus());

        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(SERVICE_PATH)
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
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(SERVICE_PATH)
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
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(SERVICE_PATH)
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
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(SERVICE_PATH)
                .setParameter("level", "DEBUG").setParameter("limit", "-1");
        URI uri = builder.build();
        // http://localhost:8080/resthome4logs
        HttpClient httpClient = HttpClientBuilder.create().build();

        //get request to server
        HttpGet getRequest = new HttpGet(uri);
        HttpResponse getResponse = httpClient.execute(getRequest);
        String getContent = EntityUtils.toString(getResponse.getEntity());
        assertEquals(400,getResponse.getStatusLine().getStatusCode());

    }

}
