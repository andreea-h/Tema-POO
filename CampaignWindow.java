import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.imageio.*;
import javax.swing.table.*;
import java.util.*;

public class CampaignWindow extends JFrame {
	VMS myVMS = VMS.getInstance();
	JTable campaigns; //tabelul cu campaniile
	JScrollPane  scrollPane;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	JButton sortByName; //buton de sortare dupa nume
	JButton sortByDatev1; //sortare dupa data de start crescator
	JButton sortByDatev2; //sortare dupa data de strat crescator
	JPanel addCampaign; //buton pentru adaugarea unei campanii
	Object[] columns = {"ID","Name","Description","startDate","endDate","status"};
	DefaultTableModel model = new DefaultTableModel();
	
	public void getTable() {
		model.setColumnIdentifiers(columns);
		campaigns.setModel(model);
		Object data[]= new Object[6];
		for(Campaign c :myVMS.campaigns) {
			data[0]=c.ID.toString();
			data[1]=c.name.toString();
			data[2]=c.description.toString();
			data[3]=c.startDate.format(formatter).toString();
			data[4]=c.endDate.format(formatter).toString();
			data[5]=c.status.toString();
			model.addRow(data);
		}
	}
	
	private static CampaignWindow myInstance;
	
	
	public static CampaignWindow getInstance() {
		if(myInstance == null)
			myInstance = new CampaignWindow();
		return myInstance;
	}
	
