package fr.pastekweb.hashdroid.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.Date;

import fr.pastekweb.hashdroid.HashtagListFragment;
import fr.pastekweb.hashdroid.R;
import fr.pastekweb.hashdroid.model.HashTag;

/**
 * Created by antoine on 04/02/2015.
 */
public class AddHashTagDialogFragment extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_add_hashtag, null);
        builder.setView(view)
            .setPositiveButton(R.string.add_hashtag, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText hashtagField = (EditText) view.findViewById(R.id.hashtag);
                    HashTag hashTag = new HashTag(hashtagField.getText().toString(), new Date());
                    HashtagListFragment fragment = (HashtagListFragment) getTargetFragment();
                    fragment.addHashTag(hashTag);
                }
            })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AddHashTagDialogFragment.this.getDialog().cancel();
                }
            });
        return builder.create();
    }
}
