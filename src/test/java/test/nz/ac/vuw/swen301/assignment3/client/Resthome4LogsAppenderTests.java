package test.nz.ac.vuw.swen301.assignment3.client;

import nz.ac.vuw.swen301.assignment3.client.Resthome4LogsAppender;
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

public class Resthome4LogsAppenderTests {

    public void doLogs(Logger log){
        log.setLevel(Level.ALL);
        log.error("The error was me all along.");
        log.trace("The error was me all along.");
        log.fatal("The error was me all along.");
        log.warn("The error was me all along.");
        log.info("The error was me all along.");
        log.debug("The error was me all along.");
        log.trace("The error was me all along.");
        log.fatal("The error was me all along.");
        log.error("The error was me all along.");
    }

    @Test
    public void testLog(){
        Logger log = getLogger("log");

        Resthome4LogsAppender m = new Resthome4LogsAppender();
        log.addAppender(m);

        log.error("The error was me all along.");

        assertTrue(m.getStringEvents().get(0).contains("The error was me all along."));
    }

    @Test
    public void test1Logs(){
        Logger log = getLogger("log");

        Resthome4LogsAppender m = new Resthome4LogsAppender();
        log.addAppender(m);

        //doLogs(log);
        log.error("The error was me all along.");

        assertEquals(1,m.getStringEvents().size());
    }

    @Test
    public void testDifferentLevelLogs(){
        Logger log = getLogger("log");

        Resthome4LogsAppender m = new Resthome4LogsAppender();
        log.addAppender(m);

        doLogs(log);

        assertEquals(9,m.getStringEvents().size());
        assertTrue(m.getStringEvents().get(0).contains("ERROR"));
        assertTrue(m.getStringEvents().get(1).contains("TRACE"));
        assertTrue(m.getStringEvents().get(2).contains("FATAL"));
        assertTrue(m.getStringEvents().get(3).contains("WARN"));
        assertTrue(m.getStringEvents().get(4).contains("INFO"));
        assertTrue(m.getStringEvents().get(5).contains("DEBUG"));
    }

    @Test
    public void testDifferentLoggers(){
        Logger log = getLogger("log");
        Logger log2 = getLogger("log2");
        Resthome4LogsAppender m = new Resthome4LogsAppender();
        log.addAppender(m);
        log2.addAppender(m);

        log2.setLevel(Level.ALL);
        log.error("message here");
        log2.trace("message 2 here");

        assertEquals(2,m.getStringEvents().size());
        assertTrue(m.getStringEvents().get(0).contains("ERROR"));
        assertTrue(m.getStringEvents().get(1).contains("TRACE"));
    }

}
