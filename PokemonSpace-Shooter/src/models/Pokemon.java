package models;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.Timer;

public class Pokemon implements Runnable{
	
	private double x, y, size, vx, vy, direction, speed;
	private boolean alive;
	private Thread thread;
	private Timer explosionTimer;
	private double width, height;
	private boolean stop;
	private int type;
	private Color color;
	private BufferedImage img;
	private ImageIcon explosionImg;
	Game game;
	private static final double INCREMENT = 1.03;
	
	public Pokemon(double y, double x, int speed, Game game) {
		this.speed = speed*2;
		this.game = game;
		initMovementComponents();
		alive = true;
		initPokemon(y,x);
		initImage();
		thread = new Thread(this);
		startMovement();
	}
	
	public void initExplosion() {
		 explosionTimer = new Timer(50, new ActionListener() {	
			 long initMillis = System.currentTimeMillis();
			@Override
			public void actionPerformed(ActionEvent e) {
				 if (System.currentTimeMillis() - initMillis > 1000 ) {
					 explosionImg = null;
					 explosionTimer.stop();
					 game.removePokemon(myInstance());
			        } 
			}
		});
		 explosionTimer.start();
	}
	
	private Pokemon myInstance() {
		return this;
	}
	
	private void initMovementComponents() {
		direction = ThreadLocalRandom.current().nextInt(1, 360);
		direction = Math.toRadians(direction);
		vx = speed*Math.cos(direction);
		vy = speed*Math.sin(direction);
		width = Toolkit.getDefaultToolkit().getScreenSize().getWidth(); 
		height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	}
	
	private void initImage() {
		explosionImg = new ImageIcon(getClass().getResource("/img/explosion.gif"));
		explosionImg.setImage(explosionImg.getImage().getScaledInstance((int)width/15, (int)height/15, Image.SCALE_DEFAULT));
		try {
			 img = resizeImage(ImageIO.read(getClass().getResourceAsStream(randomImgUrl())));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String randomImgUrl() {
		int raffleNumber = ThreadLocalRandom.current().nextInt(1,8);
		type = raffleNumber;
		return "/img/p" + raffleNumber + ".png";
	}
	
	private BufferedImage resizeImage(BufferedImage img) {
		BufferedImage auxImage =  new BufferedImage(getSize(),
                getSize(), img.getType());
		Graphics2D g2d = auxImage.createGraphics();
        g2d.drawImage(img, 0, 0, getSize(), getSize(), null);
        g2d.dispose();
        return auxImage;
	}
	
	@Override
	public void run() {
		while (alive) {
			move();
			synchronized (this) {				
				if(stop) {
					break;
				}
			}
			afterCrashStats();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void initPokemon(double y, double x) {
		this.x = x;
		this.y = y;
		size = (width+height)/40;
	}
	
	public void move() {
		x += vx;
		y += vy;
	}
	
	private void afterCrashStats() {
		if(crashingAbove()) {
			if(vx > 0) {
				direction = Math.PI/2 + (Math.atan(vx/vy));
			}
			else {
				direction = Math.PI/2 + (Math.atan(vx/vy));
			}
			speed *= INCREMENT;
			vx = (speed*Math.cos(direction));
			vy = (speed*Math.sin(direction));
		}
		if(crashingBelow()) {
			if(vx > 0) {
				direction = 3*Math.PI/2 + (Math.atan(vx/vy));
			}
			else {
				direction = 3*Math.PI/2 + (Math.atan(vx/vy));
			}
			speed *= INCREMENT;
			vx = (speed*Math.cos(direction));
			vy = (speed*Math.sin(direction));
		}
		if(crashingLeft()) {
			if(vy > 0) {
				direction = Math.PI/2 + (Math.atan(vx/vy));
			}
			else if(vy < 0){
				direction = 3*Math.PI/2 + (Math.atan(vx/vy));
			}
			else {
				direction += Math.PI;
			}
			speed *= INCREMENT;
			vx = (speed*Math.cos(direction));
			vy = (speed*Math.sin(direction));
		}
		if(crashingRight()) {
			if(vy > 0) {
				direction = Math.PI/2 + (Math.atan(vx/vy));
			}
			else if(vy < 0){
				direction = 3*Math.PI/2 + (Math.atan(vx/vy));
			}
			else {
				direction += Math.PI;
			}
			speed *= INCREMENT;
			vx = (speed*Math.cos(direction));
			vy = (speed*Math.sin(direction));
		}
	}
	
	public double getCenterX() {
		return x + size*0.5;
	}
	
	public double getCenterY() {
		return y + size*0.5;
	}
	
	public double crashingX() {
		return x + size*0.1;
	}
	
	public double crashingY() {
		return y + size*0.2;
	}
	
	public double crashingSize() {
		return size*0.8;
	}
	
	public void startMovement() {
		thread.start();
	}

	private boolean crashingAbove() {
		return y + size/2 <= 0; 
	}
	private boolean crashingBelow() {
		return y+size+1 >= height - height/4 - size/2; 
	}
	private boolean crashingLeft() {
		return x-1 <= 1; 
	}
	
	private boolean crashingRight() {
		return x+size+1 >= width; 
	}
	
	public synchronized void stop() {
		stop = true;
	}
	
	// ======================================== GETTERS && SETTERS ====================================

	public int getX() {
		return (int)x;
	}

	public int getY() {
		return (int)y;
	}

	public int getSize() {
		return (int)size;
	}
	
	public void die() {
		alive = false;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public Color getColor() {
		return color;
	}
	
	public BufferedImage getImg() {
		return img;
	}
	
	public int getType() {
		return type;
	}	
	
	public ImageIcon getExplosionImg() {
		return explosionImg;
	}
}
