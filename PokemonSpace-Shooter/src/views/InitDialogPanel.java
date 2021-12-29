package views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class InitDialogPanel extends JPanel{

	private Image img;
	private Timer writeTimer;
	private int counter = 0;
	private int width = 0;
	private int height = 0;
	private int score = 0;
	private ArrayList<String> historyArray2;
	private ArrayList<String> historyArray;
	private boolean finalFlag;
	private static final String STRING0 = "Los pokemones se han escapado al espacio.";
	private static final String STRING1 = "Debes intentar atraparlos con tu rasho laser,";
	private static final String STRING2 = "tienes tan sólo un minuto terrestre";
	private static final String STRING3 = "debido a la distorción del espacio tiempo";
	private static final String STRING4 = "o una vez que salgas de la nebula ya será tarde...";
	private static final String STRING5 = "¡Evita chocar los pokemones!";
	private static final String RETRY_STRING = "REINTENTAR";
	
	public InitDialogPanel(int width, int height) {
		initImage();
		setOpaque(false);
		this.width = width;
		this.height = height;
		initHistoryArrays();
		writeTimer = new Timer(1700, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(counter <  historyArray.size()) {
					historyArray2.add(historyArray.get(counter));
					repaint();
				}
				else {
					writeTimer.stop();
				}
				counter += 1;
			}
		});
	}
	
	private void initHistoryArrays() {
		historyArray = new ArrayList<>();
		historyArray2 = new ArrayList<>();
		if(finalFlag) {
			historyArray.add(RETRY_STRING);
			historyArray.add("PUNTAJE: " + score);
		}
		else {
			historyArray.add(STRING0);
			historyArray.add(STRING1);
			historyArray.add(STRING2);
			historyArray.add(STRING3);
			historyArray.add(STRING4);
			historyArray.add(STRING5);
		}
	}
	
	public void paintHistoryText() {
		writeTimer.start();
	}
	
	private void initImage() {
		img = new ImageIcon(getClass().getResource("/img/niceSpace2.gif")).getImage();
	}

	
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		if(finalFlag) {
			g2.drawImage(img, 0, 0,this.getWidth(),this.getHeight(), this);
			g2.setFont(new Font("UNISPACE", Font.BOLD,(width+height)/20));
			g2.setColor(Color.decode("#C9D9F5"));
			for (int i = 0; i < historyArray2.size(); i++) {
				g2.drawString(historyArray2.get(i),
						width/2 - g2.getFontMetrics().stringWidth(historyArray2.get(i))/2,
						((height/5)*(i+1))-g2.getFontMetrics().getHeight()/2);
			}
		}
		else {
			g2.drawImage(img, 0, 0,this.getWidth(),this.getHeight(), this);
			g2.setFont(new Font("UNISPACE", Font.BOLD,(width+height)/60));
			g2.setColor(Color.decode("#C9D9F5"));
			for (int i = 0; i < historyArray2.size(); i++) {
				g2.drawString(historyArray2.get(i), width/80,
						(height/20) + (i*g2.getFontMetrics().getHeight()));			
			}
		}
	}
	
	public void setFinalFlag(int score) {
		finalFlag = true;
		this.score = score;
		initHistoryArrays();
		counter = 0;
		paintHistoryText();
	}
}