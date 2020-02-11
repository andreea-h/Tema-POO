import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

enum NotificationType {
	EDIT, CANCEL;
}
public class Notification {
	NotificationType type;
	LocalDateTime sendingDate; //data trimiterii notificarii
	Integer campaignID;
	ArrayList<String> codes = new ArrayList<String>(); //lista codurilor voucherelor aferente notificarii
	
	public Notification(Integer campaignId, LocalDateTime date) {
		this.campaignID = campaignId;
		sendingDate = date;			
	}
	
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		return campaignID+";"+codes+";"+sendingDate.format(formatter)+";"+type;
	}
	
	public NotificationType getNotificationType() {
		return type;
	}
	
	public void setNotificationType(NotificationType t) {
		type = t;
	}
	
	public void setCodesValues(ArrayList<String> codes) {
		for(String code:codes) {
			this.codes.add(code);
		}	
	}
	
}
