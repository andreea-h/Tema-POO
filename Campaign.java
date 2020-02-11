import java.text.*;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

enum CampaignStatusType {
	NEW, STARTED, EXPIRED, CANCELLED;
}

interface Strategy {
	public Voucher execute(Campaign c);
	public String getStrategyName();
}

class StrategyA implements Strategy {
	String strategyName;
	
	public StrategyA(String a) {
		strategyName = a;
	}
	
	public String getStrategyName() {
		return strategyName;
	}
	
	//returneza un voucher generat si distribuit conform strategiei A
	public Voucher execute(Campaign c) {
		ArrayList<User> observers = c.getObservers();
		int size = observers.size();
		// geneareaza aleator un nr intreg < size
		Random rand = new Random();
		int index = rand.nextInt(size);
		Voucher res = c.generateVoucher(observers.get(index).email,"GiftVoucher",100);
	    observers.get(index).vouchers.addVoucher(res);
	    c.addObserver(observers.get(index));
	    return res;
	}
}

class StrategyB implements Strategy {
	String strategyName;
	
	public StrategyB(String b) {
		strategyName = b;
	}
	
	public String getStrategyName() {
		return strategyName;
	}
	
	//returneza un voucher distribuit conform strategiei B
	public Voucher execute(Campaign c) {
		ArrayList<User> observers = c.getObservers();
		CampaignVoucherMap vouchers = c.vouchers;
		int i=0;
		int currentSize;
		int pos=0; //indexul intrarii in dictionar pentru userul care are cele mai multe vouchere utilizate
		int maxSize=0; 
		for(ArrayList<Voucher> v:vouchers.values()) {
			currentSize = 0;
			for(Voucher voucher:v) {
				if(voucher.status.toString().equals("USED")) {
					currentSize++;
				}
			}
			if(currentSize >= maxSize) {
				maxSize = currentSize;
				pos = i;
			}
			i++;
		}
		Voucher res = c.generateVoucher(observers.get(pos).email, "LoyaltyVoucher",50);
		observers.get(pos).vouchers.addVoucher(res);
		return res;
	}
}


class StrategyC implements Strategy {
	String strategyName;
	
	public StrategyC(String c) {
		strategyName = c;
	}
	
	public String getStrategyName() {
		return strategyName;
	}
	
	//returneza un voucher generat si distribuit conform strategiei C
	public Voucher execute(Campaign c) {
		ArrayList<User> observers = c.getObservers();
		CampaignVoucherMap vouchers = c.vouchers;
		int size = vouchers.size();
		int i=0;
		int currentSize;
		int pos=0; //indexul intrarii in dictionar care are cele mai multe vouchere
		int minSize=1000; 
		for(ArrayList<Voucher> v:vouchers.values()) {
			currentSize = v.size();
			if(currentSize < minSize) {
				minSize = currentSize;
				pos = i;
			}
			i++;
		}
		Voucher res = c.generateVoucher(observers.get(pos).email, "GiftVoucher",100);
	    observers.get(pos).vouchers.addVoucher(res);
	    return res;
	}
}

public class Campaign extends Observable{
	Integer ID;
	String name;
	String description;
	
	LocalDateTime startDate;
	LocalDateTime endDate;
	
	int nrVouchers; //nr total de vouchere care pot fi distribuite
	int nrAvailableVouchers; //nr de vouchere disponibile
	int currentNrOfVouchers; //nr curent de vouchere generate
	CampaignStatusType status; //statusul campaniei
	
	CampaignVoucherMap vouchers; //dictionarul de vouchere distribuite
	ArrayList<User> observers; //colectia de useri/observatori
	Strategy strategy; //tipul de gestionare al campaniei
	
	public Campaign(Integer ID, String name, String description, LocalDateTime startDate, LocalDateTime endDate, int budget) {
		this.ID = ID;
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.nrVouchers = budget;
		this.nrAvailableVouchers = budget;
		vouchers = new CampaignVoucherMap();
		observers = new ArrayList<User>();
	}
	
	//returneaza numele strategiei
	public String getStrategy() {
		return strategy.getStrategyName();
	}
	
