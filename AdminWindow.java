import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Date;
import javax.imageio.*;
import java.util.TimerTask;
import java.util.Timer;

class AdminWindow extends JFrame {
	JButton campaigns;
	JButton vouchers;
	JLabel message;
	CampaignWindow campaignWindow;
	VMS myVMS = VMS.getInstance();
	
	public AdminWindow() {
		super("Admin Menu");
		setSize(1000,800);
		getContentPane().setBackground(Color.cyan);
		campaigns = new JButton("Administreaza campaniile");
		campaigns.setFont(new Font("SansSerif",Font.BOLD, 14));
		campaigns.setBackground(Color.yellow);
		vouchers = new JButton("Generare multipla vouchere");
		vouchers.setFont(new Font("SansSerif",Font.BOLD, 14));
		vouchers.setBackground(Color.pink);
		message = new JLabel("Bun venit la Voucher Management Service ~~~ pagina de administrare");
		message.setFont(new Font("Courier",Font.ITALIC,15));
		try {
			setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("admin.png")))));
			setLayout(new FlowLayout());
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		add(message);
		add(campaigns);
		add(vouchers);
		
		//afiseaza pagina de administrare a campaniilor
		campaigns.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				dispose();
				campaignWindow = CampaignWindow.getInstance();
				campaignWindow.show();
			}
		});
		
		//afiseaza pagina de generare multipla de vouchere
		vouchers.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				MultipleGenerationVouchers mVouchers = new MultipleGenerationVouchers("Generare multipla vouchere");;
				mVouchers.show();
			}
		});
		
		JButton updateStatus = new JButton("Activeza optiunea de actualizare automata a statusului campaniilor");
		updateStatus.setFont(new Font("SansSerif",Font.BOLD, 14));
		updateStatus.setBackground(Color.magenta);
		add(updateStatus);
		
		//se apeleaza verificare si editarea periodica a statusului campaniilor
		updateStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TimerTask obj = new UpdateStatus();
				Timer timer = new Timer(true);
				Date start = new Date();
				timer.scheduleAtFixedRate(obj, start, 10*1000);
				updateStatus.setEnabled(false);
			}
		});
	}
}
