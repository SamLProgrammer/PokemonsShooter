package views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import models.Bullet;
import models.Game;
import models.Pokemon;
import models.Power;
import models.Player;

public class PokemonsPanel extends JPanel{
	
	private Player player;
	private Power power;
	private Image backgroundImage, statsImage;
	private int seconds;
	private int minutes;
	private ImageIcon livesGif;
	private CopyOnWriteArrayList<Pokemon> pokemonsList;
	private CopyOnWriteArrayList<Bullet> bulletsList;
	private ArrayList<Image> imagesList;
	private int[] scoresList;
	private static final String MUNITION_STRING = "MUNITION: ";
	private static final String SPACESHIPS_STRING = "SPACESHIPS:";
	private static final String FONT_TYPE = "unispace";
	private static final String CURSOR_NAME = "cursor";
	private static final String CURSOR_IMG_URL = "/img/TargetIcon.png";
	private static final String LIVES_IMG_URL = "/img/Titan.gif";
	private static final String STATS_BACKGROUND_URL_IMG = "/img/Grid2.png";
	private static final String IMG_BACKGROUND_URL = "/img/background9.gif";
	
	public PokemonsPanel(MouseMotionListener gunListener, KeyListener kListener,
			             MouseListener mListener, Game game) {
		imagesList = new ArrayList<>();
		initImages();
		this.seconds = game.getSeconds();
		this.minutes = game.getMinutes();
		this.pokemonsList = game.getPokemonsList();
		this.player = game.getPlayer();
		this.bulletsList = game.getBulletsList();
		this.scoresList = game.getScoresList();
		int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		livesGif = new ImageIcon(getClass().getResource(LIVES_IMG_URL));
		livesGif.setImage(livesGif.getImage().getScaledInstance(width/15, height/15, Image.SCALE_DEFAULT));
		backgroundImage = new ImageIcon(getClass().getResource(IMG_BACKGROUND_URL)).getImage();
		statsImage = new ImageIcon(getClass().getResource(STATS_BACKGROUND_URL_IMG)).getImage();
		initCursor();
		addKeyListener(kListener);
		addMouseListener(mListener);
		addMouseMotionListener(gunListener);
		setOpaque(false);
		setFocusable(true);
	}
	
