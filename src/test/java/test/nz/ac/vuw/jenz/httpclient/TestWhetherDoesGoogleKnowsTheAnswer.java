package test.nz.ac.vuw.jenz.httpclient;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test whether google knows the answer to life the universe and everything.
 * If it fails, check console for timeouts and proxy issues (this does not work behind proxies requiring authentication).
 * @author jens dietrich
 */
public class TestWhetherDoesGoogleKnowsTheAnswer {

    @Test
    public void test() throws Exception {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("localhost").setPort(8080).setPath("/resthome4logs/logs")
                .setParameter("q", "debug, it works");
        URI uri = builder.build();
        // http://localhost:8080/resthome4logs
        // create and execute the request
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(uri);
        //request.set the parameters, level, limit, add (post)
        HttpResponse response = httpClient.execute(request);
        //test response values, response.get blah, gt returned values from the server.
        // this string is the unparsed web page (=html source code)
        String content = EntityUtils.toString(response.getEntity());

        // check whether the web page contains the expected answer
        assertEquals(response.getStatusLine().getStatusCode(),200);
    }

}
