import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserWindow extends JFrame {
	
	User currentUser;
	VMS myVMS = VMS.getInstance();
	
	public UserWindow(String title) {
		super(title);
		setLayout(new FlowLayout());
		getContentPane().setBackground(Color.orange);
		this.setSize(1000, 400);
	}
	
	public void addComponents() {
		String name = "";
		if(currentUser!=null) {
			name = currentUser.name;
		}
		
		Font f = new Font("",Font.ITALIC,25);
		JLabel message = new JLabel("~~~Salut, " +name +" ! "+"Bun venit la Voucher Management Service!~~~");
		message.setFont(f);
		JPanel options = new JPanel();
		options.setLayout(new BoxLayout(options, BoxLayout.Y_AXIS));
		Font g = new Font("",Font.BOLD,15);
		JButton option1 = new JButton("Vizualizare campanii");
		option1.setFont(g);
		option1.setBackground(Color.yellow);
		JButton option2 = new JButton("Vizualizare vouchere personale");
		option2.setFont(g);
		option2.setBackground(Color.cyan);
		JButton notificationButton = new JButton("Vezi lista de notificari primite");
		notificationButton.setFont(g);
		notificationButton.setBackground(Color.LIGHT_GRAY);
		add(message);
		option1.setAlignmentX(Component.CENTER_ALIGNMENT);
		option2.setAlignmentX(Component.CENTER_ALIGNMENT);
		notificationButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		options.add(option1);
		options.add(option2);
		options.add(notificationButton);
		options.setBackground(Color.orange);
		add(options);
	    
		//pagina de vizualizare a campaniilor din colectie
		option1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewCampaigns v = new ViewCampaigns("Campaniile colectiei");
				v.show();
			}
		});
		
		option2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewVouchers v = new ViewVouchers(currentUser);
				v.show();
			}
		});
		
		notificationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewNotifications not = new ViewNotifications(currentUser);
				not.show();
			}
		});
	}
	
	
}
