package models;

import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import javax.imageio.ImageIO;
import javax.swing.Timer;

public class Player {
	
	private double x;
	private double y;
	private double speed;
	private double size;
	private int width;
	private int height;
	private int lives;
	private int bullets;
	private boolean immune;
	private BufferedImage shipImg;
	private BufferedImage transparentImg;
	private Timer immuneTimer;
	private AffineTransformOp op;
	private final static String SPACE_SHIP_IMG = "/img/Titan.png";
	private final static String TRANSPARENT_IMG = "/img/transparent.png";
	
	public AffineTransformOp getOp() {
		return op;
	}

	public Player() {
		lives = 2;
		bullets = 25;
		initMovementComponents();
		initImages();
	}
	
	private void initMovementComponents() {
		speed = 20;
		width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(); 
		height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		size = (width+height)/35;
		x = height/2;
		y = width/4;
	}
	
	private void raffleImage() {
		int raffle = ThreadLocalRandom.current().nextInt(0,2);
		if(raffle == 0) {
			initImages();
		}
		else {
			shipImg = transparentImg;
		}
	}
	
	private void initImages() {
		try {
			 shipImg = resizeImage(ImageIO.read(getClass().getResourceAsStream(SPACE_SHIP_IMG)));
			 transparentImg = resizeImage(ImageIO.read(getClass().getResourceAsStream(TRANSPARENT_IMG)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void rotateGun(double degrees, boolean inversed) {
		double rotationRequired = degrees + Math.PI/2;
		if(inversed) {
			rotationRequired += Math.PI;
		}
		if(degrees == - Math.PI/2) {
			rotationRequired = Math.PI;
		}
		if(degrees == Math.PI/2) {
			rotationRequired = 0;
		}
		double locationX = shipImg.getWidth() / 2;
		double locationY = shipImg.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
	}
	
	private BufferedImage resizeImage(BufferedImage img) {
		BufferedImage auxImage =  new BufferedImage(getSize(),
                getSize(), img.getType());
		Graphics2D g2d = auxImage.createGraphics();
        g2d.drawImage(img, 0, 0, getSize(), getSize(), null);
        g2d.dispose();
        return auxImage;
	}
	
	public void moveUp() {
		if(y - speed >= 0){
			y -= speed;
		}
	}
	
	public void moveDown() {
		if(y + speed  + crashingSize()*2 <= height - height/5) {
			y += speed;
		}
	}
	
	public void moveLeft() {
		if( x - speed >= 0) {
			x -= speed;
		}
	}
	
	public void moveRight() {
		if(x + crashingSize() + speed <= width) {
			x += speed;
		}
	}
	
	public void startImmuneTimer() {
		 immuneTimer = new Timer(50, new ActionListener() {	
			 long initMillis = System.currentTimeMillis();
			@Override
			public void actionPerformed(ActionEvent e) {
				 if (System.currentTimeMillis() - initMillis > 1500 ) {
					 initImages();
					 setImmune(false);
					 immuneTimer.stop();
			        } else {
		                 raffleImage();
		              }
			}
		});
		immuneTimer.start();
	}
	
	public void die() {
		lives -= 1;
		setImmune(true);
	}
	
	public void spendBullet() {
		bullets -= 1 ;
	}
	
	public double crashingX() {
		return x;
	}
	
	public double crashingY() {
		return y;
	}
	
	public double crashingSize() {
		return size*0.9;
	}
	
	public void addLive() {
			lives += 1;
	}
	
	public void addBullets() {
		bullets += 25;
	}
	
	//========================================== GETTERS && SETTERS ==========================================

	public int getX() {
		return (int)x;
	}

	public int getY() {
		return (int)y;
	}
	
	public int getSize() { 
		return (int)size;
	}
	
	public boolean isImmune() {
		return immune;
	}	
	
	public void setImmune(boolean flag) {
		immune = flag;
	}

	public BufferedImage getShipImg() {
		return shipImg;
	}
	
	public int getLives() {
		return lives;
	}
	
	public int getBullets() { 
		return bullets;
	}
	
	public double getCenterX() {
		return x + size/2;
	}
	
	public double getCenterY() {
		return y + size/2;
	}
}
