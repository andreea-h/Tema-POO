import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;

enum VoucherStatusType {
	USED, UNUSED;
}

abstract class Voucher {
	Integer ID;
	String code;
	VoucherStatusType status; //statusul voucher-ului
	LocalDateTime usageDate;
	String email; //emailul care care a fost distribuit voucherul
	Integer campaignID; //ID-ul campaniei in care a fost generat voucherul
	
	public Integer getID() {
		return ID;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getEmail() {
		return email;
	}
	
	public Integer getCampaignID() {
		return campaignID;
	}
	
	public void setVoucherStatus(VoucherStatusType t) {
		status=t;
	}
	 
	public abstract String toString();
}

class GiftVoucher extends Voucher {
	float maxSum; //suma care poate fi utilizata o singura data
	
	public GiftVoucher(float maxSum) {
		this.maxSum = maxSum;
	}
	
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		if(usageDate!=null)
			return "["+ID + ";"+ status+";"+email+";"+maxSum+";"+campaignID+";"+ usageDate.format(formatter).toString()+"]" ;	
		else
			return "["+ID + ";"+ status+";"+email+";"+maxSum+";"+campaignID+";"+ usageDate+"]";
	}
}

class LoyaltyVoucher extends Voucher {
	float offer; //reducere
	public LoyaltyVoucher(float offer) {
		this.offer = offer;
	}
	
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		if(usageDate!=null)
			return "["+ID + ";"+ status+";"+email+";"+offer+";"+campaignID+";"+ usageDate.format(formatter).toString()+"]" ;	
		else
			return "["+ID + ";"+ status+";"+email+";"+offer+";"+campaignID+";"+ usageDate+"]";
	}
}