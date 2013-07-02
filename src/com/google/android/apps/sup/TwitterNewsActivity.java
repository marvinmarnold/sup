package com.google.android.apps.sup;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class TwitterNewsActivity extends Activity{
	ListView twitterMessageListView;
	private static final String TAG = "TwitterNewsActivity";
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
		if (GlobalInfo.session.isOpened()) {
//			getFeed(GlobalInfo.session);
		}
		Log.v(TAG, "Got to onCreate()");
	}

}
