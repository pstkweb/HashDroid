package fr.pastekweb.hashdroid.tasks;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import fr.pastekweb.hashdroid.HashtagDetailFragment;
import fr.pastekweb.hashdroid.db.HashTagDB;
import fr.pastekweb.hashdroid.db.TweetDB;
import fr.pastekweb.hashdroid.model.HashTag;
import fr.pastekweb.hashdroid.model.Tweet;
import fr.pastekweb.hashdroid.view.TweetAdapter;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Created by Thomas TRIBOULT on 04/02/15.
 */
public class SearchTweets extends AsyncTask<HashTag, Void, QueryResult> {
    /**
     * The application context used by database handlers
     */
    private Context ctx;
    /**
     * The hashtag used to retrieve tweets
     */
    private HashTag ht;
    /**
     * The tweet list adapter
     */
    private TweetAdapter adapter;
    /**
     * The dialog displayed when fetching tweets
     */
    private ProgressDialog dialog;

    public SearchTweets(Activity activity, TweetAdapter tweetsAdapter) {
        ctx = activity.getApplicationContext();
        adapter = tweetsAdapter;
        dialog = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Mise à jour des tweets en cours.");
        dialog.show();
    }

    @Override
    protected QueryResult doInBackground(HashTag... params) {
        ht = params[0];

        return SearchTweets.getLastTweets(ht);
    }

    /**
     * Ask Twitter API for last 15 tweets for the given hashtag
     * @param hashtag The hashtag choosen
     * @return The result containing a List of Status
     */
    public static QueryResult getLastTweets(HashTag hashtag) {
        Twitter tw = TwitterFactory.getSingleton();

        try {
            return tw.search(new Query("#" + hashtag.getLibelle()));
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(QueryResult queryResult) {
        // Dismiss dialog
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        // Empty hashtag tweets list
        HashTagDB htDbHandler = new HashTagDB(ctx);
        htDbHandler.emptyTweets(ht);

        // Create all tweets
        TweetDB tweetDB = new TweetDB(ctx);
        for (twitter4j.Status tweet : queryResult.getTweets()) {
            tweetDB.create(Tweet.factory(tweet, ht));
        }

        // Refresh Tweets list view if provided
        if (adapter != null) {
            adapter.clear();

            for (twitter4j.Status tweet : queryResult.getTweets()) {
                adapter.add(Tweet.factory(tweet, ht));
            }

            adapter.notifyDataSetChanged();
            //listFragment.refreshTweetsView();
        } else {
            Log.d("ListView", "No list adapter provided");
        }
    }
}
