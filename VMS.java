import java.time.LocalDateTime;
import java.util.*;

public class VMS {
	ArrayList<Campaign> campaigns;  //campaniile existente
	ArrayList<User> users;  //utilizatorii care pot primi diverse vouchere
	
	ArrayList<Campaign> getCampaigns() {
		return campaigns;
	}
	
	private static VMS instance;
	
	//constructor privat doar pentru a nu permine instantierea
	private VMS() {
		
	}
	
	public static VMS getInstance() {
		if(instance == null) {
			instance = new VMS();
		}
		return instance;
	}
	
	void addCampaign(Campaign campaign) {
		if(campaigns == null) {
			campaigns = new ArrayList<Campaign>();
		}
		if(!campaigns.contains(campaign))
			campaigns.add(campaign);
	}
	
	//intoarce indexul din colectia de campanii avand id-ul 'id'
	public int getIndexOfCampaign(Integer id) {
		int i;
		for(i=0;i<campaigns.size();i++) {
			if(campaigns.get(i).ID.equals(id)) {
				return i;
			}
		}
		return -1; //nu exista o campanie cu id-ul 'id' in colectia campaniilor
	}
	
	//returneaza campania cu 'id' specificat
	Campaign getCampaign(Integer id) {
		int size = campaigns.size();
		int i;
		for(i=0; i<size; i++) {
			Campaign c = (Campaign)campaigns.get(i);
			if(c.getID().equals(id)) {
				return c;
			}
		}
		return null; //nicio campanie cu id-ul 'id'
	}
	
	//functia 'updateCampaign' intoarce 'false' daca operatia de modificare nu este una permisa
	public boolean updateCampaign(Integer id, Campaign campaign) {
		Campaign c = getCampaign(id); //campania cu id-ul 'id'
		CampaignStatusType type = c.getCampaignStatus();
		// pentru NEW - se pot modifica toate campurile mai putin cel corespunzator nr de vouchere disponibile
		// pentru STARTED - se pot modifica doar campurile pentru nr total de vouchere si endDate-ul
		if((type.toString().equals("NEW") && c.nrAvailableVouchers == campaign.nrAvailableVouchers)||  
				((type.toString().equals("STARTED") && campaign.name.equals(c.name)
				&& campaign.description.equals(c.description) 
				&& campaign.startDate.equals(c.startDate)))) {
			int position = getIndexOfCampaign(id);
			campaign.status = type;
			campaign.observers = c.observers;
			campaign.vouchers = c.vouchers;
			campaigns.set(position, campaign);
			return true; //modificare realizata cu succes
		}
		else
			return false;
	}
	
	void cancelCampaign(Integer id) {
		Campaign c = getCampaign(id);
		// se pot inchide doar camapiniile 'NEW' sau 'STARTED'
		if(c.getCampaignStatus().toString().equals("NEW")==true || 
		   c.getCampaignStatus().toString().equals("STARTED")==true) {
			String s = "CANCELLED";
			CampaignStatusType status = c.getCampaignStatus();
			c.setCampaignStatus(status.valueOf(s));
		}
	}
	
	ArrayList<User> getUsers() {
		return users;
	}
	
	void addUser(User user) {
		if(users == null) {
			users = new ArrayList<User>();
		}
		if(!users.contains(user))
			users.add(user);
	}
	
	//intoarce userul cu id-ul 'userId'
	public User getUserById(Integer userId) {
		int i;
		for(i=0;i<users.size();i++) {
			if(users.get(i).ID.equals(userId)) {
				return users.get(i);
			}
		}
		return null;
	}
	
	boolean findUser(String email) {
		for(User a:users) {
			if(a.email.equals(email)) {
				return true;
			}
		}
		return false;
	}
	
	//intoarce userul cu emailul "userEmail" si cu parola password daca parola este corecta
	//daca parola nu este corecta, intoarce null
	//presupunem ca metoda este apelata doar cu adrese valide
	public User checkPassword(String password, String userEmail) {
		User find;
		for(User a:users) {
			if(a.email.equals(userEmail) && a.password.equals(password)) {
				return a;
			}
		}
		return null;
	}
	
	public void updateStatus() {
		LocalDateTime currentDate = LocalDateTime.now();
		String s ="";
		for(Campaign c:campaigns) {
			if(c.status.toString().equals("NEW") && currentDate.isAfter(c.startDate) && currentDate.isBefore(c.endDate)) {
				s = "STARTED";
				CampaignStatusType status = c.getCampaignStatus();
				c.setCampaignStatus(status.valueOf(s));
				Notification notification = new Notification(c.ID, currentDate);
				NotificationType type = notification.getNotificationType();
				notification.setNotificationType(type.valueOf("EDIT"));
				c.notifyAllObservers(notification); //trimite notificari userilor campaniei
				int i, index=0;
				for(i=0;i<CampaignWindow.getInstance().model.getRowCount();i++) {
					if(CampaignWindow.getInstance().model.getValueAt(i, 0).equals(c.ID.toString())) {
						index=i;
					}
				}
				CampaignWindow.getInstance().model.setValueAt(s, index, 5); //se modifica si intrarea aferenta din tabelul campaniilor
			}
			else if(c.status.toString().equals("STARTED") && currentDate.isAfter(c.endDate)) {
				s = "EXPIRED";
				CampaignStatusType status = c.getCampaignStatus();
				c.setCampaignStatus(status.valueOf(s));
				Notification notification = new Notification(c.ID, currentDate);
				NotificationType type = notification.getNotificationType();
				notification.setNotificationType(type.valueOf("EDIT"));
				c.notifyAllObservers(notification);
				//index retine indicele liniei din tabel la care se afla campania modificata
				int i, index=0;
				for(i=0;i<CampaignWindow.getInstance().model.getRowCount();i++) {
					if(CampaignWindow.getInstance().model.getValueAt(i, 0).equals(c.ID.toString())) {
						index=i;
					}
				}
				CampaignWindow.getInstance().model.setValueAt(s, index,5);
			}
		}
	}
}