	private CampaignWindow() {
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		getContentPane().setBackground(new Color(51,153,255));
		setSize(2000,700);
		
		Font f1 = new Font("Courier",Font.BOLD,12);
		campaigns = new JTable();
		getTable();
		campaigns.setBackground(Color.PINK);
		campaigns.setForeground(Color.black);
		campaigns.setFont(new Font("",1,14));
		campaigns.setRowHeight(16);
		
		scrollPane = new JScrollPane(campaigns);
		scrollPane.setPreferredSize(new Dimension(900, 300));
		add(scrollPane);
		
		sortByName = new JButton("Ordoneaza campaniile alfabetic dupa nume");
		sortByDatev1 = new JButton("Ordoneaza campaniile crescator dupa data de start");
		sortByDatev2 = new JButton("Ordoneaza campaniile descrescator dupa data de start");
		
		Font f = new Font("",1,14);
		sortByName.setBackground(Color.white);
		sortByName.setFont(f);
		sortByName.setAlignmentX(Component.CENTER_ALIGNMENT);
		sortByDatev1.setBackground(Color.white);
		sortByDatev1.setFont(f);
		sortByDatev2.setBackground(Color.white);
		sortByDatev2.setFont(f);
		sortByDatev1.setAlignmentX(Component.CENTER_ALIGNMENT);
		sortByDatev2.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JButton observers = new JButton("Vezi observatorii campaniei selectate");
		observers.setAlignmentX(Component.CENTER_ALIGNMENT);
		observers.setBackground(Color.green);
		observers.setFont(f);
		JPanel panel = new JPanel();
		panel.add(sortByDatev1);
		panel.add(sortByDatev2);
		panel.add(sortByName);
		panel.add(observers);
		panel.setLayout(new BoxLayout (panel, BoxLayout.Y_AXIS));
		panel.setBackground(Color.BLUE);
		add(panel);
		
		sortByDatev1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//ordonare dupa data de start(crescator)
				TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
				campaigns.setRowSorter(sorter);
				ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
				int columnIndexToSort = 3; //coloana cu data de start 
				sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
				sorter.setSortKeys(sortKeys);
				sorter.sort();
			}
		});
		
		sortByDatev2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//ordonare dupa data de start(descrescator)
				TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
				campaigns.setRowSorter(sorter);
				ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
				int columnIndexToSort = 3; //coloana cu data de start
				sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.DESCENDING));
				sorter.setSortKeys(sortKeys);
				sorter.sort();
			}
		});
		
		sortByName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//ordonare alfabetica dupa nume
				TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
				campaigns.setRowSorter(sorter);
				ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
				int columnIndexToSort = 1; //coloana cu numele campaniilor
				sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
				sorter.setSortKeys(sortKeys);
				sorter.sort();
			}
		});
		
		JPanel options = new JPanel();
		JButton backToMain = new JButton("--> INAPOI LA PRIMA PAGINA <--");
		backToMain.setFont(new Font("SansSerif",Font.BOLD, 15));
		backToMain.setBackground(Color.YELLOW);
		options.add(backToMain);
		
		JButton cancelCampaign = new JButton("INCHIDE CAMPANIA SELECTATA");
		cancelCampaign.setFont(new Font("SansSerif",Font.BOLD, 15));
		cancelCampaign.setBackground(Color.CYAN);
		options.add(cancelCampaign);
		
		JButton printInfo = new JButton("Vizualizeaza informatii complete despre campanie");
		printInfo.setFont(new Font("SansSerif",Font.BOLD, 15));
		printInfo.setBackground(Color.YELLOW);
		options.add(printInfo);
		
		JButton adminVouchers = new JButton("-> Gestioneaza voucherele campaniei selectate <-");
		adminVouchers.setFont(new Font("SansSerif",Font.BOLD, 15));
		adminVouchers.setBackground(Color.CYAN);
		options.add(adminVouchers);
		add(options);
		
		JLabel id = new JLabel("campaignId:");
		JTextField campaignId = new JTextField(15);
		JLabel name = new JLabel("name:");
		JTextField campaignName = new JTextField(15);
		JLabel description = new JLabel("description:");
		JTextField campaignDescription = new JTextField(15);
		JLabel startD = new JLabel("startDate:");
		JTextField startDate = new JTextField("yyyy-MM-dd HH:mm",15);
		JLabel endD = new JLabel("endDate:");
		JTextField endDate = new JTextField("yyyy-MM-dd HH:mm",15);
		JLabel budgetl = new JLabel("budget:");
		JTextField budget = new JTextField(7);
		JLabel strategyl = new JLabel("strategy:");
		JTextField strategy = new JTextField(4);
		
		JPanel p = new JPanel();
		p.add(id);
		p.add(campaignId);
		p.add(name);
		p.add(campaignName);
		p.add(description);
		p.add(campaignDescription);
		p.add(startD);
		p.add(startDate);
		p.add(endD);
		p.add(endDate);
		p.add(budgetl);
		p.add(budget);
		p.add(strategyl);
		p.add(strategy);
		add(p);
		
		JButton saveCampaign = new JButton("Adauga campania");
		p.add(saveCampaign);
		
		//se preiau datele noii campanii din componentele JTextField si se genereaza o campanie noua(data sunt date valide)
		saveCampaign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//verifica daca au fost completate toate campurile noii campanii
				boolean ok =true ;
				if(campaignId.getText().equals("") || campaignName.getText().equals("") ||
						campaignDescription.getText().equals("") || startDate.getText().equals("") || endDate.getText().equals("")
						|| budget.getText().equals("")||strategy.getText().equals("")) {
					ok = false;
				}
				if(ok==false) {
					ErrorWindow w = new ErrorWindow("Nu ai completat toate campurile!!");
					w.show();
				}
				else {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
					LocalDateTime currentDate = LocalDateTime.now();
					Integer id = Integer.valueOf(campaignId.getText());
					LocalDateTime sd = LocalDateTime.parse(startDate.getText(), formatter);
					LocalDateTime ed = LocalDateTime.parse(endDate.getText(), formatter);
					int budgetv = Integer.parseInt(budget.getText());
					Campaign c = new Campaign(id,campaignName.getText(),campaignDescription.getText(),sd, ed, budgetv);	
					String s = strategy.getText();
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
				    else if(currentDate.isAfter(c.startDate) && currentDate.isBefore(c.endDate)) {
				    	st = "STARTED";
				    }
				    else if(currentDate.isAfter(c.endDate) && currentDate.isAfter(c.endDate)) {
				    	st = "EXPIRED";
				    }
					CampaignStatusType status = c.getCampaignStatus();
					c.setCampaignStatus(status.valueOf(st));
					//verificam daca exista campania 'c', ca indentificat am folosit id-ul campaniei
					if(myVMS.getCampaign(id)==null) {
						myVMS.addCampaign(c);
						Object[] data = new Object[6];
						data[0]=c.ID.toString();
						data[1]=c.name.toString();
						data[2]=c.description.toString();
						data[3]=c.startDate.format(formatter).toString();
						data[4]=c.endDate.format(formatter).toString();
						data[5]=c.status.toString(); 
						model.addRow(data);
					}
					else { //mesaj de eroare
						ErrorWindow w = new ErrorWindow("Campania a fost deja adaugata!!!!");
						w.show();
					}
				}
			}
		});
		
		JLabel idEdit = new JLabel("campaignId:");
		JTextField campaignIdEdit = new JTextField(15);
		JLabel nameEdit = new JLabel("name:");
		JTextField campaignNameEdit = new JTextField(15);
		JLabel descriptionEdit = new JLabel("description:");
		JTextField campaignDescriptionEdit = new JTextField(15);
		JLabel startEdit = new JLabel("startDate:");
		JTextField startDateEdit = new JTextField(16);
		JLabel endDEdit = new JLabel("endDate:");
		JTextField endDateEdit = new JTextField(16);
		JLabel budgetlEdit = new JLabel("budget:");
		JTextField budgetEdit = new JTextField(7);
	
		JPanel pEdit = new JPanel();
		pEdit.add(idEdit);
		pEdit.add(campaignIdEdit);
		pEdit.add(nameEdit);
		pEdit.add(campaignNameEdit);
		pEdit.add(descriptionEdit);
		pEdit.add(campaignDescriptionEdit);
		pEdit.add(startEdit);
		pEdit.add(startDateEdit);
		pEdit.add(endDEdit);
		pEdit.add(endDateEdit);
		pEdit.add(budgetlEdit);
		pEdit.add(budgetEdit);
		
		add(pEdit);
		pEdit.setVisible(false);
		
		JButton updateButton = new JButton("Salveaza modificarile");
		pEdit.add(updateButton);
		updateButton.setVisible(false);
		//la selectarea unei campanii din tabel se afizeaza in panel continand componente JTextField de unde se pot modifica caracteristicile campaniei
		campaigns.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int index = campaigns.getSelectedRow();
				pEdit.setVisible(true);
				Integer id = Integer.parseInt(campaigns.getValueAt(index, 0).toString());
				Campaign c = myVMS.getCampaign(id);
				updateButton.setVisible(true);
				campaignIdEdit.setText(campaigns.getValueAt(index, 0).toString());
				campaignNameEdit.setText(campaigns.getValueAt(index, 1).toString());
				campaignDescriptionEdit.setText(campaigns.getValueAt(index, 2).toString());
				startDateEdit.setText(campaigns.getValueAt(index, 3).toString());
				endDateEdit.setText(campaigns.getValueAt(index, 4).toString());
				budgetEdit.setText(c.nrAvailableVouchers+"");
			}
		});
		
		//modifica campania selectata din tabel cu datele preluate din componentele JTextField cu numele <*caracteristica*Edit>
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = campaigns.getSelectedRow();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				LocalDateTime currentDate = LocalDateTime.now();
				Integer id = Integer.valueOf(campaignIdEdit.getText());
				LocalDateTime sd = LocalDateTime.parse(startDateEdit.getText(), formatter);
				LocalDateTime ed = LocalDateTime.parse(endDateEdit.getText(), formatter);
				int budgetv = Integer.parseInt(budgetEdit.getText());
				//campaign - noua campanie
				Campaign campaign = new Campaign(id,campaignNameEdit.getText(),campaignDescriptionEdit.getText(),sd, ed, budgetv);	
				String s = myVMS.getCampaign(id).getStrategy();
			    if(s.equals("A")) {
			    	campaign.strategy = new StrategyA("A");
			    }
			    else if(s.equals("B")) {
			    	campaign.strategy = new StrategyB("B");
			    }
			    else if(s.equals("C")) {
			    	campaign.strategy = new StrategyC("C");
			    }
				boolean result = myVMS.updateCampaign(id, campaign);
				if(result == false) {
					ErrorWindow w = new ErrorWindow("Modificarea nu este permisa!!!");
					w.show();
				}
				else{
					model.setValueAt(campaignIdEdit.getText(), index, 0);
					model.setValueAt(campaignNameEdit.getText(), index, 1);
					model.setValueAt(campaignDescriptionEdit.getText(), index, 2);
					model.setValueAt(startDateEdit.getText(), index, 3);
					model.setValueAt(endDateEdit.getText(), index, 4);
					Notification notification = new Notification(id, currentDate);
					NotificationType type = notification.getNotificationType();
					notification.setNotificationType(type.valueOf("EDIT"));
					myVMS.getCampaign(id).notifyAllObservers(notification);
				}
			}
		});
	
		//inchide campania selectata din tabel
		cancelCampaign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(campaigns.getSelectionModel().isSelectionEmpty()) {
					ErrorWindow err = new ErrorWindow("NU AI SELECTAT NICIO CAMPANIE DIN TABEL!!!");
					err.show();
					return;
				}
				int index = campaigns.getSelectedRow();
				Integer id = Integer.parseInt(campaigns.getValueAt(index, 0).toString());
				Campaign c = myVMS.getCampaign(id);
				myVMS.cancelCampaign(id);
				if(!c.status.toString().equals("CANCELLED")) { //nu este valida operatia
					ErrorWindow a = new ErrorWindow("Campania "+c.name+" nu poate fi inchisa");
					a.show();
				}
				else { //campania a fost inchisa cu succes
					model.setValueAt("CANCELLED", index, 5); 
					LocalDateTime currentDate = LocalDateTime.now();
					Notification notification = new Notification(c.ID, currentDate);
					NotificationType type = notification.getNotificationType();
					notification.setNotificationType(type.valueOf("CANCEL"));
					c.notifyAllObservers(notification);
				}			
			}
		});
		
		JPanel a = new JPanel(); //a contine componente JLabel pentru afisarea de informatii depre campanie
		a.setLayout (new BoxLayout (a, BoxLayout.Y_AXIS));
		a.setPreferredSize(new Dimension(1700,200));
		a.setBackground(Color.PINK);
		JLabel campaignId1 = new JLabel();
		campaignId1.setFont(new Font("SansSerif",Font.BOLD, 14));
		JLabel name1 = new JLabel();
		name1.setFont(new Font("SansSerif",Font.BOLD, 14));
		JLabel description1 = new JLabel();
		description1.setFont(new Font("SansSerif",Font.BOLD, 14));
		JLabel start1 = new JLabel();
		start1.setFont(new Font("SansSerif",Font.BOLD, 14));
		JLabel end1 = new JLabel();
		end1.setFont(new Font("SansSerif",Font.BOLD, 14));
		JLabel status1 = new JLabel();
		status1.setFont(new Font("SansSerif",Font.BOLD, 14));
		JLabel nrVouchers = new JLabel();
		nrVouchers.setFont(new Font("SansSerif",Font.BOLD, 14));
		JLabel nrAVouchers = new JLabel();
		nrAVouchers.setFont(new Font("SansSerif",Font.BOLD, 14));
		JLabel strategy1 = new JLabel();
		strategy1.setFont(new Font("SansSerif",Font.BOLD, 14));
		
		//se afiseaza pagina de administrare a voucherelor pentru campania selectata din tabel
		adminVouchers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(campaigns.getSelectionModel().isSelectionEmpty()) {
					ErrorWindow err = new ErrorWindow("NU AI SELECTAT NICIO CAMPANIE DIN TABEL!!!");
					err.show();
					return;
				}
				int index = campaigns.getSelectedRow();
				Integer id = Integer.parseInt(campaigns.getValueAt(index, 0).toString());
				VoucherWindow v = new VoucherWindow(id+"");
				v.show();
			}
		});
		
		//se revine la pagina de logare
		backToMain.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				dispose();
				MainWindow firstWindow = MainWindow.getInstance();
				firstWindow.show();
			}
		});
		
		add(a);
		a.setVisible(false);
		
		//se afiseaza informatiile despre campania selectata din tabel
		printInfo.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent e) {
				if(campaigns.getSelectionModel().isSelectionEmpty()) {
					ErrorWindow err = new ErrorWindow("NU AI SELECTAT NICIO CAMPANIE DIN TABEL!!!");
					err.show();
					return;
				}
				int index = campaigns.getSelectedRow();
				Integer id = Integer.parseInt(campaigns.getValueAt(index, 0).toString());
				Campaign c = myVMS.getCampaign(id);
				campaignId1.setText("->campaign Id: "+c.ID.toString());
				name1.setText("->name: "+c.name);
				description1.setText("->description: "+c.description);	
				start1.setText("->startDate: "+c.startDate.format(formatter));
				end1.setText("->endDate: "+c.endDate.format(formatter));
				status1.setText("->status: "+c.status.toString());
				nrVouchers.setText("->nr total de vouchere: "+c.nrVouchers+"");
				nrAVouchers.setText("->nr curent de vouchere disponibile: "+c.nrAvailableVouchers+"");
				strategy1.setText("->strategy: "+c.getStrategy());
				a.add(campaignId1);
				a.add(name1);
				a.add(description1);
				a.add(start1);
				a.add(end1);
				a.add(status1);
				a.add(nrVouchers);
				a.add(nrAVouchers);
				a.add(strategy1);
				a.setVisible(true);
			}
		});
		
		//se afiseaza pagina cu userii din campania selectata din tabel
		observers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(campaigns.getSelectionModel().isSelectionEmpty()) {
					ErrorWindow err = new ErrorWindow("NU AI SELECTAT NICIO CAMPANIE DIN TABEL!!!");
					err.show();
					return;
				}
				int index = campaigns.getSelectedRow();
				Integer id = Integer.parseInt(campaigns.getValueAt(index, 0).toString());
				Campaign c = myVMS.getCampaign(id);
				ViewObservers obs = new ViewObservers(c);
				obs.show();
			}
		});
		
	}
}
