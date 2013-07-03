package messeges;

public class Messege {

	//fields:
	private String posterName;
	private String text;
	private String time;
	private String profilePicUrl;
	

	public Messege(String posterName, String text, String time, String profilePicUrl) {
		super();
		this.posterName = posterName;
		this.text = text;
		this.time = time;
		this.profilePicUrl = profilePicUrl;
	}

	public String getPosterName() {
		return posterName;
	}
	
	public String getText() {
		return text;
	}

	public String getProfilePicUrl() {
		return profilePicUrl;
	}

	public String getTime() {
		return time;
	}
	
	
	
	
}
