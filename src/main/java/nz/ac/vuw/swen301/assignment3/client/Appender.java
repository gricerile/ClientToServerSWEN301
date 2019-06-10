package nz.ac.vuw.swen301.assignment3.client;

import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.pattern.LogEvent;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;


import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Appender extends AppenderSkeleton {

    private int maxSize;
    private ArrayList<LogEvent> logEvents = new ArrayList<LogEvent>();
    private int currentSize = 0;
    private int discardedCount = 0;

    public Appender (Layout l, int maxSize){
        this.layout= new PatternLayout();
        this.maxSize = maxSize;
    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        if(this.currentSize<this.maxSize) {
            //LogEvent logEvent = this.layout.format(loggingEvent);
            //this.logEvents.add(logEvent);
            this.currentSize++;
        } else {
            this.discardedCount++;
        }
    }

    public void append(LogEvent loggingEvent) {
        if(this.currentSize<this.maxSize) {
            //String logEvent = this.layout.format(loggingEvent);
            this.logEvents.add(loggingEvent);
            this.currentSize++;
        } else {
            this.discardedCount++;
        }
    }

    public List<LogEvent> getCurrentLogs(){
        List<LogEvent> currentLogs = Collections.unmodifiableList(this.logEvents);
        return currentLogs;
    }

    public long getDiscardedLogCount(){
        return this.discardedCount;
    }

    public void close() {
        this.closed = true;
    }

    public boolean requiresLayout() {
        return false;
    }
}

