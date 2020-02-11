import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

import javax.imageio.*;

public class VoucherWindow extends JFrame {
	VMS myVMS = VMS.getInstance();
	JButton back = new JButton("BACK to Campaigns page");
	Object[] columns = {"Id","Code","Status","UsageDate","Email","CampaignId","Type"};
	DefaultTableModel model = new DefaultTableModel();
	JTable vouchers;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	Integer campaignId; //id-ul campaniei
	
	public void getTable() {
		model.setColumnIdentifiers(columns);
		vouchers = new JTable();
		vouchers.setModel(model);
		Object data[]= new Object[7];
		if(myVMS.getCampaign(campaignId).vouchers!=null) {
			Collection<ArrayList<Voucher>> values = myVMS.getCampaign(campaignId).vouchers.values();
			if(values!=null) {
				for(ArrayList<Voucher> v: values) {
					if(v!=null) {
						for(Voucher a:v) {
							data[0] = a.ID.toString();
							data[1] = a.code;
							data[2] = a.status.toString();
							if(a.usageDate!=null)
								data[3] = a.usageDate.format(formatter).toString();
							else
								data[3] = "";
							data[4] = a.email.toString();
							data[5] = a.campaignID.toString();
							data[6] = a.getClass().getName();
							model.addRow(data);
						}
					}
				}
			}
		}
	}
	
	JPanel panel = new JPanel();
	JLabel email = new JLabel("email user:");
	JLabel type = new JLabel("voucher type:");
	JLabel value = new JLabel("value: ");
	JTextField getEmail = new JTextField(10);
	JRadioButton getType1 = new JRadioButton("GiftVoucher");
	JRadioButton getType2 = new JRadioButton("LoyaltyVoucher");
	ButtonGroup getType = new ButtonGroup();
	JTextField getValue = new JTextField(10);
	
