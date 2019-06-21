package nz.ac.vuw.swen301.assignment3.client;


import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.log4j.LogManager.getLogger;

public class LogMonitor {

    private static final String TEST_HOST = "localhost";
    private static final int TEST_PORT = 8080;
    private static final String TEST_PATH = "/resthome4logs"; // as defined in pom.xml
    private static final String SERVICE_PATH = TEST_PATH + "/logs"; // as defined in pom.xml and web.xml
    private static final String SERVICE_STATS_PATH = TEST_PATH + "/stats";

    private JFrame frame;
    private JTextField textField;
    private JTable table;
    private DefaultTableModel model;

    public LogMonitor() {
        initialize();
    }

    private void initialize() {
        //mainframe for the entire interface is created here
        frame = new JFrame();
        frame.setBounds(100, 100, 1000, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        //download button with action attached
        JButton sendLogs = new JButton("Send Test Logs");
        sendLogs.setBounds(750, 20, 200, 34);
        sendLogs.setFont(new Font("Arial", Font.PLAIN, 18));
        frame.getContentPane().add(sendLogs);

        //download button with action attached
        JButton download = new JButton("Download");
        download.setBounds(566, 20, 151, 34);
        download.setFont(new Font("Arial", Font.PLAIN, 18));
        frame.getContentPane().add(download);

        //fetch button with action attached
        JButton fetch = new JButton("Fetch Logs");
        fetch.setBounds(405, 20, 151, 34);
        fetch.setFont(new Font("Arial", Font.PLAIN, 18));
        frame.getContentPane().add(fetch);

        //label for the text field
        JLabel limitLabel = new JLabel("Limit:");
        limitLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        limitLabel.setBounds(246, 31, 46, 13);
        frame.getContentPane().add(limitLabel);

        //text field to enter the number of logs desired
        textField = new JTextField();
        textField.setBounds(299, 20, 96, 34);
        textField.setFont(new Font("Arial", Font.PLAIN, 18));
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        //label for the combobox
        JLabel levelLabel = new JLabel("Level:");
        levelLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        levelLabel.setBounds(29, 20, 82, 34);
        frame.getContentPane().add(levelLabel);

        //drop down combo box with options for the desired type of logs to be selected
        final JComboBox comboBox = new JComboBox();
        comboBox.setBounds(120, 20, 116, 34);
        comboBox.addItem("INFO");
        comboBox.addItem("WARN");
        comboBox.addItem("DEBUG");
        comboBox.addItem("ERROR");
        comboBox.addItem("FATAL");
        comboBox.addItem("TRACE");
        comboBox.addItem("ALL");
        comboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        frame.getContentPane().add(comboBox);

        //scrollable feature for the table so more logs can be viewed
        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(10, 100, 711, 295);
        frame.getContentPane().add(scrollPane_1);
        table = new JTable(new DefaultTableModel(new Object[]{"Time","Level", "Logger", "Thread","Message"},0));
        table.setFont(new Font("Arial", Font.PLAIN, 18));
        //model = new DefaultTableModel();
        scrollPane_1.setViewportView(table);

        // Button actions for fetch and download
        fetch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //get level
                //System.out.println(comboBox.getSelectedItem().toString());
                String level = comboBox.getSelectedItem().toString();
                //get limit
                //System.out.println(textField.getText());
                String limit = textField.getText();
                //make get request to /logs
                URIBuilder builder = new URIBuilder();
                builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(SERVICE_PATH)
                        .setParameter("level", level).setParameter("limit", limit);
                URI uri = null;
                try {
                    uri = builder.build();
                } catch (URISyntaxException ex) {
                    ex.printStackTrace();
                }
                // http://localhost:8080/resthome4logs
                HttpClient httpClient = HttpClientBuilder.create().build();

                //get request to server
                HttpGet getRequest = new HttpGet(uri);
                HttpResponse getResponse = null;

                String getContent = "";
                try {
                    getResponse = httpClient.execute(getRequest);
                    //get content in response
                    getContent = EntityUtils.toString(getResponse.getEntity());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                //System.out.println(getContent);
                //turn response into list of logs
                if(getContent.equals("[]")){
                    return;
                }
                JSONArray array = new JSONArray(getContent);
                List<JSONObject> list = new ArrayList<JSONObject>();
                for(int i = 0; i < array.length(); i++){
                    list.add(array.getJSONObject(i));
                    //System.out.println(list.get(i).toString());
                }

                //create new table
                model = new DefaultTableModel(new Object[]{"Time","Level", "Logger", "Thread","Message"},0);
                table = new JTable(model);
                table.setModel(model);
                table.setFont(new Font("Arial", Font.PLAIN, 12));
                JScrollPane scrollPane_1 = new JScrollPane(table);
                scrollPane_1.setBounds(10, 100, 711, 295);
                frame.getContentPane().add(scrollPane_1);
                scrollPane_1.setViewportView(table);


                //get information from logs and enter them into rows and columns
                for(int i = 0; i < list.size(); i++){
                    String time = list.get(i).get("timestamp").toString();
                    //System.out.println(time);
                    String levels = list.get(i).get("level").toString();
                    //System.out.println(levels);
                    String logger = list.get(i).get("logger").toString();
                    //System.out.println(logger);
                    String thread = list.get(i).get("thread").toString();
                    //System.out.println(thread);
                    String message = list.get(i).get("message").toString();
                    //System.out.println(message);
                    model.addRow(new Object[]{time , levels, logger, thread, message});
                }
            }
        });

        download.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //make request to /stats and set parameters
                URIBuilder builder = new URIBuilder();
                builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(SERVICE_STATS_PATH)
                        .setParameter("statsRequest", "notNull");
                URI uri = null;
                try {
                    uri = builder.build();
                } catch (URISyntaxException ex) {
                    ex.printStackTrace();
                }
                // http://localhost:8080/resthome4logs
                HttpClient httpClient = HttpClientBuilder.create().build();

                //get request to server
                HttpGet getRequest = new HttpGet(uri);
                HttpResponse getResponse = null;

                try {
                    getResponse = httpClient.execute(getRequest);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                //get file
                File downloadedFile = new File("stats.xlsx");
                try {
                    FileUtils.copyInputStreamToFile(getResponse.getEntity().getContent(), downloadedFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        getResponse.getEntity().getContent().close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                //download it
            }
        });

        sendLogs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doPostTest();
            }
        });
    }

    public void doPostTest() {
        Logger log = getLogger("logger-GenerateRandomLogs");
        log.addAppender(new Resthome4LogsAppender());
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
        log.info("The error was me all along.");
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LogMonitor window = new LogMonitor();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}