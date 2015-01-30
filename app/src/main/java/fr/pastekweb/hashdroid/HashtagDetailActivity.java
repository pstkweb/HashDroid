package fr.pastekweb.hashdroid;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import io.oauth.OAuth;
import io.oauth.OAuthCallback;
import io.oauth.OAuthData;
import io.oauth.OAuthRequest;


/**
 * An activity representing a single Hashtag detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link HashtagListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link HashtagDetailFragment}.
 */
public class HashtagDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hashtag_detail);

        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(HashtagDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(HashtagDetailFragment.ARG_ITEM_ID));
            HashtagDetailFragment fragment = new HashtagDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .add(R.id.hashtag_detail_container, fragment)
                    .commit();
        }

        final OAuth oauth = new OAuth(HashtagDetailActivity.this);
        oauth.initialize("xVJYDt7zdriZhhaaBhp1bkYM4vo");

        oauth.popup("twitter", new OAuthCallback() {
            @Override
            public void onFinished(OAuthData data) {
                data.http("/1.1/search/tweets.json?q=HeroesOfTheStorm", new OAuthRequest() {
                    private URL url;
                    private URLConnection con;

                    @Override
                    public void onSetURL(String s) {
                        try {
                            url = new URL(s);
                            con = url.openConnection();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onSetHeader(String s, String s2) {
                        con.addRequestProperty(s, s2);
                    }

                    @Override
                    public void onReady() {
                        try {
                            BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream()));
                            StringBuilder total = new StringBuilder();
                            String line;

                            while ((line = r.readLine()) != null) {
                                total.append(line);
                            }

                            JSONObject res = new JSONObject(total.toString());
                            Toast.makeText(getApplicationContext(), res.getJSONArray("statuses").getJSONObject(0).getString("id"), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(getApplicationContext(), "Err : " + message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, HashtagListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
