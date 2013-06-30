package com.google.android.apps.sup;

import java.util.ArrayList;

import messeges.LinkMessege;
import messeges.Messege;
import messeges.PicMessege;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

public class NewsActivity extends Activity {

	private ArrayList<Messege> messeges;
	private static final String TAG = "NewsActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
		if(GlobalInfo.session.isOpened()){
			getFeed(GlobalInfo.session);
		}
		Log.v(TAG, "Got to onCreate()");
	}

	//reutrns the final news feed
	public void getFeed(Session session) {
		Bundle params = new Bundle();
		params.putString("limit", "0");

		Request request = new Request(session, "me/home", params,
				HttpMethod.GET, new Request.Callback() {
					public void onCompleted(Response response) {
						ArrayList<JSONObject> JSONmessages = buildValsFromResponse(response);
						sort(JSONmessages);
						try {
							messeges = parse(JSONmessages);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
//						for(int i=0;i<messeges.size();i++){
//					    	Log.i(TAG, messeges.get(0).getPosterName());

//						}
						
					}

				});
		request.executeAsync();
	}
	
	public void sanityCheck(JSONObject object){
		Log.i(TAG, object.names().toString());
	}
	
	//reutrns an array of JSON objects, each elemnt is a message
	public ArrayList<JSONObject> buildValsFromResponse(Response response) {

		JSONObject jo = response.getGraphObject().getInnerJSONObject();
		JSONArray ja;
		JSONObject tempJo;
		ArrayList<JSONObject> arr = null;
		try {
			ja = jo.getJSONArray("data");
			arr = new ArrayList<JSONObject>();
			for (int i = 0; i < ja.length(); i++) {
				tempJo = ja.getJSONObject(i);
				arr.add(tempJo);

			}
		} catch (JSONException e) {
	    	Log.i(TAG, "no response");
			e.printStackTrace();
		}

		return arr;
	}

	//dells all irrelevent messages
	public ArrayList<JSONObject> sort(ArrayList<JSONObject> arr) {

		for (int i = 0; i < arr.size(); i++) {
			// dell irelevent messages
		}

		return arr;
	}

	//parses the final news feed array from JSON Objects into messages
	public ArrayList<Messege> parse(ArrayList<JSONObject> arr) throws JSONException{
		
		ArrayList<Messege> mes = new ArrayList<Messege>();
		JSONObject jo;
		
//		for(int i=0; i<arr.size(); i++){
			
			jo = arr.get(0);
			
			Log.i(TAG, jo.getJSONObject("from").getString("name"));
//			try {
				
				//checking for pic
//					if(jo.has("picture")){
//						mes.add(new PicMessege(jo.getString("name"), jo.getString("message"), jo.getString("created_time"), jo.getString("picture")));
//					}
//				
//			
//					//checking for link
//					else if(jo.has("link")){
//						mes.add(new LinkMessege(jo.getString("name"), jo.getString("message"), jo.getString("created_time"), jo.getString("link")));
//					}
//			
//					//default
//					else{
//						mes.add(new Messege(jo.getString("name"), jo.getString("message"), jo.getString("created_time")));
//					}
//					
//			} catch (JSONException e) {
//				Log.i(TAG,"the JSON message dosen't exist! :(");
//				e.printStackTrace();
//			}
//		}
		
		return mes;
	}
	
	public ArrayList<Messege> getMesseges(){
		return messeges;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
