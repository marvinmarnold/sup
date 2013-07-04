package messeges;

public class Messege {

	//fields:
	private String posterName;
	private String text;
	private String time;
	private String profilePicUrl;
	private String source;
	

	public Messege(String posterName, String text, String time, String profilePicUrl, String source) {
		super();
		this.posterName = posterName;
		this.text = text;
		this.time = time;
		this.profilePicUrl = profilePicUrl;
		this.source = source;
	}

	public String getSource() {
		return source;
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