	public ArrayList<Voucher> getVouchers() {
		ArrayList<Voucher> rez = new ArrayList<Voucher>();
		for(ArrayList<Voucher> vouchers:this.vouchers.values()) {
			for(Voucher v:vouchers) {
				rez.add(v);
			}
		}
		return rez;
	}
	
	//intoarce voucherul cu id-ul voucherId
	public Voucher getVoucher(Integer voucherId) {
		Voucher res = null;
		Collection<ArrayList<Voucher>>  values = vouchers.values();
		for(ArrayList<Voucher> v: values) {
			for(Voucher a:v) {
				if(a.ID.equals(voucherId)) {
					res = a;
				}
			}
		}
		return res;
	}
	
	public Voucher generateVoucher(String email, String voucherType, float value) {
		Voucher v = null;
		if(voucherType.equals("GiftVoucher")) {
			v = new GiftVoucher(value);
			v.email = email;
		}
		else if(voucherType.equals("LoyaltyVoucher")) {
			v = new LoyaltyVoucher(value);
			v.email = email;
		}
		v.campaignID = ID;
		String initialStatus = "UNUSED";
		VoucherStatusType status=null;
		v.setVoucherStatus(status.valueOf(initialStatus));
		//genereaza un cod unic pentru voucher
		Random r = new Random();
		int rand_int = r.nextInt(nrVouchers);
		v.code = rand_int+"";
		//genereaza incremental ID-ul asociat voucherului
		if(currentNrOfVouchers == 0) {
			v.ID = 1;
			currentNrOfVouchers++;
		}
		else {
			currentNrOfVouchers++;
			v.ID = currentNrOfVouchers;
		}
		nrAvailableVouchers --;
		
		v.code = v.ID.toString();
		
		vouchers.addVoucher(v);
		return v;
	}
	
	public void redeemVoucher(Integer voucherId, LocalDateTime date) {
		//gaseste voucherul cu codul 'code'
		Voucher v = getVoucher(voucherId);
	    if(endDate.isAfter(date)) { //data valida
	    	if(v.status.toString().equals("UNUSED") && !this.status.equals("EXPIRED") && !this.status.equals("CANCELLED")) {
	    		String usedStatus = "USED";
	    		VoucherStatusType status=null;
	    		v.usageDate = date; //seteaza data utilizarii voucherelui
	    		v.setVoucherStatus(status.valueOf(usedStatus));
	    	}
	   }
	}
	
	public ArrayList<User> getObservers() {
		return observers;
	}
	
	public void addObserver(User user) {
		if(observers == null)
			observers.add(user);
		else if(!observers.contains(user))
			observers.add(user);
	}
	
	public void removeObserver(User user) {
		if(observers.contains(user))
			observers.remove(user);
	}
	
	public void notifyAllObservers(Notification notification) {
		 for(User a:this.getObservers()) {
			 	Notification userNotification = new Notification(notification.campaignID, notification.sendingDate);
				NotificationType type = notification.getNotificationType();
				userNotification.setNotificationType(type);
				ArrayList<String> codes = new ArrayList<String>();
				Collection<ArrayList<Voucher>> values = a.vouchers.values();
				for(ArrayList<Voucher> c:values) {
					for(Voucher v:c) {
						if(notification.campaignID.equals(v.campaignID))
							codes.add(v.code);
					}
				}
				userNotification.codes = new ArrayList<String>();
				for(String code:codes) {
					userNotification.codes.add(code);
				}
				a.update(this, userNotification);
		 }
	}
	
	public Integer getID() {
		return ID;
	}
	
	public CampaignStatusType getCampaignStatus() {
		return status;
	}
	
	public String getCampaignName() {
		return name;
	}
	
	public void setCampaignStatus(CampaignStatusType t) {
		status=t;
	}
	
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		return ID +" "+ name +" "+description +" "+ startDate.format(formatter) + " " + endDate.format(formatter)+ " " + nrVouchers+" "+nrAvailableVouchers+" "+strategy.getStrategyName()+" "+status;
	}
	
	// trimite un voucher unui user conform strategiei campaniei
	public Voucher executeStrategy() {
		return strategy.execute(this);
	}
}



