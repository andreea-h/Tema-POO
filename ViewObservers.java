import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

class ViewObservers extends JFrame {
	VMS myVMS = VMS.getInstance();
	Object[] columns = {"Id","Name","Email","Type","UsedVouchers"};
	DefaultTableModel model = new DefaultTableModel();
	JTable users;
	JScrollPane scrollPane;
	Campaign campaign; //campania pentru care afisam userii
	int totalUsedVouchers;
	
	//contruieste tabelul cu observerii din campania 'campaign'
	public void getTable() {
		model.setColumnIdentifiers(columns);
		users = new JTable();
		users.setModel(model);
		Object data[]= new Object[5];
		if(campaign.observers!=null) {
			for(User v:campaign.observers) {
				if(v!=null) {
					data[0] = v.ID.toString();
					data[1] = v.name;
					data[2] = v.email;
					data[3] = v.type.toString();
					int usedVouchers = 0;
					ArrayList<Voucher> vouchers = v.vouchers.get(campaign.ID);
					for(Voucher a:vouchers) {
						if(a.status.toString().equals("USED")) {
							usedVouchers++;
						}
					}
					totalUsedVouchers = totalUsedVouchers + usedVouchers;
					data[4] = usedVouchers;
					model.addRow(data);	
				}
			}
		}
		users.setFont(new Font("",1,14));
		users.setRowHeight(16);
		users.setBackground(Color.PINK);
	}
	
	public ViewObservers(Campaign c) {
		getContentPane().setBackground(Color.orange);
		this.setLayout(new FlowLayout());
		this.setSize(2000,700);
		campaign = c;
		getTable();
		scrollPane = new JScrollPane(users);
		scrollPane.setPreferredSize(new Dimension(900, 300));
		JLabel usedVouchers = new JLabel ("Nr de vouchere utilizate in cadrul campaniei: "+totalUsedVouchers);
		add(usedVouchers);
		add(scrollPane);
	}
}
