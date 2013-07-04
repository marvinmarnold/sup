package messeges;

public class LinkMessege extends Messege{

	//fields:
	private String link;
	
	public LinkMessege(String posterName, String text, String time, String profilePicUrl, String source, String link) {
		super(posterName, text,time, profilePicUrl, source);
		this.link = link;
		// TODO Auto-generated constructor stub
	}

	public String getLink() {
		return link;
	}
	
}
