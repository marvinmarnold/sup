package com.google.android.apps.sup;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import messeges.LinkMessege;
import messeges.Messege;
import messeges.PicMessege;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Status;
import twitter4j.TwitterException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewsActivity extends Activity {
	ListView messageListView;

	// Listview Adapter
	LazyAdapter arrayAdapter;

	// Search EditText
	EditText inputSearch;

	ArrayList<String> MessageList;
	ImageButton back;
	ImageButton sup2;

	private ArrayList<Messege> messeges = new ArrayList<Messege>();
	private static final String TAG = "NewsActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
		addListenerOnButton();
	}

	protected void onResume() {
		Log.v(TAG, "resumed");
		super.onResume();
		messeges.clear();
		if (/* !GlobalInfo.isGotFromFacebook()&& */GlobalInfo.session != null
				&& GlobalInfo.session.isOpened()) {
			/* GlobalInfo.setGotFromFacebook(true); */
			getFacebookFeed(GlobalInfo.session);
		}
		if (/* !GlobalInfo.isGotFromTwitter() && */MainActivity
				.isConnectedTwitter()) {
			/* GlobalInfo.setGotFromTwitter(true); */
			buildTwitterFeed();
			statussToMesseges(GlobalInfo.getStatuses());
		}

	}

	// reutrns the final news feed
	public void getFacebookFeed(Session session) {
		Log.i(TAG, "started getting feed from fb");
		Bundle params = new Bundle();
		params.putString("limit", "0");

		Request request = new Request(session, "me/home", params,
				HttpMethod.GET, new Request.Callback() {
					public void onCompleted(Response response) {
						Log.i(TAG, "got response");
						ArrayList<JSONObject> JSONmessages = buildValsFromResponse(response);
						sort(JSONmessages);
						try {
							parseFacebook(JSONmessages);
							getFeed();

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				});
		request.executeAsync();
	}

	public void getFeed() {
		Log.i(TAG, "'getFeed()' started");
		messageListView = (ListView) findViewById(R.id.listViewMessages);
		inputSearch = (EditText) findViewById(R.id.inputSearch);
		MessageList = new ArrayList<String>();
		ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < messeges.size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(GlobalInfo.KEY_ID, "ID");
			map.put(GlobalInfo.KEY_TITLE, messeges.get(i).getPosterName());
			map.put(GlobalInfo.KEY_ARTIST, messeges.get(i).getText());
			if(messeges.get(i).getSource()=="twitter"){
				map.put(GlobalInfo.KEY_DURATION, twitterHumanFriendlyDate(messeges.get(i)
					.getTime().toString()));
			}else{
				map.put(GlobalInfo.KEY_DURATION,facebookNotHumanFriendlyDate(messeges.get(i)
						.getTime().toString()));
			}
			
			map.put(GlobalInfo.KEY_THUMB_URL, messeges.get(i)
					.getProfilePicUrl());
			Log.i(TAG, twitterHumanFriendlyDate(messeges.get(i)
					.getTime().toString()));
			// adding HashList to ArrayList
			songsList.add(map);
		}

		// Create The Adapter with passing ArrayList as 3rd
		// parameter
		arrayAdapter = new LazyAdapter(NewsActivity.this, songsList);
		messageListView.setAdapter(arrayAdapter);

		// Set The Adapter
		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				NewsActivity.this.arrayAdapter.getFilter().filter(cs);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}
		});
	}

	// argument position gives the index of item which is clicked
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		// Search EditText
	}

	public void sanityCheck(JSONObject object) {
		Log.i(TAG, object.names().toString());
		Log.i(TAG, getTextByJsonobject(object));
	}

	// reutrns an array of JSON objects, each elemnt is a message
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

	// dells all irrelevent messages
	public ArrayList<JSONObject> sort(ArrayList<JSONObject> arr) {

		for (int i = 0; i < arr.size(); i++) {
			// dell irelevent messages
		}

		return arr;
	}

	// parses the final news feed array from JSON Objects into messages
	public void parseFacebook(ArrayList<JSONObject> arr) throws JSONException {

		JSONObject jo;

		for (int i = 0; i < arr.size(); i++) {

			jo = arr.get(i);
			sanityCheck(jo);
			Log.i(TAG, "Got to the parsing");
			try {

				// checking for pic
				if (jo.has("picture")) {

					messeges.add(new PicMessege(jo.getJSONObject("from")
							.getString("name"), getTextByJsonobject(jo), jo
							.getString("created_time"),
							"http://graph.facebook.com/"
									+ jo.getJSONObject("from").getString("id")
									+ "/picture", "facebook", jo
									.getString("picture")));
				}

				// checking for link
				else if (jo.has("link")) {
					messeges.add(new LinkMessege(jo.getJSONObject("from")
							.getString("name"), getTextByJsonobject(jo), jo
							.getString("created_time"),
							"http://graph.facebook.com/"
									+ jo.getJSONObject("from").getString("id")
									+ "/picture", "facebook", jo
									.getString("link")));

				}

				// default
				else {
					messeges.add(new Messege(jo.getJSONObject("from")
							.getString("name"), jo.getString("message"), jo
							.getString("created_time"),

					"http://graph.facebook.com/"
							+ jo.getJSONObject("from").getString("id")
							+ "/picture", "facebook"));
				}

				Log.i(TAG,
						"http://graph.facebook.com/"
								+ jo.getJSONObject("from").getString("id")
								+ "/picture");

			} catch (JSONException e) {
				Log.i(TAG, "the JSON message dosen't exist! :(");
				e.printStackTrace();
			}
		}

	}

	public ArrayList<Messege> getMesseges() {
		return messeges;
	}

	public String getTextByJsonobject(JSONObject object) {
		try {
			return object.getString("message");
		} catch (JSONException e) {
			try {
				return object.getString("story");
			} catch (JSONException E) {
				return "*Ad(Like us on facebook http://tinyurl.com/supfbad";
			}
		}
	}

	private void buildTwitterFeed() {
		try {
			GlobalInfo.setStatuses(GlobalInfo.getTwitter().getHomeTimeline());

		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			Log.v(TAG, "no messages");
		}
	}

	public void statussToMesseges(List<Status> list) {

		for (int i = 0; i < list.size(); i++) {
			messeges.add(statusToMessege(list.get(i)));
		}
		getFeed();

	}

	public Messege statusToMessege(Status status) {
		String posterName = "Unknown";
		String profilePicUrl = "http://1.bp.blogspot.com/-Yenc3Vmbm6M/TkxlNb0qnWI/AAAAAAAAANs/pUrQP2H7Ql8/s1600/funny-different-facebook-profile-pic-chicken.jpg";
		try {
			posterName = status.getUser().getName();
			profilePicUrl = status.getUser().getProfileImageURL();
			Log.v(TAG, profilePicUrl);
		} catch (Exception e) {

		}

		return new Messege(posterName, status.getText(), status.getCreatedAt()
				.toString(), profilePicUrl, "twitter");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;

	}

	public void addListenerOnButton() {

		sup2 = (ImageButton) findViewById(R.id.supNewsButton);

		sup2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent nextScreen = new Intent(NewsActivity.this,
						MainActivity.class);
				startActivity(nextScreen);

			}

		});

	}

	public static String twitterHumanFriendlyDate(String dateStr) {
		// parse Twitter date
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
		dateFormat.setLenient(false);
		Date created = null;
		try {
			created = (Date) dateFormat.parse(dateStr);
			// created=(Date) dateFormat.parse(dateStr, null);
		} catch (Exception e) {
			return "Something";
		}

		// today
		Date today = new Date();

		// how much time since (ms)
		Long duration = today.getTime() - created.getTime();

		int second = 1000;
		int minute = second * 60;
		int hour = minute * 60;
		int day = hour * 24;

		if (duration < second * 7) {
			return "right now";
		}

		if (duration < minute) {
			int n = (int) Math.floor(duration / second);
			return n + " seconds ago";
		}

		if (duration < minute * 2) {
			return "about 1 minute ago";
		}

		if (duration < hour) {
			int n = (int) Math.floor(duration / minute);
			return n + " minutes ago";
		}

		if (duration < hour * 2) {
			return "about 1 hour ago";
		}

		if (duration < day) {
			int n = (int) Math.floor(duration / hour);
			return n + " hours ago";
		}
		if (duration > day && duration < day * 2) {
			return "yesterday";
		}

		if (duration < day * 365) {
			int n = (int) Math.floor(duration / day);
			return n + " days ago";
		} else {
			return "over a year ago";
		}
	}

