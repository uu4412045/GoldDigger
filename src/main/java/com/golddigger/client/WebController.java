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
    private String host;
    private final int serverSleep;
    
    public WebController(String secretName, String host, int serverSleep) {
        super();
        this.secretName = secretName;
        this.host = host;
        this.serverSleep = serverSleep;
    }
    
    public WebResponse view() {
        return call("view");
    }
    public WebResponse score() {
        return call("score");
    }

    public WebResponse carrying() {
        return call("carrying");
    }

    public WebResponse drop() {
        return call("drop");
    }

    public WebResponse nextField() {
        return call("next");
    }

    public WebResponse grab() {
        return call("grab");
    }

    public WebResponse moveWest() {
        return call("move","west");
    }

    public WebResponse moveNorth() {
        return call("move","north");
    }

    public WebResponse moveSouth() {
        return call("move","south");
    }

    public WebResponse moveEast() {
        return call("move","east");
    }
    public WebResponse moveNorthEast() {
        return call("move","north_east");
    }

    public WebResponse moveSouthEast() {
        return call("move","south_east");
    }

    public WebResponse moveNorthWest() {
        return call("move","north_west");
    }
    
    public WebResponse moveSouthWest() {
        return call("move","south_west");
    }
    
	public WebResponse cannonBuy() {
        return call("cannon","buy");
	}
	
	public WebResponse cannonShoot(int lat, int lng) {
        return call("cannon","shoot",lat+"",+lng+"");
	}
	
	private WebResponse call(String action, String... extras){
		String url = "http://" + host + "/golddigger/digger/" + secretName + "/" + action;
		for (String extra : extras){
			url += "/"+extra;
		}
		return call(url, serverSleep);
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
}