	public VoucherWindow(String title) {
		super(title);
		
		getContentPane().setBackground(Color.orange);
		setSize(2000,700);
		setLayout(new FlowLayout());
		
		//index - indicele randului selectat din tabelul de campanii
		int index = CampaignWindow.getInstance().campaigns.getSelectedRow();
		Integer id = Integer.parseInt(CampaignWindow.getInstance().model.getValueAt(index, 0).toString());
		campaignId = id;
		getTable();
		
		panel.add(email);
		panel.add(getEmail);
		panel.add(type);
		getType.add(getType1);
		getType.add(getType2);
		panel.add(getType1);
		panel.add(getType2);
		panel.add(value);
		panel.add(getValue);
		panel.setLayout(new BoxLayout (panel, BoxLayout.Y_AXIS));
		add(panel);
		
		JButton generateVoucher = new JButton("Genereaza un nou voucher");
		generateVoucher.setBackground(Color.green);
		add(generateVoucher);
		JScrollPane scrollPane = new JScrollPane(vouchers);
		scrollPane.setPreferredSize(new Dimension(900, 300));
		add(scrollPane);
		
		Font f = new Font("",1,14);
		JButton getVoucherByStrategy = new JButton("GENEREAZA VOUCHER CONFORM STRATEGIEI");
		getVoucherByStrategy.setBackground(Color.GREEN);
		getVoucherByStrategy.setFont(f);
		getVoucherByStrategy.setEnabled(false); //se poate accesa doar dupa generarea primului voucher
		
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				CampaignWindow.getInstance().setVisible(true);
			}
		});
			
		generateVoucher.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean ok =true ;
				if(getEmail.getText().equals("") || getValue.getText().contentEquals("") ||(getType1.isSelected()==false && getType2.isSelected()==false)) {
					ok = false;
				}
				if(ok==false) {
					ErrorWindow w = new ErrorWindow("Nu ai completat toate campurile!!");
					w.show();
				}
				//nu este valida generarea de vouchere in cadrul campaniei
				if(myVMS.getCampaign(campaignId).status.toString().equals("CANCELLED") || myVMS.getCampaign(campaignId).status.toString().equals("EXPIRED")) {
					ErrorWindow w = new ErrorWindow("Nu se pot genera vouchere in cadrul campaniei (Campania este "+myVMS.getCampaign(campaignId).status.toString()+")");
					w.show();
					return;
				}
				else {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
					LocalDateTime currentDate = LocalDateTime.now();
					String email = getEmail.getText();
					String type="";
					if(getType1.isSelected()==true)
						type = "GiftVoucher";
					else if(getType2.isSelected()==true)
						type = "LoyaltyVoucher";
					String value = getValue.getText();
					boolean okGeneration = false;
					User a=null;
					//gaseste userul cu emailul 'email' si adauga-l in colectia de observers
					ArrayList<User> users = myVMS.getUsers();
					if(users==null) {
						okGeneration = true;
					}
					if(okGeneration==false) {
						int i;
						for(i=0;i<users.size();i++) {
							if(users.get(i).email.equals(email)) {
								a = users.get(i);
								okGeneration = true;
							}
						}
					}
					if(okGeneration==true) { //adaugare voucherului este o operatie valida
						if(myVMS.getCampaign(campaignId).nrAvailableVouchers!=0) {
							Voucher rez = myVMS.getCampaign(campaignId).generateVoucher(email, type, Integer.parseInt(value));
							if(rez!=null) {	
								Object[] data = new Object[7];
								data[0]=rez.ID.toString();
								data[1]=rez.code.toString();
								data[2]=rez.status.toString();
								data[3]="";
								data[4]=rez.email.toString(); 
								data[5]=rez.campaignID.toString();
								data[6]=rez.getClass().getName();
								model.addRow(data);
								//adauga userul 'a' in lista de observeri a campaniei in care s-a generat voucher
								myVMS.getCampaign(campaignId).addObserver(a);
								a.vouchers.addVoucher(rez);
								getVoucherByStrategy.setEnabled(true);
							}
						}
						else {
							ErrorWindow err = new ErrorWindow("Campania "+ myVMS.getCampaign(campaignId).name+ " nu mai are vouchere disponibile!!!");
							err.show();
						}
					}
					if(okGeneration==false) { //nu este valida adaugarea
						ErrorWindow err = new ErrorWindow("Adresa de email introdusa nu este valida!!!");
						err.show();
					}
					}
				}
		});
		
		JPanel redeem = new JPanel();
		JLabel findId = new JLabel("Introdu ID-ul voucherului cautat: ");
		JTextField getId = new JTextField(6);
		JButton redeemButton = new JButton("Marcheaza voucherul ca fiind utilizat");
		redeemButton.setBackground(Color.magenta);
		redeem.add(findId);
		redeem.add(getId);
		redeem.add(redeemButton);
		add(redeem);
		redeem.setLayout(new FlowLayout());
		
		redeemButton.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				String voucherId = getId.getText();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				LocalDateTime currentDate = LocalDateTime.now();
				Integer ID = Integer.valueOf(voucherId);
				myVMS.getCampaign(campaignId).redeemVoucher(ID, currentDate);
				if(myVMS.getCampaign(campaignId).getVoucher(ID).status.toString().equals("UNUSED")) {
					//nu este valida utilizarea voucherului
					ErrorWindow err = new ErrorWindow("Voucherul nu poate fi marcat ca utilizat!!!");
					err.show();
				}
				else { //voucherul este marcat ca utilizat
					int row=0; //se cauta linia din tabel care contine voucherul modificat
					int i;
					for(i=0;i<model.getRowCount();i++) {
						if(model.getValueAt(i, 0).equals(ID.toString())) {
							row=i;
						}
					}
					model.setValueAt("USED", row, 2);
					model.setValueAt(currentDate.format(formatter),row ,3);
				}
			}
		});
		
		add(getVoucherByStrategy);
		
		getVoucherByStrategy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//verifica daca se pot genera vouchere in cadrul campaniei curente
				if(myVMS.getCampaign(campaignId).status.toString().equals("CANCELLED") || myVMS.getCampaign(campaignId).status.toString().equals("EXPIRED")) {
					ErrorWindow w = new ErrorWindow("Nu se pot genera vouchere in cadrul campaniei (Campania este "+myVMS.getCampaign(campaignId).status.toString()+")");
					w.show();
					return;
				}
				//daca campania are mai are vouchere disponibile
				if(myVMS.getCampaign(campaignId).currentNrOfVouchers<myVMS.getCampaign(id).nrVouchers) {
					Voucher rez=myVMS.getCampaign(id).executeStrategy(); //genereaza voucher conform startegiei
					Object[] data = new Object[7];
					data[0]=rez.ID.toString();
					data[1]=rez.code.toString();
					data[2]=rez.status.toString();
					data[3]="";
					data[4]=rez.email.toString(); 
					data[5]=rez.campaignID.toString();
					data[6]=rez.getClass().getName();
					model.addRow(data); //adauga o noua intrare in tabel
				}
				else { //campania nu mai are vouchere disponibile
					ErrorWindow w = new ErrorWindow("Campania "+myVMS.getCampaign(campaignId).name+" nu mai are vouchere disponibile!!!");
					w.show();
					return;
				}
			}
		});
		
		back.setBackground(Color.gray);
		add(back);
	}
}
