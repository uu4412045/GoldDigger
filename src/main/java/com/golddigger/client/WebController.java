/**
 * 
 */
package com.golddigger.client;

import java.io.IOException;
import java.net.MalformedURLException;

import org.xml.sax.SAXException;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

/**
 * A HTTP client that provides access to the main commands to control a digger.
 * 
 * @author Brett Wandel
 */
public class WebController {
    private static WebConversation wc = new WebConversation();
    private String secretName;
    private int port;
    private final int serverSleep;
    
    public WebController(String secretName, int port, int serverSleep) {
        super();
        this.secretName = secretName;
        this.port = port;
        this.serverSleep = serverSleep;
    }
    public WebResponse view() {
        return call("http://localhost:" + port + "/golddigger/digger/" + secretName + "/view", serverSleep);
    }
    public WebResponse score() {
        return call("http://localhost:" + port + "/golddigger/digger/" + secretName + "/score", serverSleep);
    }

    public WebResponse carrying() {
        return call("http://localhost:" + port + "/golddigger/digger/" + secretName + "/carrying", serverSleep);
    }

    public WebResponse drop() {
        return call("http://localhost:" + port + "/golddigger/digger/" + secretName + "/drop", serverSleep);
    }

    public WebResponse nextField() {
        return call("http://localhost:" + port + "/golddigger/digger/" + secretName + "/next", serverSleep);
    }

    public WebResponse grab() {
        return call("http://localhost:" + port + "/golddigger/digger/" + secretName + "/grab", serverSleep);
    }

    public WebResponse moveWest() {
        return call("http://localhost:" + port + "/golddigger/digger/" + secretName + "/move/west", serverSleep);
    }
    public static WebResponse call(String url, int sleep) {
        try {
            WebRequest webRequest = new GetMethodWebRequest(url);
            webRequest.setHeaderField("sleep", sleep + "");
            return wc.getResponse(webRequest);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    public WebResponse moveNorth() {
        return call("http://localhost:" + port + "/golddigger/digger/" + secretName + "/move/north", serverSleep);
    }

    public WebResponse moveSouth() {
        return call("http://localhost:" + port + "/golddigger/digger/" + secretName + "/move/south", serverSleep);
    }

    public WebResponse moveEast() {
        return call("http://localhost:" + port + "/golddigger/digger/" + secretName + "/move/east", serverSleep);
    }
    public WebResponse moveNorthEast() {
        return call("http://localhost:" + port + "/golddigger/digger/" + secretName + "/move/north_east", serverSleep);
    }

    public WebResponse moveSouthEast() {
        return call("http://localhost:" + port + "/golddigger/digger/" + secretName + "/move/south_east", serverSleep);
    }

    public WebResponse moveNorthWest() {
        return call("http://localhost:" + port + "/golddigger/digger/" + secretName + "/move/north_west", serverSleep);
    }
    
    public WebResponse moveSouthWest() {
        return call("http://localhost:" + port + "/golddigger/digger/" + secretName + "/move/south_west", serverSleep);
    }
    
	public WebResponse cannonBuy() {
        return call("http://localhost:" + port + "/golddigger/digger/" + secretName + "/cannon/buy", serverSleep);
	}
	
	public WebResponse cannonShoot(int lat, int lng) {
        return call("http://localhost:" + port + "/golddigger/digger/" + secretName + "/cannon/shoot/"+lat+"/"+lng, serverSleep);
	}

}