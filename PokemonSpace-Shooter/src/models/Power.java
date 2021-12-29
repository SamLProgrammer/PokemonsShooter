package models;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.ImageIcon;

public class Power {
	
	private double x;
	private double y;
	private double width;
	private double height;
	private double size;
	private ImageIcon img;
	private int type;
	private static final String FILE_EXTENSION = ".gif";
	private static final String INIT_LETTER_FILE_NAME = "/img/power";
	
	public Power() {
		initPosition();
		initImage();
	}

	private void initPosition() {
		width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		size = (width+height)/30;
		y = ThreadLocalRandom.current().nextDouble(0, height - height/4 - size);
		x = ThreadLocalRandom.current().nextDouble(size,width-size);
	}
	
	private void initImage() {
		img = new ImageIcon(getClass().getResource(randomImgUrl()));
		img.setImage(img.getImage().getScaledInstance((int)width/10, (int)height/10, Image.SCALE_DEFAULT));
	}
	
	private String randomImgUrl() {
		int raffleNumber = ThreadLocalRandom.current().nextInt(1,3);
		type = raffleNumber;
		return INIT_LETTER_FILE_NAME + raffleNumber +  FILE_EXTENSION;
	}
	
	public double crashingSize() {
		return size*0.9;
	}
	
	public double getCenterX() {
		return x + crashingSize()/2;
	}
	
	public double getCenterY() {
		return y + crashingSize()/2;
	}
	
	public int getSize() {
		return (int)size;
	}
	
	public int getType() {
		return type;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public ImageIcon getImg() {
		return img;
	}
}
