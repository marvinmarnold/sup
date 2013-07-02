package com.google.android.apps.sup;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;

import messeges.Messege;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class TwitterNewsActivity extends Activity {
	ListView twitterMessageListView;
	private static final String TAG = "TwitterNewsActivity";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twitter_news);
		Log.v(TAG, "Got to onCreate()");
		
	}

	public ArrayList<Messege> statussToMesseges(List<Status> list) {
		ArrayList<Messege> messeges = new ArrayList<Messege>();
		for (int i = 0; i < list.size(); i++) {
			messeges.add(statusToMessege(list.get(i)));
			Log.v(TAG, messeges.get(i).getText());
		}
		return messeges;

	}

	public Messege statusToMessege(Status status) {
		String posterName = "Unknown";
		try {
			posterName = status.getUser().getName();
		} catch (Exception e) {

		}

		return new Messege(posterName, status.getText(), status.getCreatedAt()
				.toString());

	}

}
