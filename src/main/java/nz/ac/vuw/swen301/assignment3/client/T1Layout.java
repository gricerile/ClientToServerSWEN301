package nz.ac.vuw.swen301.assignment3.client;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import freemarker.log.Logger;
import freemarker.template.*;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;
import org.springframework.http.converter.json.GsonBuilderUtils;

import javax.security.auth.login.Configuration;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;

import static freemarker.log.Logger.selectLoggerLibrary;

public class T1Layout extends Layout {
    public boolean option = true;
    private String pattern;
    private Template template;
    private final Gson gson = new GsonBuilder().create();

    public T1Layout(){

    }

    public String format(LoggingEvent loggingEvent) {
        Map<String, Object> data = new LinkedHashMap<>();
        //put data into the hashmap
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        data.put("id", randomUUIDString);
        data.put("message", loggingEvent.getMessage());
        long milliSeconds = System.currentTimeMillis();
        String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String date = formatter.format(new Date());
        data.put("timestamp", date);
        data.put("thread", loggingEvent.getThreadName());
        data.put("logger", loggingEvent.getLogger().getName());
        data.put("level", loggingEvent.getLevel().toString());
        if(loggingEvent.getThrowableInformation() != null){
            data.put("errorDetails", loggingEvent.getThrowableInformation().toString());
        } else {
            data.put("errorDetails", "No error Details.");
        }

        return gson.toJson(data)+"\n";
    }

    public boolean ignoresThrowable() {
        return false;
    }

    public void activateOptions() {

    }
}