public static String facebookNotHumanFriendlyDate(String dateStr) {
	// parse Twitter date
	SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
	dateFormat.setLenient(false);
	Date created = null;
	try {
		created = (Date) dateFormat.parse(dateStr);
		// created=(Date) dateFormat.parse(dateStr, null);
	} catch (Exception e) {
		return "Something";
	}

	// today
	Date today = new Date();

	// how much time since (ms)
	Long duration = today.getTime() - created.getTime();

	int second = 1000;
	int minute = second * 60;
	int hour = minute * 60;
	int day = hour * 24;

	if (duration < second * 7) {
		return "right now";
	}

	if (duration < minute) {
		int n = (int) Math.floor(duration / second);
		return n + " seconds ago";
	}

	if (duration < minute * 2) {
		return "about 1 minute ago";
	}

	if (duration < hour) {
		int n = (int) Math.floor(duration / minute);
		return n + " minutes ago";
	}

	if (duration < hour * 2) {
		return "about 1 hour ago";
	}

	if (duration < day) {
		int n = (int) Math.floor(duration / hour);
		return n + " hours ago";
	}
	if (duration > day && duration < day * 2) {
		return "yesterday";
	}

	if (duration < day * 365) {
		int n = (int) Math.floor(duration / day);
		return n + " days ago";
	} else {
		return "over a year ago";
	}
}

}

