package messeges;

public class PicMessege extends Messege{

	//fields:
	private String pic;
	
	public PicMessege(String posterName, String text, String time, String pic) {
		super(posterName, text,time);
		// TODO Auto-generated constructor stub
		this.pic = pic;
	}

	public String getPic() {
		return pic;
	}
	

}
