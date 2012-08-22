/**
 * 
 */
package com.golddigger.client;

import com.meterware.httpunit.WebResponse;

/**
 * A Simple HTTP client that allows you to access the admin commands.
 * @author Brett Wandel
 */
public class AdminWebController {
    private final int port;
    
    /**
     * New Admin Controller talking to a server.
     * @param port The port that the server is running on
     */
    public AdminWebController(int port) {
        super();
        this.port = port;
    }

    /**
     * Add A player to the server
     * @param name The name of the Player
     * @param secretName The password of the Player
     * @return the WebResponse from the server, "OK" if successful, "FAILED" if not.
     */
    public WebResponse add(String name, String secretName) {
        return WebController.call("http://localhost:" + port + "/golddigger/admin/ccret/add/" + name + "/" + secretName, 0);
    }
    
    /**
     * List all the diggers on the server
     * @return The web response containing each players name and password
     */
    public WebResponse listdiggers() {
        return WebController.call("http://localhost:8066/golddigger/admin/ccret/listdiggers", 0);
    }
    
}