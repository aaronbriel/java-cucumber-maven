package utilities;

import com.saucelabs.saucerest.SauceREST;
import common.Config;
import org.json.JSONException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aaronbriel on 12/2/16.
 */
public class SauceUtils {

    private static SauceREST sauceRESTClient;

    private static SauceREST getSauceRestClient(String username, String accessKey){
        if (sauceRESTClient == null) {
            sauceRESTClient = new SauceREST(username, accessKey);
        }
        return sauceRESTClient;
    }

    /**
     * Updates the results of a test based on the name and status at end of the test
     * Session ID is passed in as an identifier
     *
     * @param testName   Name of current test
     * @param sessionId  ID of current session
     * @param testStatus Boolean status of the executed test
     * @throws JSONException JSON exception
     * @throws IOException Input/Output exception
     */
    public static void updateResults(String testName, String sessionId, boolean testStatus)
            throws JSONException, IOException {
        String message = String.format("SauceOnDemandSessionID=%1$s job-name=%2$s",
                sessionId, testName);
        System.out.println(message);
        SauceREST client = getSauceRestClient(Config.getSauceUsername(), Config.getSauceAccessKey());
        Map<String, Object> updates = new HashMap<String, Object>();
        updates.put("passed", testStatus);
        client.updateJobInfo(sessionId, updates);
    }

}
