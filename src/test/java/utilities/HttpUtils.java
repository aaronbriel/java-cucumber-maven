package utilities;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Assert;
import java.util.logging.Logger;

/**
 * Created by aaronbriel on 12/20/16.
 */
public class HttpUtils {

    private static Logger LOGGER = Logger.getLogger(HttpUtils.class.getName());


    /**
     * Executes an HTTP Get request to the URL and returns the status code
     *
     * @param url The element to scroll into view on the page
     * @return int  The status code of the response
     * @throws Exception General exception to catch multiple
     */
    public static int getStatusCode(String url) throws Exception {

        int status;
        //Trust self-signed certificate
        //NOTE: Cookie rejected warnings are thrown with this. Leveraging
        // Config.getBaseUrl(Config.HTTPS) for urls that route to https did not resolve them
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                builder.build());
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

        try {
            HttpGet httpget = new HttpGet(url);
            LOGGER.info("Executing request " + httpget.getRequestLine());
            HttpResponse response = httpclient.execute(httpget);
            status = response.getStatusLine().getStatusCode();
        } finally {
            httpclient.close();
        }

        return status;
    }

    /**
     * Asserts status code
     *
     * @param expectedStatus Expected status code
     * @param actualStatus   Actual status code
     */
    public static void assertStatusCode(int expectedStatus, int actualStatus) {
        Assert.assertEquals("FAIL: Expected status: \"" + expectedStatus + "\", Actual status:\""
                + actualStatus + "\"!", expectedStatus, actualStatus);
        LOGGER.info("Confirmed that status code was '" + expectedStatus + "'.");
    }

    /**
     * Asserts status code is below 400
     *
     * @param status    The status to check
     */
    public static void assertStatusCodeBelow400(int status) {
        if (status < 400) {
            LOGGER.info("Confirmed that status code was below 400 (status: " + status + ".");
        } else {
            Assert.fail("FAIL: Status code was NOT below 400, status was " + status + "!");
        }
    }
}