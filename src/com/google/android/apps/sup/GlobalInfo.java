package com.google.android.apps.sup;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;

import com.facebook.Session;

public class GlobalInfo {

	public static Session session;
	private static Twitter twitter;
	private static List<Status> statuses;
	private static boolean gotFromFacebook = false;
	private static boolean gotFromTwitter = false;

	static String TWITTER_CONSUMER_KEY = "XIKzBJCgmKKs0rVF9Sp7ow";
	static String TWITTER_CONSUMER_SECRET = "v3Uv7BpxZJl7N7rQTxM1hmm4xH8DpQsc6Cs6WOiEy0";

	static String TWITTER_PREFERENCE_NAME = "twitter_oauth";
	static final String TWITTER_PREF_KEY_SECRET = "oauth_token_secret";
	static final String TWITTER_PREF_KEY_TOKEN = "oauth_token";

	static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";

	static final String TWITTER_IEXTRA_AUTH_URL = "auth_url";
	static final String TWITTER_IEXTRA_OAUTH_VERIFIER = "oauth_verifier";
	static final String TWITTER_IEXTRA_OAUTH_TOKEN = "oauth_token";

	
	
	
	public static void setGotFromFacebook(boolean gotFromFacebook) {
		GlobalInfo.gotFromFacebook = gotFromFacebook;
	}

	public static void setGotFromTwitter(boolean gotFromTwitter) {
		GlobalInfo.gotFromTwitter = gotFromTwitter;
	}
		public static boolean isGotFromFacebook() {
		return gotFromFacebook;
	}

	public static boolean isGotFromTwitter() {
		return gotFromTwitter;
	}
	public static Twitter getTwitter() {
		return twitter;
	}

	public static void setTwitter(Twitter twitter) {
		GlobalInfo.twitter = twitter;
	}
	public static List<Status> getStatuses() {
		return statuses;
	}

	public static void setStatuses(List<Status> statuses) {
		GlobalInfo.statuses = statuses;
	}

}
