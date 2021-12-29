package models;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Bullet implements Runnable{

	private double x, y, size, vx, vy, direction, speed;
	private boolean alive;
	private Thread thread;
	private int width, height;
	private Color color;
	private BufferedImage img;
	private AffineTransformOp op;
	private Game game;
	private static final String IMG_FLAME_PNG = "/img/flame.png";
	
	public Bullet(double x, double y, double xDistance, double yDistance, boolean inversed, Game game) {
		speed = 40;
		this.game = game;
		this.x = x;
		this.y = y;
		direction = Math.atan(yDistance/xDistance);
		if(inversed) {
			direction += Math.PI;
		}
		if(Math.atan(yDistance/xDistance) == -Math.PI/2 || Math.atan(yDistance/xDistance) == Math.PI/2) {
			direction += Math.PI;
		}
		vx = speed*Math.cos(direction);
		vy = speed*Math.sin(direction);
		width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(); 
		height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		alive = true;
		size = (width+height)/300;
		initImage();
		rotateImg(Math.toDegrees(direction+Math.PI/2));
		thread = new Thread(this);
		startMovement();
	}
	
	private void initImage() {
		try {
			img = resizeImage(ImageIO.read(getClass().getResourceAsStream(IMG_FLAME_PNG)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void rotateImg(double degrees) {
		double rotationRequired = Math.toRadians(degrees);
		double locationX = img.getWidth()/2;
		double locationY = img.getHeight()/2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
	}
	
	private BufferedImage resizeImage(BufferedImage img) {
		BufferedImage auxImage =  new BufferedImage(getSize()*5,
                getSize()*5, img.getType());
		Graphics2D g2d = auxImage.createGraphics();
        g2d.drawImage(img, 0, 0, getSize()*5, getSize()*5, null);
        g2d.dispose();
        return auxImage;
	}
	
	@Override
	public void run() {
		while (alive) {
			move();
			verifyCrashing();
			game.verifyShootCrashing(myInstance());
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Bullet myInstance() {
		return this;
	}
	
	public void startMovement() {
		thread.start();
	}
	
	public void move() {
		x += vx;
		y += vy;
	}
	
	public void verifyCrashing() {
		if(crashingAbove() || crashingBelow() || crashingLeft() || crashingRight()) {
			die();
		}
	}
	
	public double getCenterX() {
		return x + size/2;
	}
	
	public double getCenterY() {
		return y + size/2;
	}
	
	public boolean crashingAbove() {
		return y <= 0; 
	}
	
	public boolean crashingBelow() {
		return y >= height; 
	}
	
	public boolean crashingLeft() {
		return x <= 0; 
	}
	
	public boolean crashingRight() {
		return x >= width; 
	}
	
	public void die() {
		alive = false;
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
	
	public boolean isAlive() {
		return alive;
	}
	
	public Color getColor() {
		return color;
	}
	
	public BufferedImage getImg() {
		return img;
	}

	public AffineTransformOp getOp() {
		return op;
	}
	
}
