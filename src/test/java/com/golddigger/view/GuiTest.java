package com.golddigger.view;

import static org.junit.Assert.*;

import javax.swing.JLabel;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.golddigger.GenericServer;
import com.golddigger.model.Player;
import com.golddigger.templates.TestGameTemplate;

public class GuiTest {
	private GenericServer server;
	private Player player;
	
	@Before
	public void before(){
		server = new GenericServer(false);
		server.addTemplate(new TestGameTemplate("www\nwbw\nwww"));
		server.addPlayer("test", "secret");
		player = server.getMain().getPlayer("test");
		assertNotNull(player);
	}
	
	@After
	public void after(){
		server.stop();
	}
	
	@Ignore
	@Test
	public void test() {
		//TODO: Implemen GUI Tests properly
		ViewPanel view = server.getMainGUI().getViewFor(player);
		assertNotNull(view);
		
		JLabel score = (JLabel) view.getLeftComponent();
		FieldView field = (FieldView) view.getRightComponent();
		
		assertTrue(score.isShowing());
		assertTrue(field.isShowing());
	}

}
