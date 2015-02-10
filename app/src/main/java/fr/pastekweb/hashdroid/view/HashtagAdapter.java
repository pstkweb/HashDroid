package fr.pastekweb.hashdroid.view;

import android.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import fr.pastekweb.hashdroid.HashtagListFragment;
import fr.pastekweb.hashdroid.R;
import fr.pastekweb.hashdroid.model.HashTag;

/**
 * Created by Thomas TRIBOULT on 10/02/15.
 */
public class HashtagAdapter extends ArrayAdapter<HashTag> {
    private List<HashTag> hashtags;
    private Fragment fragment;
    private int layoutId;

    public HashtagAdapter(Fragment fragment, int resource, List<HashTag> objects) {
        super(fragment.getActivity(), resource, objects);

        this.fragment = fragment;
        layoutId = resource;
        hashtags = objects;
    }

    public void setDeleteMode(boolean state) {
        for (HashTag ht : hashtags) {
            ht.setInDeleteMode(state);
        }

        notifyDataSetChanged();
    }

    public List<HashTag> getList() {
        return hashtags;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final HashTag ht = hashtags.get(position);

        if (convertView == null) {
            convertView = fragment.getActivity().getLayoutInflater().inflate(layoutId, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.hashtag_name)).setText(ht.getLibelleToRender());

        final CheckBox cb = (CheckBox) convertView.findViewById(R.id.hashtag_checkbox);
        convertView.setTag(ht);
        cb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ht.setIsSelected(cb.isChecked());
            }
        });
        if (ht.isInDeleteMode()) {
            cb.setVisibility(View.VISIBLE);
        } else {
            cb.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }
}
