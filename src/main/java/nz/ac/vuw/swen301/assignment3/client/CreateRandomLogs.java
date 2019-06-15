package nz.ac.vuw.swen301.assignment3.client;

import org.apache.log4j.Logger;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.apache.log4j.LogManager.getLogger;

public class CreateRandomLogs {
    public static void main(String args[]) {
        Resthome4LogsAppender appender = new Resthome4LogsAppender();
        CreateRandomLogs creator = new CreateRandomLogs(appender);
        creator.generateRandomLogs();
    }

    private Resthome4LogsAppender appender;

    public CreateRandomLogs(Resthome4LogsAppender appender){
        this.appender = appender;
    }

    public void generateRandomLogs(){
        Logger log = getLogger("logger-GenerateRandomLogs");
        log.addAppender(this.appender);
        while(true){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String level = getRandomLevel();
            String message = getRandomMessage();
            if(level.equals("ALL")){

            }
            if(level.equals("DEBUG")){
                log.debug(message);
            }
            if(level.equals("INFO")){
                log.info(message);
            }
            if(level.equals("ERROR")){
                log.error(message);
            }
            if(level.equals("FATAL")){
                log.fatal(message);
            }
            if(level.equals("TRACE")){
                log.trace(message);
            }
            else{

            }
        }
    }

    private String getRandomMessage() {
        byte[] array = new byte[10]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        return generatedString;

    }

    private String getRandomLevel() {
        int min = 1;
        int max = 7;
        Random r = new Random();
        int number = r.nextInt((max - min) + 1) + min;
        if(number==1){
            return "ALL";
        }
        if(number==2){
            return "DEBUG";
        }
        if(number==2){
            return "INFO";
        }
        if(number==3){
            return "ERROR";
        }
        if(number==4){
            return "WARN";
        }
        if(number==5){
            return "FATAL";
        }
        if(number==6){
            return "TRACE";
        }
        else{
            return "OFF";
        }
    }
}
