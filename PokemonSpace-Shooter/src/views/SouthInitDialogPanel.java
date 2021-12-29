package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import presenter.Actions;

public class SouthInitDialogPanel extends JPanel{
	
	JComboBox<Integer> amountBox;
	JComboBox<String> speedBox;
	int width;
	int height;

	public SouthInitDialogPanel(ActionListener listener, int width, int height) {
		this.width = width;
		this.height = height;
		setLayout(new GridLayout(3, 1));
		setBackground(Color.decode("#1c253c"));
		initComponents(listener);
	}
	
	private void initComponents(ActionListener listener) {
		setUIManager();
		JLabel titleLabel = new JLabel("<html><div style='text-align: center;'>" + "¿CUAL ES TU CAPACIDAD?" + "</div></html>", SwingConstants.CENTER);
		titleLabel.setBackground(Color.black);
		titleLabel.setOpaque(true);
		JLabel amountLabel = new JLabel("<html><div style='text-align: center;'>" + "CANTIDAD DE POKEMONS" + "</div></html>", SwingConstants.CENTER);
		JLabel speedLabel = new JLabel("<html><div style='text-align: center;'>" + "VELOCIDAD" + "</div></html>", SwingConstants.CENTER);
		JPanel containerPanel = new JPanel();
		containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.X_AXIS));
		containerPanel.setBackground(Color.black);
		containerPanel.setBorder(BorderFactory.createEmptyBorder(0,0,height/20,0));
		JPanel amountPanel = new JPanel(new GridLayout(2,1));
		amountPanel.setOpaque(false);
		JPanel speedPanel = new JPanel(new GridLayout(2,1));
		speedPanel.setOpaque(false);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1,2));
		buttonsPanel.setOpaque(false);
		DCButton startBtn = new DCButton("#17CD47");
		startBtn.setText("INICIAR");
		startBtn.setActionCommand(Actions.START_GAME_BUTTON.name());
		startBtn.addActionListener(listener);
		DCButton exitBtn = new DCButton("#EF2D2D");
		exitBtn.setText("SALIR");
		exitBtn.setActionCommand(Actions.EXIT_GAME_BTN.name());
		exitBtn.addActionListener(listener);
		
		JPanel amountBoxPanel = new JPanel();
		amountBoxPanel.setOpaque(false);
		amountBoxPanel.setLayout(new BorderLayout());
		JPanel speedBoxPanel = new JPanel();
		speedBoxPanel.setOpaque(false);
		speedBoxPanel.setLayout(new BorderLayout());
		
		amountBox = new JComboBox<>();
		amountBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		amountBox.getComponent(0).setBackground(Color.decode("#4B666B"));
		((JLabel)amountBox.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		speedBox = new JComboBox<>();
		speedBox.getComponent(0).setBackground(Color.decode("#4B666B"));
		((JLabel)speedBox.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		for (int i = 5; i < 16; i++) {
			amountBox.addItem(i);
		}
		for (int i = 1; i < 6; i++) {
			speedBox.addItem("x" + i);
		}
		
		amountBoxPanel.add(amountBox);
		amountBoxPanel.setBorder(BorderFactory.createEmptyBorder(0, width/5 + (width+height)/80, 0, width/5 + (width+height)/80));
		speedBoxPanel.add(speedBox);
		speedBoxPanel.setBorder(BorderFactory.createEmptyBorder(0, width/5 + (width+height)/80, 0, width/5 + (width+height)/80));
		
		amountPanel.add(amountLabel);
		amountPanel.add(amountBoxPanel);
		
		speedPanel.add(speedLabel);
		speedPanel.add(speedBoxPanel);
		
		containerPanel.add(amountPanel);
		containerPanel.add(speedPanel);
		
		buttonsPanel.add(startBtn);
		buttonsPanel.add(exitBtn);
		
		add(titleLabel);
		add(containerPanel);
		add(buttonsPanel);
	}	
	
	private void setUIManager() {
		UIManager.put("Label.font", new Font("UNISPACE", Font.BOLD, 15));
		UIManager.put("Label.foreground", Color.decode("#8F969B"));
		UIManager.put("ComboBox.background", Color.decode("#DED8D5"));
		UIManager.put("ComboBox.selectionBackground", Color.decode("#4B666B"));
		UIManager.put("ComboBox.font", new Font("UNISPACE", Font.BOLD, (width+height)/80));
		UIManager.put("ComboBox.foreground", Color.decode("#262B2C"));
		UIManager.put("Label.foreground", Color.decode("#C9D9F5"));
	}
	
	public int getSelectedAmount() {
		return (int)amountBox.getSelectedItem();
	}
	
	public String getSelectedSpeed() {
		return (String)speedBox.getSelectedItem();
	}
}
