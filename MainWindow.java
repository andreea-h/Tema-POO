import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class MainWindow extends JFrame {
	JButton loadCampaigns;
	JButton loadUsers;
	JTextField name;
	JPasswordField password; //camp pentru introducere parola
	JButton login;  //buton de logare
	JPanel loginPanel;
	UserWindow userWindow;
	ErrorWindow userError;
	AdminWindow adminWindow;
	VMS myVMS;
	
	private static MainWindow instance;
	
	private MainWindow() {
		
	}
	
	public static MainWindow getInstance() {
		if(instance == null) {
			instance = new MainWindow();
		}
		return instance;
	}
	
	public void MainWindowComponents() {
		this.setSize(1000, 400);
		setLayout(new FlowLayout());
		
		JTextField name = new JTextField(20);
		name.setFont(new Font("SansSerif",Font.BOLD, 14));
		
		JPasswordField password = new JPasswordField(20);
		password.setFont(new Font("SansSerif",Font.BOLD, 14));
		
		JButton loadCampaigns = new JButton("LOAD CAMPAIGNS");
		loadCampaigns.setBackground(Color.pink);
		
		JButton loadUsers = new JButton("LOAD USERS");
		loadUsers.setBackground(Color.orange);
		
		JPanel loginPanel = new JPanel();
		JButton login = new JButton("LOGIN");
		
		login.setBackground(Color.cyan);
		add(new JLabel(new ImageIcon("image2.jpeg")));
		add(new JLabel(new ImageIcon("image1.jpeg")));
		add(new JLabel(new ImageIcon("image2.jpeg")));
		add(loadCampaigns);
		add(loadUsers);
		JLabel email = new JLabel("email: ");
		Font f = new Font("",1,15);
		email.setFont(f);
		JLabel pass = new JLabel("password: ");
		pass.setFont(f);
		loginPanel.add(email);
		loginPanel.add(name);
		loginPanel.add(pass);
		loginPanel.add(password);
		loginPanel.add(login);
		loginPanel.setBackground(Color.white);
		add(loginPanel);
		login.setEnabled(false); //butonul de logare devine accesibil dupa ce se incarca userii si campaniile
		
		VMS myVMS = VMS.getInstance();
		getContentPane().setBackground(Color.white);
		Test obj = new Test(); //folosesc o instanta a clasei Test pentru a putea refolosi metoda 'loadUsers' declarata acolo
	
		//incarca din 'campaigns.txt' campaniile
		loadCampaigns.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				final String path1 = "campaigns.txt";
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				try {
					RandomAccessFile file1 = new RandomAccessFile(path1,"r");
					String nrOfCampaigns = file1.readLine();
					//data curenta a aplicatiei este momentul rularii
					LocalDateTime currentDate = LocalDateTime.now();
				    String date = file1.readLine(); //aceasta data preluata din fisier(data curenta a aplicatiei) nu este folosita
											        //este inlocuita cu momentul curent la care se ruleaza aplicatia
				    String campaign = new String();
					while((campaign = file1.readLine())!=null) {
						StringTokenizer elements = new StringTokenizer(campaign,";");
						Campaign c = new Campaign(Integer.valueOf(elements.nextToken()),
								elements.nextToken(),elements.nextToken(),LocalDateTime.parse(elements.nextToken(), formatter),
								LocalDateTime.parse(elements.nextToken(), formatter),
								Integer.valueOf(elements.nextToken()));
						String s = elements.nextToken();
					    if(s.equals("A")) {
					    	c.strategy = new StrategyA("A");
					    }
					    else if(s.equals("B")) {
					    	c.strategy = new StrategyB("B");
					    }
					    else if(s.equals("C")) {
					    	c.strategy = new StrategyC("C");
					    }
					    String st = new String(); //pentru retinerea statusului campaniei
					    if(currentDate.isBefore(c.startDate)) {
					    	st = "NEW";
					    }
					    if(currentDate.isAfter(c.startDate) && c.startDate.isBefore(c.endDate)) {
					    	st = "STARTED";
					    }
					    if(currentDate.isAfter(c.endDate)) {
					    	st = "EXPIRED";
					    }
						CampaignStatusType status = c.getCampaignStatus();
						c.setCampaignStatus(status.valueOf(st));
						myVMS.addCampaign(c);
					}
					file1.close();
				}
				catch(IOException e) {
					e.printStackTrace();
				}
				loadCampaigns.setEnabled(false);
			}
		});
		
		//incarca userii din 'users.txt'
		loadUsers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				obj.loadUsers(myVMS); 
				login.setEnabled(true);
				loadUsers.setEnabled(false);
			}
		});
		
		//la apasarea butonului 'login' se afizeaza ferestra aferenta(admin/user)
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String user = name.getText();
				String userPassword = password.getText();
				// verificam daca autentificarea este valida
				// verifica daca exista userul in baza de date
				if(myVMS.findUser(user) == false) {
					userError = new ErrorWindow("Adresa de email introdusa NU ESTE VALIDA!");
					userError.show();
				}
				else {
					//verifica daca parola este corecta; in carul parolei corecte returneaza userul cu emailul si parola date 
					User result = myVMS.checkPassword(userPassword, user);
					if(result!=null) { //parola corecta
						if(result.type.toString().equals("GUEST")) {
							userWindow = new UserWindow("User Menu");
							//clasa userWindow are un membru 'currentUser' 
							userWindow.currentUser = new User(result.ID, result.name, result.password, result.email);
							userWindow.currentUser.vouchers = new UserVoucherMap();
							userWindow.currentUser.vouchers.putAll(result.vouchers);
							userWindow.currentUser.notifications = new ArrayList<Notification>();
							userWindow.currentUser.notifications.addAll(result.notifications);
						    
							userWindow.addComponents();
							userWindow.show(); //afiseaza fereastra de meniu a userului
						}
						else if(result.type.toString().equals("ADMIN")) {
							adminWindow = new AdminWindow(); 
							adminWindow.show();
						}
					}
					else if(result == null) {
						userError = new ErrorWindow("Parola GRESITA!!!");
						userError.show();
					}
				}
			}	
		});
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		show();
	}
	
		
}
