package views;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;

public class InitDialog extends JDialog{
	
	private InitDialogPanel initDialogPanel;
	private SouthInitDialogPanel southPanel;
	private static final String URL_ICON = "/img/TargetIcon.png";
	
	public InitDialog(ActionListener listener) {
		setLayout(new BorderLayout());
		locate();
		setIconImage(new ImageIcon(getClass().getResource(URL_ICON)).getImage());
		initComponents(listener, getWidth(), getHeight());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	private void initComponents(ActionListener listener, int width, int height) {
		initDialogPanel = new InitDialogPanel(width, height);
		southPanel = new SouthInitDialogPanel(listener,width,height);
		add(initDialogPanel,BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		initDialogPanel.paintHistoryText();
	}
	
	private void locate() {
		int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		setSize(2*width/3,height - height/4);
		setLocation(width/2 - getWidth()/2, height/2 - getHeight()/2);
	}
	
	public int getSelectedAmount() {
		return southPanel.getSelectedAmount();
	}
	
	public String getSelectedSpeed() {
		return southPanel.getSelectedSpeed();
	}

	public void setFinal(int score) {
		initDialogPanel.setFinalFlag(score);
	}
	
}
