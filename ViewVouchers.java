import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.imageio.*;
import javax.swing.table.*;
import java.util.*;

class ViewVouchers extends JFrame {
	VMS myVMS = VMS.getInstance();
	User currentUser;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	JScrollPane  scrollPane;
	JTable vouchers = new JTable();
	Object[] columns = {"Code","Status","UsageDate","CampaignName","Type"};
	DefaultTableModel model = new DefaultTableModel();
	
	//construieste un tabel cu voucherele userului 'currentUser' (currentUser este setat cu parametrul primit de constructor la apel)
	public void getTable() {
		model.setColumnIdentifiers(columns);
		vouchers.setModel(model);
		vouchers.setBackground(Color.LIGHT_GRAY);
		Object data[]= new Object[5];
		Collection<ArrayList<Voucher>> values = currentUser.vouchers.values();
		for(ArrayList<Voucher> v:values) {
			for(Voucher c : v) {
				data[0]=c.code.toString();
				data[1]=c.status.toString();
				if(c.usageDate!=null)
					data[2]=c.usageDate.format(formatter).toString();
				else
					data[2]="";
				data[3]=myVMS.getCampaign(c.campaignID).name;
				data[4]=c.getClass().getName(); //tipul de voucher
				model.addRow(data);
			}
		}
		TableColumnModel columnModel = vouchers.getColumnModel();
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		int i;
		for(i=0;i<=4;i++) {
			columnModel.getColumn(i).setPreferredWidth(300);
			columnModel.getColumn(i).setCellRenderer(centerRenderer);
		}
		vouchers.setFont(new Font("",1,14));
		vouchers.setRowHeight(16);
		vouchers.setBackground(Color.PINK);
	}
	
	public ViewVouchers(User user) {
		this.setSize(2000,400);
		currentUser = user;
		getTable();
		scrollPane = new JScrollPane(vouchers);
		add(scrollPane);
	}
}
