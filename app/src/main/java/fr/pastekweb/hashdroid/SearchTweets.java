package fr.pastekweb.hashdroid;

import android.os.AsyncTask;
import android.util.Log;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Created by SpYd3r on 04/02/15.
 */
public class SearchTweets extends AsyncTask<String, Void, QueryResult> {
    public static QueryResult getLastTweets(String hashtag) {
        Twitter tw = TwitterFactory.getSingleton();

        try {
            return tw.search(new Query("#" + hashtag));
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected QueryResult doInBackground(String... params) {
        return SearchTweets.getLastTweets(params[0]);
    }

    @Override
    protected void onPostExecute(QueryResult queryResult) {
        Log.d("T4J", String.valueOf(queryResult.getCount()));
    }
}
