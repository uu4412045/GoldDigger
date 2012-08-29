package com.golddigger.view;

import static org.junit.Assert.*;

import javax.swing.JLabel;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.golddigger.GenericServer;
import com.golddigger.GenericServer.Mode;
import com.golddigger.client.TestingClient;
import com.golddigger.model.Direction;
import com.golddigger.model.Player;
import com.golddigger.templates.TestGameTemplate;

public class GuiTest {
	private GenericServer server;
	private TestingClient client;
	private Player player;
	
	@Before
	public void before(){
		server = new GenericServer(Mode.GUI);
		server.addTemplate(new TestGameTemplate("www\nwbw\nwww"));
		server.addTemplate(new TestGameTemplate("wwww\nwb9w\nwwww"));
		server.addPlayer("test", "secret");
		player = server.getMain().getPlayer("test");
		assertNotNull(player);
		
		client = new TestingClient("test", "http://localhost:8066");
	}
	
	@After
	public void after(){
		server.stop();
	}
	
	@Test
	public void test() throws InterruptedException {
		Thread.sleep(2000);

		ViewPanel view = server.getMainGUI().getViewFor(player);
		assertNotNull(view);
		
		JLabel score = (JLabel) view.getLeftComponent();
		FieldView field = (FieldView) view.getRightComponent();
		
		assertTrue(score.isShowing());
		assertTrue(field.isShowing());

		client.next();
		Thread.sleep(2000);
		assertNotSame(field, view.getRightComponent());
		
		client.move(Direction.EAST);
		client.grab();
		Thread.sleep(2000);
		assertEquals("test: 0 [3]", score.getText());
		client.move(Direction.WEST);
		client.drop();
		Thread.sleep(2000);
		assertEquals("test: 3 [0]", score.getText());
	}

}
