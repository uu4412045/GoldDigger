package com.golddigger.client;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

/** A Simple GoldDigger GUI client to allow easy interaction with a running
 *   GoldDigger server.
 */
public class SwingClient extends JFrame {
	private static final long serialVersionUID = -8050979025970084790L;
	/**
	 * The user's "secret" to be used in the URL.
	 */
	public static JTextField secret;
	/**
	 * The IP Address of the target GoldDigger Server.
	 */
	public static JTextField ipAddress;
	/**
	 * The port of the target GoldDigger Server.
	 */
	public static JTextField port;
	
	/**
	 * The output from the HTTP Response after a call to the GoldDigger Server.
	 */
	public static JTextArea result;
	
	/**
	 * Used to talk to the GoldDigger Server.
	 */
    private static WebConversation wc = new WebConversation();
    
    /**
     * used to hold the settings for interacting with the golddigger server
     */
	JPanel settingsPanel = new JPanel(new FlowLayout());
	
	/**
	 * the "general" tab for the different basic command buttons
	 */
	JPanel general = new JPanel(new FlowLayout());
	
	/**
	 * the tab that holds the Square Tile Movement command buttons
	 */
	JPanel move4 = new JPanel(new FlowLayout());

	/**
	 * the tab that holds the Hex Tile Movement command buttons
	 */
	JPanel move6 = new JPanel(new FlowLayout());
	
	/**
	 * the tab that holds the Admin command buttons
	 */
	JPanel admin = new JPanel(new FlowLayout());
	
	/**
	 * The JTabPanel that contains the command buttons.
	 */
	JTabbedPane commands = new JTabbedPane();
	
	/**
	 * holds the JTextArea that displays the output from the interaction with the server.
	 */
	JPanel logs = new JPanel(new FlowLayout());
	
	public SwingClient(){
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		secret = new JTextField(12);
		settingsPanel.add(new JLabel("User:"));
		settingsPanel.add(secret);
		ipAddress = new JTextField("localhost",20);
		settingsPanel.add(new JLabel("IP Address:"));
		settingsPanel.add(ipAddress);
		port = new JTextField("8066",5);
		settingsPanel.add(new JLabel("Port:"));
		settingsPanel.add(port);
		
		result = new JTextArea(10,50);
		logs.add(new JScrollPane(result));
		
		add(settingsPanel);
		add(commands);
		add(logs);
		initCommands();
		pack();
	}
	
	/**
	 * Used to build and add the command buttons the their tabs.
	 */
	public void initCommands(){
		JPanel general = new JPanel();
		general.add(new Button("View", "/view"));
		general.add(new Button("Grab", "/grab"));
		general.add(new Button("Drop", "/drop"));
		general.add(new Button("Next Map","/next"));
		general.add(new Button("Carrying","/carrying"));
		general.add(new Button("Score","/score"));
		commands.add(general, "General");
		
		JPanel move4 = new JPanel();
		move4.add(new Button("North", "/move/north", true));
		move4.add(new Button("South", "/move/south", true));
		move4.add(new Button("East", "/move/east", true));
		move4.add(new Button("West", "/move/west", true));
		commands.add(move4, "Square Tile Movement");
		
		JPanel move6 = new JPanel();
		move6.add(new Button("North", "/move/north", true));
		move6.add(new Button("South", "/move/south", true));
		move6.add(new Button("North East", "/move/north_east", true));
		move6.add(new Button("South East", "/move/south_east", true));
		move6.add(new Button("North West", "/move/north_west", true));
		move6.add(new Button("South West", "/move/south_west", true));
		commands.add(move6, "Hex Tile Movement");
	}
	
	/**
	 * Very basic client using a Swing based GUI.
	 */
	public static void main(String[] args) {
		new SwingClient();
	}
	
	/**
	 * Calls the URL, and returns the response information. Used to interact with the golddigger server.
	 */
	public static String call(String url){
		WebResponse r;
		String result;
		try {
			r = wc.getResponse(url);
			result = "Status:"+r.getResponseCode()+" - "+r.getResponseMessage() + "\n";
			result += r.getText();
		} catch (Exception e){
			result = "Error:\n"+e.getMessage();
		}
		return result;
	}

	/**
	 * A simple JButton extension used to create all the "command buttons" on the interface
	 */
	private class Button extends JButton{
		private static final long serialVersionUID = 4423907580745435372L;
		
		/**
		 * @param name The text to display on the button.
		 * @param cmd The command to run when pressed.
		 * @param timed adds the execution time to the result if true.
		 */
		public Button(String name, final String cmd, boolean timed){
			super(name);
			if (timed){
				this.addActionListener(new ActionListener() {	
					@Override
					public void actionPerformed(ActionEvent e) {
						String output, url = "http://"+ipAddress.getText()+":"+port.getText();
						url += "/golddigger/digger/"+secret.getText();
						url += cmd;
						long start = System.currentTimeMillis();
						output = call(url);
						long stop = System.currentTimeMillis();
						output += "\nReply Time: "+(stop-start)+"ms";
						result.setText(output);
					}
				});
			} else {
				this.addActionListener(new ActionListener() {	
					@Override
					public void actionPerformed(ActionEvent e) {
						String url = "http://"+ipAddress.getText()+":"+port.getText();
						url += "/golddigger/digger/"+secret.getText() + cmd;
						result.setText(call(url));
					}
				});
			}
		}
		
		/**
		 * @param name The text to display on the button.
		 * @param cmd The command to run when pressed.
		 */
		public Button(String name, String cmd){
			this(name, cmd, false);
		}
	}
}
