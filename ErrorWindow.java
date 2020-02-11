import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ErrorWindow extends JFrame {
	public ErrorWindow(String error) {
		super("Error");
		setLayout(new FlowLayout());
		JLabel errorMessage = new JLabel(error);
		add(errorMessage);
		errorMessage.setFont(new Font(errorMessage.getName(), Font.BOLD,14));
		this.setSize(700, 200);
		getContentPane().setBackground(Color.red);
		
	}
}
