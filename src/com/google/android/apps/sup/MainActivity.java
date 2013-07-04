package com.google.android.apps.sup;

import java.util.Arrays;

import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

public class MainActivity extends FragmentActivity {

	// FB vars
	private UiLifecycleHelper fbUiHelper;
	private static final String FACEBOOK_TAG = "Facebook log";

	// Twitter vars
	private static final String TWITTER_TAG = "Twitter log";
	private ImageButton twitterLoginButton;

	private static RequestToken requestToken;
	private static SharedPreferences mSharedPreferences;

	// Google vars
	ImageButton google;
	ImageButton sup;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		createFacebook(savedInstanceState);
		createTwitter();
		addListenerOnButton();
	}

	private void createFacebook(Bundle savedInstanceState) {
		LoginButton fbAuthButton = (LoginButton) findViewById(R.id.fbLoginButton);
		fbAuthButton.setBackgroundResource(R.drawable.face);
		fbAuthButton.setReadPermissions(Arrays.asList("user_likes",
				"user_status", "read_stream"));

		fbUiHelper = new UiLifecycleHelper(MainActivity.this, callback);
		fbUiHelper.onCreate(savedInstanceState);
	}

	private void createTwitter() {
		mSharedPreferences = getSharedPreferences(
				GlobalInfo.TWITTER_PREFERENCE_NAME, MODE_PRIVATE);
		twitterLoginButton = (ImageButton) findViewById(R.id.twitterLoginButton);
		twitterLoginButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isConnectedTwitter()) {
					disconnectTwitter();
				} else {
					askOAuth();
				}
			}

		});

		/**
		 * Handle OAuth Callback
		 */
		Uri uri = getIntent().getData();
		if (uri != null
				&& uri.toString().startsWith(GlobalInfo.TWITTER_CALLBACK_URL)) {
			String verifier = uri
					.getQueryParameter(GlobalInfo.TWITTER_IEXTRA_OAUTH_VERIFIER);
			try {
				AccessToken accessToken = GlobalInfo.getTwitter()
						.getOAuthAccessToken(requestToken, verifier);
				Editor e = mSharedPreferences.edit();
				e.putString(GlobalInfo.TWITTER_PREF_KEY_TOKEN,
						accessToken.getToken());
				e.putString(GlobalInfo.TWITTER_PREF_KEY_SECRET,
						accessToken.getTokenSecret());
				e.commit();
			} catch (Exception e) {
				Log.e(TWITTER_TAG, e.getMessage());
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			try {
				GlobalInfo.session = session;
				// Intent nextScreen = new Intent(MainActivity.this,
				// NewsActivity.class);
				// startActivity(nextScreen);
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
			}

			Log.i(FACEBOOK_TAG, "Logged in...");
		} else if (state.isClosed()) {
			Log.i(FACEBOOK_TAG, "Logged out...");
		}
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	@Override
	public void onResume() {
		Log.v(TWITTER_TAG, "onResume started");
		super.onResume();

		// FACEBOOK
		// For scenarios where the main activity is launched and user
		// session is not null, the session state change notification
		// may not be triggered. Trigger it if it's open/closed.
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}// startActivity(nextScreen);
		fbUiHelper.onResume();

		// TWITTER
		if (isConnectedTwitter()) {
			String oauthAccessToken = mSharedPreferences.getString(
					GlobalInfo.TWITTER_PREF_KEY_TOKEN, "");
			String oAuthAccessTokenSecret = mSharedPreferences.getString(
					GlobalInfo.TWITTER_PREF_KEY_SECRET, "");

			ConfigurationBuilder confbuilder = new ConfigurationBuilder();
			Configuration conf = confbuilder
					.setOAuthConsumerKey(GlobalInfo.TWITTER_CONSUMER_KEY)
					.setOAuthConsumerSecret(GlobalInfo.TWITTER_CONSUMER_SECRET)
					.setOAuthAccessToken(oauthAccessToken)
					.setOAuthAccessTokenSecret(oAuthAccessTokenSecret).build();
			GlobalInfo.setTwitter(new TwitterFactory(conf).getInstance());
			// Intent nextScreen = new Intent(MainActivity.this,
			// NewsActivity.class);
			// startActivity(nextScreen);
		}
	}

	// TODO
	// Implement this method so that it creates an intent to a new activity that
	// will
	// use the twitter object to get the timeline

	private void askOAuth() {
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder
				.setOAuthConsumerKey(GlobalInfo.TWITTER_CONSUMER_KEY);
		configurationBuilder
				.setOAuthConsumerSecret(GlobalInfo.TWITTER_CONSUMER_SECRET);
		Configuration configuration = configurationBuilder.build();
		GlobalInfo.setTwitter(new TwitterFactory(configuration).getInstance());

		try {
			requestToken = GlobalInfo.getTwitter().getOAuthRequestToken(
					GlobalInfo.TWITTER_CALLBACK_URL);
			Toast.makeText(this, "Please authorize this app!",
					Toast.LENGTH_LONG).show();
			this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
					.parse(requestToken.getAuthenticationURL())));
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	/**
	 * check if the Twitter account is authorized
	 * 
	 * @return
	 */
	public static boolean isConnectedTwitter() {
		return mSharedPreferences.getString(GlobalInfo.TWITTER_PREF_KEY_TOKEN,
				null) != null;
	}

	/**
	 * Remove Token, Secret from preferences
	 */
	private void disconnectTwitter() {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.remove(GlobalInfo.TWITTER_PREF_KEY_TOKEN);
		editor.remove(GlobalInfo.TWITTER_PREF_KEY_SECRET);

		editor.commit();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		fbUiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		fbUiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		fbUiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		fbUiHelper.onSaveInstanceState(outState);

	}

	public void addListenerOnButton() {

		google = (ImageButton) findViewById(R.id.google);
		sup = (ImageButton) findViewById(R.id.sup);

		google.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Toast.makeText(MainActivity.this, "Coming soon.",
						Toast.LENGTH_SHORT).show();
				// Intent nextScreen = new Intent(MainActivity.this,
				// NewsActivity.class);
				// startActivity(nextScreen);

			}

		});
		sup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent nextScreen = new Intent(MainActivity.this,
						NewsActivity.class);
				startActivity(nextScreen);

			}

		});

	}

}
