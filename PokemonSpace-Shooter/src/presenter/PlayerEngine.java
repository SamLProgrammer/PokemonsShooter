package presenter;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import models.Player;

public class PlayerEngine implements KeyListener, Runnable{

	private boolean upPressed, downPressed, leftPressed, rightPressed;
	private Player player;
	
	public PlayerEngine(Player player) {
		this.player = player;
		new Thread(this).start();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			upPressed = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			downPressed = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			leftPressed = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			rightPressed = true;
		}	
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			upPressed = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			downPressed = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			leftPressed = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			rightPressed = false;
		}	
	}

	@Override
	public void run() {
		while(true) {
			if(upPressed) {
				player.moveUp();
			}
			if(downPressed) {
				player.moveDown();
			}
			if(leftPressed) {
				player.moveLeft();
			}
			if(rightPressed) {
				player.moveRight();
			}
			
			try {
				Thread.sleep(60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
