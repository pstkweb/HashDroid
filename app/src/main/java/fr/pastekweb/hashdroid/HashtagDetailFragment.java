package fr.pastekweb.hashdroid;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import fr.pastekweb.hashdroid.db.HashTagDB;
import fr.pastekweb.hashdroid.dummy.DummyContent;
import fr.pastekweb.hashdroid.model.HashTag;

/**
 * A fragment representing a single Hashtag detail screen.
 * This fragment is either contained in a {@link HashtagListActivity}
 * in two-pane mode (on tablets) or a {@link HashtagDetailActivity}
 * on handsets.
 */
public class HashtagDetailFragment extends Fragment {
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
        View rootView = inflater.inflate(R.layout.fragment_hashtag_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (hashTag != null) {
            ((TextView) rootView.findViewById(R.id.hashtag_detail)).setText(hashTag.toString());
        }

        return rootView;
    }
}
