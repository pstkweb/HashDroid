package fr.pastekweb.hashdroid.view;

import android.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import fr.pastekweb.hashdroid.R;
import fr.pastekweb.hashdroid.model.Tweet;

/**
 * Created by Thomas TRIBOULT on 08/02/15.
 */
public class TweetAdapter extends ArrayAdapter<Tweet> {
    private List<Tweet> tweets;
    private Fragment fragment;
    private int layoutId;

    public TweetAdapter(Fragment fragment, int resource, List<Tweet> objects) {
        super(fragment.getActivity(), resource, objects);

        layoutId = resource;
        tweets = objects;
        this.fragment = fragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = tweets.get(position);

        if (convertView ==  null) {
            convertView = fragment.getActivity().getLayoutInflater().inflate(layoutId, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.tweet_author)).setText(tweet.getAuthor());
        ((TextView) convertView.findViewById(R.id.tweet_text)).setText(tweet.getText());
        ((TextView) convertView.findViewById(R.id.tweet_date)).setText(tweet.getCreated().toString());

        return convertView;
    }
}
