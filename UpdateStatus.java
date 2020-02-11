import java.util.TimerTask;	

class UpdateStatus extends TimerTask {
	
	VMS myVMS = VMS.getInstance();
	
	public void run() {
		myVMS.updateStatus();
	}
}
