package messeges;

public class PicMessege extends Messege{

	//fields:
	private String picUrl;
	
	public PicMessege(String posterName, String text, String time, String profilePicUrl, String pic) {
		super(posterName, text,time, profilePicUrl);
		// TODO Auto-generated constructor stub
		this.picUrl = pic;
	}

	public String getPicUrl() {
		return picUrl;
	}
	

}
