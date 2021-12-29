package views;

import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import models.Game;

public class GameWindow extends JFrame{

	private PokemonsPanel pokemonsPanel;
	private static final String URL_ICON = "/img/TargetIcon.png";
	
	public GameWindow(MouseMotionListener gunListener, KeyListener kListener,MouseListener mListener,
		Game game) {
		locate();
		setIconImage(new ImageIcon(getClass().getResource(URL_ICON)).getImage());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		initComponents(gunListener, kListener, mListener, game);
		setVisible(true);
	}
	
	private void initComponents(MouseMotionListener gunListener, KeyListener kListener, MouseListener mListener,
			Game game) {
		pokemonsPanel = new PokemonsPanel(gunListener, kListener, mListener, game);
		add(pokemonsPanel);
	}
	
	public void refresh(Game game) {
		pokemonsPanel.refresh(game);
	}
	
	private void locate() {
		int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		setSize(width/2, height/2);
		setLocation(width/2 - getWidth()/2, height - 3*getHeight()/2);
		setExtendedState(MAXIMIZED_BOTH);
	}
}