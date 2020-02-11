import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date.*;

public class Test {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	public void loadCampaigns(VMS myVMS) {
		final String path1 = "campaigns.txt";
		try {
			RandomAccessFile file1 = new RandomAccessFile(path1,"r");
			String nrOfCampaigns = file1.readLine();
			String localTime = file1.readLine();
			LocalDateTime currentDate = LocalDateTime.parse(localTime, formatter);
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
			    else if(currentDate.isAfter(c.startDate) && c.startDate.isBefore(c.endDate)) {
			    	st = "STARTED";
			    }
			    else if(currentDate.isAfter(c.endDate)) {
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
	}
	
	public void loadUsers(VMS myVMS) {
		final String path2 = "users.txt";
		try {
			RandomAccessFile file2 = new RandomAccessFile(path2,"r");
			String nrOfUsers = file2.readLine();
			String user = new String();
			while((user = file2.readLine())!=null) {
				StringTokenizer elements = new StringTokenizer(user,";");
				User u = new User(Integer.valueOf(elements.nextToken()),
						 elements.nextToken(),  elements.nextToken(),
						 elements.nextToken());
				UserType t = u.getUserType();
				u.setUserType(t.valueOf(elements.nextToken()));
				myVMS.addUser(u);
				}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addCampaignEvent(VMS myVMS, String event, LocalDateTime currentDate) {
		StringTokenizer elements1 = new StringTokenizer(event, ";");
		Integer userId = Integer.parseInt(elements1.nextToken());
		if(elements1.nextToken().equals("addCampaign")) {
			if(myVMS.getUserById(userId).type.toString().equals("ADMIN")) {
				Campaign c = new Campaign(Integer.valueOf(elements1.nextToken()),
						elements1.nextToken(),elements1.nextToken(),LocalDateTime.parse(elements1.nextToken(), formatter),
						LocalDateTime.parse(elements1.nextToken(), formatter),
						Integer.valueOf(elements1.nextToken()));
				String s = elements1.nextToken();
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
			    else if(currentDate.isAfter(c.startDate) && c.startDate.isBefore(c.endDate)) {
			    	st = "STARTED";
			    }
			    else if(currentDate.isAfter(c.endDate)) {
			    	st = "EXPIRED";
			    }
				CampaignStatusType status = c.getCampaignStatus();
				c.setCampaignStatus(status.valueOf(st));
				myVMS.addCampaign(c);
			}
		}
	}
	
	public void editCampaignEvent(VMS myVMS, String event, LocalDateTime currentDate) {
		StringTokenizer elements2 = new StringTokenizer(event, ";");
		Integer userId2 = Integer.parseInt(elements2.nextToken());
		if(elements2.nextToken().equals("editCampaign")) {
			Integer campaignId = Integer.parseInt(elements2.nextToken());
			if(myVMS.getUserById(userId2).type.toString().equals("ADMIN")) {
				Campaign campaign = new Campaign(campaignId, elements2.nextToken(),
					elements2.nextToken(), LocalDateTime.parse(elements2.nextToken(),formatter ),
								LocalDateTime.parse(elements2.nextToken(),formatter),
								Integer.valueOf(elements2.nextToken()));
				String s = myVMS.getCampaign(campaignId).getStrategy();
			    if(s.equals("A")) {
			    	campaign.strategy = new StrategyA("A");
			    }
			    else if(s.equals("B")) {
			    	campaign.strategy = new StrategyB("B");
			    }
			    else if(s.equals("C")) {
			    	campaign.strategy = new StrategyC("C");
			    }
				for(User a:myVMS.getCampaign(campaignId).getObservers()) {
					campaign.addObserver(a);
				}
				myVMS.updateCampaign(campaignId, campaign);
				
				Notification notification = new Notification(campaignId, currentDate);
				NotificationType type = notification.getNotificationType();
				notification.setNotificationType(type.valueOf("EDIT"));
				campaign.notifyAllObservers(notification);
				}
			}
	}
	
	public void cancelCampaignEvent(VMS myVMS, String event, LocalDateTime currentDate){	
		StringTokenizer elements3 = new StringTokenizer(event, ";");
		Integer userId3 = Integer.parseInt(elements3.nextToken());
		
		if(elements3.nextToken().equals("cancelCampaign")) {
			Integer campaignId = Integer.parseInt(elements3.nextToken());
		//	String endTime = elements3.nextToken();
		//	LocalDateTime endDate = LocalDateTime.parse(endTime, formatter);
			//verifaca daca userul este administrator
			if(myVMS.getUserById(userId3).type.toString().equals("ADMIN")) {
				myVMS.cancelCampaign(campaignId);
				//se trimite notificare catre useri
				Notification notification = new Notification(campaignId, currentDate);
				NotificationType type = notification.getNotificationType();
				notification.setNotificationType(type.valueOf("CANCEL"));
				myVMS.getCampaign(campaignId).notifyAllObservers(notification); 
				}
			}
	}
	
	public void generateVoucherEvent(VMS myVMS, String event, LocalDateTime currentDate) {
		StringTokenizer elements4 = new StringTokenizer(event, ";");
		Integer userId4 = Integer.parseInt(elements4.nextToken());
		if(elements4.nextToken().equals("generateVoucher")) {
			Integer campaignId = Integer.parseInt(elements4.nextToken());
			String email = elements4.nextToken();
			String voucherType = elements4.nextToken();
			float value = Float.parseFloat(elements4.nextToken());
			if(myVMS.getUserById(userId4).type.toString().equals("ADMIN")) {
				Campaign c = myVMS.getCampaign(campaignId);
				if(c.currentNrOfVouchers<c.nrVouchers) {
					Voucher v = c.generateVoucher(email, voucherType, value);
				//gaseste userul cu emailul 'email' si adauga-l in colectia de observers
				ArrayList<User> users = myVMS.getUsers();
				User a=null;
				int i;
				for(i=0;i<users.size();i++) {
					if(users.get(i).email.equals(email)) {
						a=users.get(i);
					}
				}
				if(a!=null) {
					myVMS.getCampaign(campaignId).addObserver(a);
					a.vouchers.addVoucher(v);
				}
				}
			}
		}
	}
	
		public void getVouchersEvent(VMS myVMS, String event,RandomAccessFile file4) {
			StringTokenizer elements5 = new StringTokenizer(event, ";");
			Integer userId5 = Integer.parseInt(elements5.nextToken());
			
			if(elements5.nextToken().equals("getVouchers")) {
				if(myVMS.getUserById(userId5).type.toString().equals("GUEST")) {
					if(myVMS.getUserById(userId5).vouchers!=null) {
						try {
						file4.writeBytes(myVMS.getUserById(userId5).vouchers.getValues()+"\n");
						}
						catch(IOException e) {
							e.printStackTrace();
						}
					}	
				}
			}	
		}
		
		public void getNotificationEvent(VMS myVMS, String event,RandomAccessFile file4) {
			StringTokenizer elements7 = new StringTokenizer(event, ";");
			Integer userId7 = Integer.parseInt(elements7.nextToken());
			
			if(elements7.nextToken().equals("getNotifications")) {
				if(myVMS.getUserById(userId7).type.toString().equals("GUEST")) {
					Integer asa = Integer.parseInt("2");
					try {
						file4.writeBytes(myVMS.getUserById(userId7).notifications+"\n");
					}
					catch(IOException e) {
						e.printStackTrace();
					}
					}
				}
		}
		
		public void getObserversEvent(VMS myVMS, String event,RandomAccessFile file4) {
			StringTokenizer elements6 = new StringTokenizer(event, ";");
			Integer userId6 = Integer.parseInt(elements6.nextToken());
			
			if(elements6.nextToken().equals("getObservers")) {
				if(myVMS.getUserById(userId6).type.toString().equals("ADMIN")) {
					Integer campaignId = Integer.parseInt(elements6.nextToken());
					try {
						file4.writeBytes(myVMS.getCampaign(campaignId).getObservers().toString().replace(", ","")+"\n");
					}
					catch(IOException e) {
						e.printStackTrace();
					}
					}
				}	
		}
		
		public void getNotificationsEvent(VMS myVMS, String event,RandomAccessFile file4) {
			StringTokenizer elements7 = new StringTokenizer(event, ";");
			Integer userId7 = Integer.parseInt(elements7.nextToken());
			
			if(elements7.nextToken().equals("getNotifications")) {
			
				if(myVMS.getUserById(userId7).type.toString().equals("GUEST")) {
					try {
						file4.writeBytes(myVMS.getUserById(userId7).notifications+"\n");
					}
					catch(IOException e) {
						e.printStackTrace();
					}
					}
				}	
		}
		
		public void redeemVoucherEvent(VMS myVMS, String event,RandomAccessFile file4, LocalDateTime currentDate) {
			StringTokenizer elements8 = new StringTokenizer(event, ";");
			Integer userId8 = Integer.parseInt(elements8.nextToken());
			
			if(elements8.nextToken().equals("redeemVoucher")) {
				if(myVMS.getUserById(userId8).type.toString().equals("ADMIN")) {
					Integer campaignId = Integer.parseInt(elements8.nextToken());
					String code = elements8.nextToken(); 
					Integer voucherId = Integer.parseInt(code);
					String time = elements8.nextToken();
					LocalDateTime date =  LocalDateTime.parse(time, formatter);
					myVMS.getCampaign(campaignId).redeemVoucher(voucherId, date);
					}
				}
		}
		
		public void getVoucherEvent(VMS myVMS, String event,RandomAccessFile file4) {
			StringTokenizer elements9 = new StringTokenizer(event, ";");
			Integer userId9 = Integer.parseInt(elements9.nextToken());
			if(elements9.nextToken().equals("getVoucher")) {
				if(myVMS.getUserById(userId9).type.toString().equals("ADMIN")) {
					Integer campaignId = Integer.parseInt(elements9.nextToken());
					if(myVMS.getCampaign(campaignId).currentNrOfVouchers<myVMS.getCampaign(campaignId).nrVouchers &&
							!myVMS.getCampaign(campaignId).status.toString().equals("EXPIRED") 
							&& !myVMS.getCampaign(campaignId).status.toString().equals("CANCELLED")) {
						try {
							file4.writeBytes(myVMS.getCampaign(campaignId).executeStrategy()+"\n");
						}
						catch(IOException e)	 {
							e.printStackTrace();
						}
						}
					}
				}
	}
	
	public void testWithoutGraphicalInterface() {
		VMS myVMS = VMS.getInstance();
		this.loadCampaigns(myVMS);
		this.loadUsers(myVMS);
		final String path3 = "events.txt";
		final String path4 = "output.txt";
		try {
			RandomAccessFile file3 = new RandomAccessFile(path3,"r");
			RandomAccessFile file4 = new RandomAccessFile(path4,"rw");
			//data curenta a aplicatiei
			String localTime = file3.readLine();
			LocalDateTime currentDate = LocalDateTime.parse(localTime, this.formatter);
			int nrOfEvents = Integer.parseInt(file3.readLine());
			String event = new String();
			while((event = file3.readLine())!=null) {
				
				StringTokenizer elements1 = new StringTokenizer(event, ";");
				Integer userId = Integer.parseInt(elements1.nextToken());
				
				if(elements1.nextToken().equals("addCampaign")) {
					this.addCampaignEvent(myVMS, event, currentDate);
				}
		
				StringTokenizer elements2 = new StringTokenizer(event, ";");
				Integer userId2 = Integer.parseInt(elements2.nextToken());
				if(elements2.nextToken().equals("editCampaign")) {
					this.editCampaignEvent(myVMS, event, currentDate);
				}
			
			StringTokenizer elements3 = new StringTokenizer(event, ";");
			Integer userId3 = Integer.parseInt(elements3.nextToken());
			
			if(elements3.nextToken().equals("cancelCampaign")) {
				this.cancelCampaignEvent(myVMS, event, currentDate);
			}
		
		
			StringTokenizer elements4 = new StringTokenizer(event, ";");
			Integer userId4 = Integer.parseInt(elements4.nextToken());
			if(elements4.nextToken().equals("generateVoucher")) {
				this.generateVoucherEvent(myVMS, event, currentDate);
			}
			
			StringTokenizer elements5 = new StringTokenizer(event, ";");
			Integer userId5 = Integer.parseInt(elements5.nextToken());
			
			if(elements5.nextToken().equals("getVouchers")) {
				this.getVouchersEvent(myVMS, event, file4);
			}
		
			StringTokenizer elements6 = new StringTokenizer(event, ";");
			Integer userId6 = Integer.parseInt(elements6.nextToken());
			
			if(elements6.nextToken().equals("getObservers")) {
				this.getObserversEvent(myVMS, event, file4);
			}
			
			StringTokenizer elements7 = new StringTokenizer(event, ";");
			Integer userId7 = Integer.parseInt(elements7.nextToken());
			
			if(elements7.nextToken().equals("getNotifications")) {
				this.getNotificationEvent(myVMS, event, file4);
			}
		
			StringTokenizer elements8 = new StringTokenizer(event, ";");
			Integer userId8 = Integer.parseInt(elements8.nextToken());
			
			if(elements8.nextToken().equals("redeemVoucher")) {
				this.redeemVoucherEvent(myVMS, event, file4, currentDate);
			}
			
			StringTokenizer elements9 = new StringTokenizer(event, ";");
			Integer userId9 = Integer.parseInt(elements9.nextToken());
			
			if(elements9.nextToken().equals("getVoucher")) {
				this.getVoucherEvent(myVMS, event, file4);
				}	
			}	
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
		
	public static void main(String[] args) {
		
		//testarea aplicatiei fara partea de interfata grafica
	//	Test obj = new Test();
	//	obj.testWithoutGraphicalInterface();
		
		// testarea aplicatiei din interfata grafica
		MainWindow firstWindow = MainWindow.getInstance();
		firstWindow.MainWindowComponents();
	}
}
