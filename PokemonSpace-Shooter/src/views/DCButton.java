package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

import javax.swing.JButton;

public class DCButton extends JButton{
	
	public DCButton(String string) {
		setForeground(Color.decode(string));
		setLayout(new BorderLayout());
//		setOpaque(false);
		setBackground(Color.decode("#2F3939"));
//		setContentAreaFilled(false);
		setFont(new Font("UNISPACE", Font.BOLD, 20));
		setFocusPainted(false);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
}
