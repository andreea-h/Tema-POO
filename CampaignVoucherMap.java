import java.util.ArrayList;

public class CampaignVoucherMap extends ArrayMap<String, ArrayList<Voucher>> {
	//K - emailul unui user, V - colectia de vouchere
	
	boolean addVoucher(Voucher v) {
		String key = (String)v.getEmail();
		if(!this.equals(null)){
			if(super.containsKey(key)==true) {
				ArrayList<Voucher> value = new ArrayList<Voucher>();
				value =(ArrayList<Voucher>) super.get(key);
				return value.add(v);
			}
			else if(super.containsKey(key)==false) { //este creata o noua intrare in dictionar
				ArrayList<Voucher> value = new ArrayList<Voucher>();
				value.add(v);
				super.put(key,value);
				return true;
			}
		}
		else if(this.equals(null)) {
			ArrayList<Voucher> value = new ArrayList<Voucher>();
			value.add(v);
			super.put(key,value);
			return true;
		}
		return false;
	}
}
