import java.util.*;

enum UserType {
	ADMIN, GUEST;
}

public class User implements Observer {
	Integer ID;
	String name;
	String password;
	String email;
	UserType type;
	ArrayList<Notification> notifications = new ArrayList<Notification>(); //colectie de notificari
	UserVoucherMap vouchers = new UserVoucherMap(); //colectia de vouchere
	
	public User(Integer ID, String name, String password, String email) {
		this.ID = ID;
		this.name = name;
		this.password = password;
		this.email = email;
	}
	
	public ArrayList<Notification> getNotificationList() {
		return notifications;
	}
	
	public void update(Observable campaign, Object notification) {
			this.notifications.add((Notification)notification);
	}

	public String toString() {
		return "["+ID + ";" + name + ";" + email+";"+type+"]";
	}
	
	public UserType getUserType() {
		return type;
	}
	
	public void setUserType(UserType t) {
		type = t;
	}
	
}
