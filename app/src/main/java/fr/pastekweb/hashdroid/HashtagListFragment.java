package fr.pastekweb.hashdroid;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;

import fr.pastekweb.hashdroid.db.HashTagDB;
import fr.pastekweb.hashdroid.dialog.AddHashTagDialogFragment;
import fr.pastekweb.hashdroid.model.HashTag;
import fr.pastekweb.hashdroid.tasks.SearchTweets;
import fr.pastekweb.hashdroid.view.HashtagAdapter;

/**
 * A list fragment representing a list of Hashtags. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link HashtagDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class HashtagListFragment extends ListFragment {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks listActionCallbacks = sHashTagCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * The state of the edit mode
     */
    private boolean isInDeleteMode = false;

    /**
     * The hashtags list adapter
     */
    private HashtagAdapter hashTagsAdapter;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(int id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sHashTagCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(int htID) {

        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HashtagListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        HashTagDB hdb = new HashTagDB(getActivity().getApplicationContext());
        hashTagsAdapter = new HashtagAdapter(this, R.layout.hashtag_list_item, hdb.retrieveAll());
        setListAdapter(hashTagsAdapter);

        // Listen to delete mode buttons
        /*View buttonsLayout = getActivity().findViewById(R.id.buttons);
        buttonsLayout.findViewById(R.id.hashtag_delete_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDeleteMode();
            }
        });*/
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_hashtag:
                FragmentManager fm = getFragmentManager();
                AddHashTagDialogFragment addDialog = new AddHashTagDialogFragment();
                addDialog.setTargetFragment(this, getTargetRequestCode());
                addDialog.show(fm, "Add_hashtag");
                return true;
            case R.id.delete_hashtag:
                toggleDeleteMode();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


        @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        listActionCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        listActionCallbacks = sHashTagCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        listActionCallbacks.onItemSelected(hashTagsAdapter.getItem(position).getId());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Add a hashtag to the list
     * @param hashTag The hashtag to append to the list
     */
    public void addHashTag(HashTag hashTag)
    {
        HashTagDB hashTagDB = new HashTagDB(getActivity().getApplicationContext());
        hashTagDB.create(hashTag);

        // Search for that hashtag tweets with Twitter API if network connection is up
        if (isNetworkAvailable()) {
            SearchTweets api = new SearchTweets(getActivity(), null);
            api.execute(hashTag);
        }

        hashTagsAdapter.add(hashTag);
        hashTagsAdapter.notifyDataSetChanged();
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
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

    /**
     * Toggle the hashtags delete mode
     */
    public void toggleDeleteMode() {
        isInDeleteMode = !isInDeleteMode;

        // Show/hide checkboxes
        hashTagsAdapter.setDeleteMode(isInDeleteMode);

        // Display/hide confirm buttons
        if (isInDeleteMode) {
            getActivity().findViewById(R.id.buttons).setVisibility(View.VISIBLE);
        } else {
            getActivity().findViewById(R.id.buttons).setVisibility(View.GONE);
        }
    }

    public HashtagAdapter getAdapter() {
        return hashTagsAdapter;
    }
}
