package nz.ac.vuw.swen301.assignment3.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.pattern.LogEvent;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;


import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class Resthome4LogsAppender extends AppenderSkeleton {
    private ArrayList<String> stringEvents = new ArrayList<String>();
    private int count = 0;
    private int status;

    public Resthome4LogsAppender() {
        this.layout= new T1Layout();
    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        String logEvent = this.layout.format(loggingEvent);
        count++;
        this.stringEvents.add(logEvent);
        if(this.stringEvents.size()==10) {//needs to be changed to 10
            this.status = post();
        }
    }

    public int getStatus(){
        return this.status;
    }

    public int post(){
        URIBuilder builder = new URIBuilder();
        //post to server
        builder.setScheme("http").setHost("localhost").setPort(8080).setPath("/resthome4logs/logs");
        URI uri = null;
        try {
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        // http://localhost:8080/resthome4logs
        HttpClient httpClient = HttpClientBuilder.create().build();
        //post request to server
        HttpPost postRequest = new HttpPost(uri);
        ObjectMapper objectMapper = new ObjectMapper();

        ArrayList<String> events = new ArrayList<String>();
        for(int i=0; i<10; i++){//need to send 10
            events.add(this.stringEvents.get(i));
        }
        this.stringEvents.clear();
        String jString = "";
        try {
            jString = objectMapper.writeValueAsString(events);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        //make entity and place in request
        //System.out.println(jString);
        postRequest.setEntity(new ByteArrayEntity(jString.getBytes()));
        HttpResponse postResponse = null;
        try {
            postResponse = httpClient.execute(postRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String postContent = EntityUtils.toString(postResponse.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return postResponse.getStatusLine().getStatusCode();
    }

    protected String getLogJSONString(LoggingEvent loggingEvent) {
        String logEvent = this.layout.format(loggingEvent);
        return logEvent;
    }

    public void close() {
        this.closed = true;
    }

    public boolean requiresLayout() {
        return false;
    }
}

