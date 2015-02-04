package fr.pastekweb.hashdroid;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;


/**
 * An activity representing a list of Hashtags. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link HashtagDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link HashtagListFragment} and the item details
 * (if present) is a {@link HashtagDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link HashtagListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class HashtagListActivity extends Activity
        implements HashtagListFragment.Callbacks {
    public final static String CONSUMER_TOKEN = "LqZyju1qEpQMVrUvhPkdhU22Z";
    public final static String CONSUMER_SECRET = "aA2JoF0mqY0rUymnVt4omSuom919CjI4SR7mvjIFze6b1Q0LsS";

    public final static String USER_TOKEN = "596818956-T9EdTMkjzN4PPN4vg2KQbJCO4B1mLS2u3kx5l3Bk";
    public final static String USER_SECRET = "LugwQ5dRqZYgG0yHHwjVUvzfW4tKcceHDoskt2T4pmC0l";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hashtag_list);

        if (findViewById(R.id.hashtag_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((HashtagListFragment) getFragmentManager()
                    .findFragmentById(R.id.hashtag_list))
                    .setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.
        Twitter tw = TwitterFactory.getSingleton();
        // App token
        tw = TwitterFactory.getSingleton();
        tw.setOAuthConsumer(CONSUMER_TOKEN, CONSUMER_SECRET);

        // User token
        AccessToken userToken = new AccessToken(USER_TOKEN, USER_SECRET);
        tw.setOAuthAccessToken(userToken);
    }

    /**
     * Callback method from {@link HashtagListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(HashtagDetailFragment.ARG_ITEM_ID, id);
            HashtagDetailFragment fragment = new HashtagDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.hashtag_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, HashtagDetailActivity.class);
            detailIntent.putExtra(HashtagDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
