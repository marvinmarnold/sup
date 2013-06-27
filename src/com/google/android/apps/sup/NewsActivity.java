package com.google.android.apps.sup;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import com.facebook.*;
import messeges.*;

public class NewsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
		getFeed(GlobalInfo.session);
	}

	public void getFeed(Session session) {
		Bundle params = new Bundle();
		params.putString("limit", "0");

		Request request = new Request(session, "me/home", params,
				HttpMethod.GET, new Request.Callback() {
					public void onCompleted(Response response) {
						ArrayList<JSONObject> vals = buildValsFromResponse(response);
						sort(vals);

					}

				});
		request.executeAsync();
	}

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
			System.out.println("no data key in response");
			e.printStackTrace();
		}

		return arr;
	}

	public ArrayList<JSONObject> sort(ArrayList<JSONObject> arr) {

		for (int i = 0; i < arr.size(); i++) {
			// dell irelevent messages
		}

		return arr;
	}

	public ArrayList<Messege> parse(ArrayList<JSONObject> arr){
		
		ArrayList<Messege> mes = new ArrayList<Messege>();
		JSONObject jo;
		
		for(int i=0; i<arr.size(); i++){
			
			jo = arr.get(i);
			
			try {
				
				//checking for pic
					if(jo.has("picture")){
				
						mes.add(new PicMessege(jo.getString("name"), jo.getString("message"), jo.getString("created_time"), null));
				
					}
				
			
					//checking for link
					else if(jo.has("link")){
				
					}
			
					//default
					else{
						
					}
					
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
