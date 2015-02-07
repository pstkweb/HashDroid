package fr.pastekweb.hashdroid;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

import fr.pastekweb.hashdroid.db.HashTagDB;
import fr.pastekweb.hashdroid.dialog.AskRefreshDialogFragment;
import fr.pastekweb.hashdroid.model.HashTag;
import fr.pastekweb.hashdroid.model.Tweet;

/**
 * A fragment representing a single Hashtag detail screen.
 * This fragment is either contained in a {@link HashtagListActivity}
 * in two-pane mode (on tablets) or a {@link HashtagDetailActivity}
 * on handsets.
 */
public class HashtagDetailFragment extends Fragment
{
    public static final long REFRESH_TIME = 180000;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private HashTag hashTag;

    /**
     * The root view of the fragment
     */
    private View rootView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HashtagDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            int htID = getArguments().getInt(ARG_ITEM_ID);
            HashTagDB htdb = new HashTagDB(getActivity().getApplicationContext());
            hashTag = htdb.retrieve(htID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_hashtag_detail, container, false);

        if (hashTag != null) {
          /*  ((TextView) rootView.findViewById(R.id.hashtag_detail)).setText(hashTag.toString());

            ViewGroup tweetsContainer = ((LinearLayout) rootView.findViewById(R.id.tweets_container));
            for (Tweet tweet : hashTag.getTweets()) {
                View tweetView = inflater.inflate(R.layout.fragment_tweet_detail, tweetsContainer, false);
                ((TextView) tweetView.findViewById(R.id.tweet_author)).setText(tweet.getAuthor());
                ((TextView) tweetView.findViewById(R.id.tweet_text)).setText(tweet.getText());
                ((TextView) tweetView.findViewById(R.id.tweet_date)).setText(tweet.getCreated().toString());
                tweetView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                tweetsContainer.addView(tweetView);
            }*/
            refreshTweetsView();
        }

        // If there's internet connection and tweets are too old
        long lastUpdate = REFRESH_TIME + 1;
        if (hashTag.getTweets().size() > 0) {
            lastUpdate = new Date().getTime() - hashTag.getTweets().get(0).getCreated().getTime();
        }

        if (isNetworkAvailable() && lastUpdate > REFRESH_TIME) {
            AskRefreshDialogFragment dialog = new AskRefreshDialogFragment();
            dialog.setTargetFragment(this, getTargetRequestCode());
            Bundle args = new Bundle();
            dialog.setArguments(args);

            dialog.show(getFragmentManager(), "AskRefresh");
        }

        return rootView;
    }

    /**
     * Detect whether an internet connection exists
     * @return True if the connection is possible
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void refreshTweetsView()
    {
        ((TextView) rootView.findViewById(R.id.hashtag_detail)).setText(hashTag.toString());

        ViewGroup tweetsContainer = ((LinearLayout) rootView.findViewById(R.id.tweets_container));
        for (Tweet tweet : hashTag.getTweets()) {
            View tweetView = getActivity().getLayoutInflater().inflate(R.layout.fragment_tweet_detail, tweetsContainer, false);
            ((TextView) tweetView.findViewById(R.id.tweet_author)).setText(tweet.getAuthor());
            ((TextView) tweetView.findViewById(R.id.tweet_text)).setText(tweet.getText());
            ((TextView) tweetView.findViewById(R.id.tweet_date)).setText(tweet.getCreated().toString());
            tweetView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            tweetsContainer.addView(tweetView);
        }
    }
}
