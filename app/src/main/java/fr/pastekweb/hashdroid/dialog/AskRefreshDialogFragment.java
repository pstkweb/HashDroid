package fr.pastekweb.hashdroid.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import fr.pastekweb.hashdroid.HashtagDetailFragment;
import fr.pastekweb.hashdroid.R;
import fr.pastekweb.hashdroid.db.HashTagDB;
import fr.pastekweb.hashdroid.model.HashTag;
import fr.pastekweb.hashdroid.tasks.SearchTweets;
import fr.pastekweb.hashdroid.view.TweetAdapter;

/**
 * Created by Thomas TRIBOULT on 04/02/15.
 */
public class AskRefreshDialogFragment extends DialogFragment {
    private int hashId;
    public TweetAdapter listAdapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        hashId = getArguments().getInt(HashtagDetailFragment.ARG_ITEM_ID);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.ask_refresh)
            .setTitle(R.string.refresh_title)
            .setPositiveButton(R.string.refresh, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Retrieve the HashTag from database
                    HashTagDB hashtagDbHandler = new HashTagDB(getActivity().getApplicationContext());
                    HashTag ht = hashtagDbHandler.retrieve(hashId);

                    // Search for that hashtag tweets with Twitter API
                    SearchTweets api = new SearchTweets(getActivity(), listAdapter);
                    api.execute(ht);
                }
            })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

        return builder.create();
    }
}
