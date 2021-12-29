package presenter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import javax.swing.Timer;
import models.Game;
import views.GameWindow;
import views.InitDialog;

public class Presenter implements MouseListener, MouseMotionListener, ActionListener{

	private GameWindow gameWindow;
	private Game game;
	private Timer crashingCirclesTimer;
	private Timer refreshScreenTimer;
	private PlayerEngine playerMover;
	private InitDialog initDialog;
	
	public Presenter() {
		initDialog = new InitDialog(this);
		showInitDialog();
	}
	
	private void stopThreads() {
		int score = game.getScore();
		crashingCirclesTimer.stop();
		game.stopAllThreads();
		refreshScreenTimer.stop();
		initDialog.setFinal(score);
		initDialog.setVisible(true);
	}
	
	private void initMainThreads() {
		crashingCirclesTimer
		=  new Timer(1, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.verifyCrashing();
			};
		});
		refreshScreenTimer
		=  new Timer(60, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(game.getPlayer().getLives() == 0 || game.isTimeFinished()) {
					stopThreads();
				}
				gameWindow.refresh(game);
			};
		});
		crashingCirclesTimer.start();
		refreshScreenTimer.start();
		game.startCountDown();
	}
	
	private void initComponents(int pokemonsAmount, int speed) {
		game = new Game(pokemonsAmount,speed);
		playerMover = new PlayerEngine(game.getPlayer());
		gameWindow = new GameWindow(this, playerMover, this, game);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (Actions.valueOf(e.getActionCommand())) {
		case START_GAME_BUTTON:
			initComponents(initDialog.getSelectedAmount(),speedTraductor(initDialog.getSelectedSpeed()));
			initMainThreads();
			initDialog.setVisible(false);
			break;
		case EXIT_GAME_BTN:
			if(gameWindow != null) {
			gameWindow.dispatchEvent(new WindowEvent(gameWindow, WindowEvent.WINDOW_CLOSING));
			}
			initDialog.dispose();
			break;
		}
	}
	
	private int speedTraductor(String string) {
		int speed = 0;
		switch (string) {
		case "x1":
			speed = 1;
			break;
		case "x2":
			speed = 2;
			break;
		case "x3":
			speed = 3;
			break;
		case "x4":
			speed = 4;
			break;
		case "x5":
			speed = 5;
			break;
		}
		return speed;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	@Override
	public void mousePressed(MouseEvent e) {	
		game.addBullet(e.getX(), e.getY());
		gameWindow.refresh(game);
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {	
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		game.moveGun(e.getX(), e.getY());
		gameWindow.refresh(game);
	}
	
	private void showInitDialog() {
		initDialog.setVisible(true);
	}
}
