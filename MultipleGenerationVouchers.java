import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.imageio.*;
import javax.swing.table.*;
import java.util.*;
import java.io.*;


class MultipleGenerationVouchers extends JFrame {
	
	VMS myVMS = VMS.getInstance();
	JScrollPane  scrollPane;
	JTable vouchers = new JTable();
	Object[] columns = {"ID","Code","CampaignId","Type","Email"};
	DefaultTableModel model = new DefaultTableModel();
	UserVoucherMap newVouchers;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	Campaign campaign;
	
	public void generateVouchers() {
		int i;
		final String path = "emails.txt";
		try {
			RandomAccessFile file = new RandomAccessFile(path,"r");
			int nrOfEmails = Integer.parseInt(file.readLine());
			String voucher;
			while((voucher = file.readLine())!=null) {
				StringTokenizer elements = new StringTokenizer(voucher, ";");
				String email = elements.nextToken();
				String type = elements.nextToken();
				int value = Integer.parseInt(elements.nextToken());
				boolean okGeneration = false;
				User a=null;
				//gaseste userul cu emailul 'email' si adauga-l in colectia de observers
				ArrayList<User> users = myVMS.getUsers();
				if(users==null) {
					okGeneration = true;
				}
				if(okGeneration==false) {
					for(i=0;i<users.size();i++) {
						if(users.get(i).email.equals(email)) {
							a = users.get(i);
							okGeneration = true;
						}
					}
				}
				if(okGeneration==true) { //adaugarea voucherului este o operatie valida
					if(campaign.nrAvailableVouchers!=0) {
						Voucher rez = campaign.generateVoucher(email, type, value);
						if(rez!=null) {	
							Object[] data = new Object[5];
							data[0]=rez.ID.toString();
							data[1]=rez.code.toString();
							data[2]=rez.campaignID.toString(); 
							data[3]=rez.getClass().getName();
							data[4]=rez.email;
							model.addRow(data);
							//adauga userul 'a' in lista de observeri a campaniei in care s-a generat voucher
							campaign.addObserver(a);
							a.vouchers.addVoucher(rez);
						}
					}
				else {
					ErrorWindow err = new ErrorWindow("Campania "+ campaign.name+ " nu mai are vouchere disponibile!!!");
					err.show();
				}
				}
				if(okGeneration==false) { //nu este valida adaugarea
					ErrorWindow err = new ErrorWindow("Adresa de email"+ email+" nu este valida!!!");
					err.show();
				}
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public MultipleGenerationVouchers(String title) {
		super(title);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		getContentPane().setBackground(Color.orange);
		Font f1 = new Font("Courier",Font.BOLD,12);
		setSize(2000,800);
		JLabel message = new JLabel("Introdu id-ul campaniei pentru care se face distribuire multipla de vouchere: ");
		JButton generate = new JButton("Realizeaza distribuirea multipla de vouchere");
		JTextField campaignId = new JTextField(6);
		JPanel p = new JPanel();
		p.add(message);
		p.add(campaignId);
		message.setFont(new Font("",1,14));
		message.setBackground(Color.green);
		generate.setFont(new Font("",1,14));
		generate.setBackground(Color.cyan);
		add(p);
		model.setColumnIdentifiers(columns);
		vouchers.setModel(model);
		vouchers.setBackground(Color.LIGHT_GRAY);
		TableColumnModel columnModel = vouchers.getColumnModel();
		int i;
		for(i=0;i<=4;i++) {
			columnModel.getColumn(i).setPreferredWidth(300);
		}
		scrollPane = new JScrollPane(vouchers);
		scrollPane.setPreferredSize(new Dimension(700, 700));
		add(scrollPane);
		add(generate);
		
		generate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer campaignID = Integer.parseInt(campaignId.getText());
				campaign = myVMS.getCampaign(campaignID);
				if(campaign.status.toString().equals("EXPIRED") || campaign.status.toString().equals("CANCELLED")) {
					ErrorWindow w = new ErrorWindow("Nu se pot genera vouchere in cadrul campaniei " + campaign.name +"!!!"+"(Campania este" +campaign.status+")");
					w.show();
					return;
				}
				if(campaign.nrAvailableVouchers==0) {
					ErrorWindow w = new ErrorWindow("Campania " + campaign.name +" nu mai are vouchere disponibile!!!");
					w.show();
					return;
				}
				generateVouchers(); //genereaza vouchere pentru userii din 'emails.txt'
			}
		});
	}
}
