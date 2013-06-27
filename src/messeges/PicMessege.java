package messeges;

public class PicMessege extends Messege{

	//fields:
	private String pic;
	
	public PicMessege(String posterName, String text, String pic, String time) {
		super(posterName, text,time);
		// TODO Auto-generated constructor stub
		this.pic = pic;
	}

	public String getPic() {
		return pic;
	}
	

}
