package fr.pastekweb.hashdroid.tasks;

import android.content.Context;
import android.os.AsyncTask;

import fr.pastekweb.hashdroid.db.HashTagDB;
import fr.pastekweb.hashdroid.db.TweetDB;
import fr.pastekweb.hashdroid.model.HashTag;
import fr.pastekweb.hashdroid.model.Tweet;
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

    public SearchTweets(Context context) {
        ctx = context;
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
    protected QueryResult doInBackground(HashTag... params) {
        ht = params[0];

        return SearchTweets.getLastTweets(ht);
    }

    @Override
    protected void onPostExecute(QueryResult queryResult) {
        // Empty hashtag tweets list
        HashTagDB htDbHandler = new HashTagDB(ctx);
        htDbHandler.emptyTweets(ht);

        // Create all tweets
        TweetDB tweetDB = new TweetDB(ctx);
        for (twitter4j.Status tweet : queryResult.getTweets()) {
            tweetDB.create(Tweet.factory(tweet, ht));
        }
    }
}