	private void initImages() {
		int size = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() +
				Toolkit.getDefaultToolkit().getScreenSize().getWidth())/50;
		imagesList.add(initImage(LIVES_IMG_URL,size));
		for (int i = 0; i < 7; i++) {
			imagesList.add(initImage("/img/p" + (i+1) + ".png",size));
		}
	}
	
	private Image initImage(String string, int size) {
		Image img = new ImageIcon(getClass().getResource(string)).getImage();
		Image newimg = img.getScaledInstance(size, size,  Image.SCALE_DEFAULT);
		return new ImageIcon(newimg).getImage();
	}
	
	public void initCursor() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image img = toolkit.getImage(getClass().getResource(CURSOR_IMG_URL));
		Point point = new Point(8, 8);
		Cursor cursor = toolkit.createCustomCursor(img, point,CURSOR_NAME);
		setCursor(cursor);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(backgroundImage, 0, 0,this.getWidth(),this.getHeight(), this);
		if(power != null) {
			power.getImg().paintIcon(this,g2,(int)power.getX(),(int)power.getY());
		}
		if(player.getShipImg() != null && player.getOp() != null) {
			g2.drawImage(player.getOp().filter(player.getShipImg(), null), (int)player.getX(), (int)player.getY(), null);
		}
		for (Pokemon pokemon : pokemonsList) {
			if(pokemon.isAlive()) {
				g2.setColor(pokemon.getColor());
				g2.drawImage(pokemon.getImg(), pokemon.getX(), pokemon.getY(), this);
			}
			else if(pokemon.getExplosionImg() != null){
				pokemon.getExplosionImg().paintIcon(this, g2, pokemon.getX(), pokemon.getY());
			}
		}
		if(bulletsList.size() > 0) {
			for (Bullet bullet : bulletsList) {
				if(bullet.isAlive()) {
					g2.setColor(Color.red);
//					g2.drawImage(bullet.getOp().filter(bullet.getImg(), null), bullet.getX()-bullet.getSize()/2, bullet.getY()-bullet.getSize()/2, null);
					g2.setStroke(new BasicStroke(2));
					g2.drawRect(bullet.getX(), bullet.getY(), bullet.getSize(), bullet.getSize());
				}
			}
		}
		paintStats(g2);
	}
	
	public void paintStats(Graphics2D g2) {
		int width = getParent().getWidth();
		int height = getParent().getHeight();
		paintBorderStats(g2, width, height);
		paintLives(g2, width, height);
		paintPokemonsScoreIcon(g2, width, height);
		paintTime(g2, width, height);
	}
	
	private void paintTime(Graphics2D g2, int width, int height) {
		g2.setFont(new Font(FONT_TYPE, Font.BOLD, (height + width)/30));
		if(seconds > 9) {
			g2.setColor(Color.decode("#84ff00"));
			g2.drawString("0" + minutes + ":" + seconds, width - width/4 + width/25, height - height/40);
		}
		else { 
			g2.setColor(Color.decode("#DE1F46"));
			g2.drawString("0" + minutes + ":0" + seconds, width - width/4 + width/25, height - height/40);
		}
	}
	
	private void paintLives(Graphics2D g2, int width, int height) {
		g2.setFont(new Font(FONT_TYPE,Font.BOLD, (height + width)/80));
		g2.setColor(Color.decode("#84ff00"));
		g2.drawString(SPACESHIPS_STRING, width/90, (height-height/5) + height/25);
		g2.drawString(MUNITION_STRING + player.getBullets(), width/80, height-height/20);
		for (int i = 0; i < player.getLives(); i++) {	
			livesGif.paintIcon(this, g2,
					width/90 + i*imagesList.get(0).getWidth(this) - width/100, (height-height/5) + height/35);
		}
	}
	
	private void paintPokemonsScoreIcon(Graphics2D g2, int width, int height) {
		g2.setFont(new Font(FONT_TYPE, Font.BOLD, (width+height)/50));
		for (int i = 1; i < imagesList.size(); i++) {
			g2.drawImage(imagesList.get(i),
					width/4 + i*(3*imagesList.get(i).getWidth(this)/4) + (i-1)*imagesList.get(i).getWidth(this),
					(height-height/5) + height/40, this);
			g2.drawString(String.valueOf(scoresList[i-1]),
					width/4 + i*(3*imagesList.get(i).getWidth(this)/4) + (i-1)*imagesList.get(i).getWidth(this) + imagesList.get(i).getWidth(this)/4,
					(height-height/40));
		}
	}
	
	private void paintBorderStats(Graphics2D g2, int width, int height) {
		int strokeWidth = width/100;
		int[] xPoints = {strokeWidth/2, width/8,width/8 + width/80, width - strokeWidth/2,
				 width-strokeWidth/2, strokeWidth/2};
		int[] yPoints = {height - height/5, height-height/5,(height - height/5) + height/80, (height - height/5) + height/80,
				 height-strokeWidth/2, height-strokeWidth/2};
		g2.setStroke(new BasicStroke(strokeWidth));
		g2.setColor(Color.black);
		int x = 0;
		while(x < width) {			
			g2.drawImage(statsImage, x, (height - height/5) + height/80, this);
			x += statsImage.getWidth(this);
		}
//		g2.fillPolygon(xPoints, yPoints, xPoints.length);
		g2.setColor(Color.decode("#1E638F"));
		g2.drawPolygon(xPoints, yPoints, xPoints.length);
		g2.setStroke(new BasicStroke(strokeWidth/3));
		g2.drawLine(0,height - height/10, width/4, height - height/10);
		g2.drawLine(width/4, (height - height/5) + height/80, width/4, height);
		g2.drawLine(width - width/4, (height - height/5) + height/80, width - width/4, height);
	}
	
	public void refresh(Game game) {
		this.seconds = game.getSeconds();
		this.minutes = game.getMinutes();
		this.player = game.getPlayer();
		this.power = game.getCurrentPower();
		this.pokemonsList = game.getPokemonsList();
		this.bulletsList = game.getBulletsList();
		this.scoresList = game.getScoresList();
		repaint();
	}
}