import java.util.ArrayList;

class UserVoucherMap extends ArrayMap<Integer, ArrayList<Voucher>> {
	// K - ID-ul unei campanii, V - colectia de vouchere
	boolean addVoucher(Voucher v) {
		Integer key =(Integer)v.getCampaignID();
		if(!this.equals(null))
		{if(super.containsKey(key)==true) { //exista o intrare pentru cheia 'key'
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
	
	//intoarce o colectie cu toate voucherele din map
	public ArrayList<Voucher> getValues() {
		ArrayList<Voucher> values = new ArrayList<Voucher>();
		for(ArrayList<Voucher> v:this.values()) {
			for(Voucher a:v) {
				values.add(a);
			}
		}
		return values;
	}
}