import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.imageio.*;
import javax.swing.table.*;
import java.util.*;

class ViewCampaigns extends JFrame { 
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	VMS myVMS = VMS.getInstance();
	JTable campaigns = new JTable();
	Object[] columns = {"Name","Description","startDate","endDate","status"};
	DefaultTableModel model = new DefaultTableModel();
	
	//costruieste tabelul cu datele campaniilor din colectie
	public void getTable() {
		model.setColumnIdentifiers(columns);
		campaigns.setModel(model);
		Object data[]= new Object[5];
		for(Campaign c :myVMS.campaigns) {
			data[0]=c.name.toString();
			data[1]=c.description.toString();
			data[2]=c.startDate.format(formatter).toString();
			data[3]=c.endDate.format(formatter).toString();
			data[4]=c.status.toString();
			model.addRow(data);
		}
		TableColumnModel columnModel = campaigns.getColumnModel();
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		int i;
		for(i=0;i<=4;i++) {
			columnModel.getColumn(i).setPreferredWidth(300);
			columnModel.getColumn(i).setCellRenderer(centerRenderer);
		}
		campaigns.setFont(new Font("",1,14));
		campaigns.setRowHeight(16);
		campaigns.setBackground(Color.PINK);
		getContentPane().setBackground(Color.orange);
	}
	
	public ViewCampaigns(String title) {
		super(title);
		getTable();
		setSize(2000,400);
		JScrollPane camp = new JScrollPane(campaigns);
		add(camp);
	}
}
