package fr.pastekweb.hashdroid.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

/**
 * Created by antoine on 30/01/2015.
 */
public class HashTag
{
    /**
     * The hashTag's id
     */
    private int id;

    /**
     * The HashTag's libelle
     */
    private String libelle;

    /**
     * The HashTag's created date
     */
    private Date created;

    /**
     * The list of the last tweets containing the HashTag
     */
    private ArrayList<Tweet> tweets;

    /**
     * Check if in delete mode
     */
    private boolean inDeleteMode;

    /**
     * Check if this hashtag is selected in the delete mode
     */
    private boolean isSelected;

    /**
     * Creates a new instance of HashTag with a random id
     * @param libelle
     * @param create
     */
    public HashTag(String libelle, Date create)
    {
        this(UUID.randomUUID().hashCode(), libelle, create);
    }

    /**
     * Creates a new instance of HashTag with
     * empty list of tweets
     * @param libelle
     */
    public HashTag(int id, String libelle, Date created)
    {
        this(id, libelle, created, new ArrayList<Tweet>());
    }

    /**
     * Creates a new instance of HashTag using the
     * given tweet Collection
     * @param id The Hashtag's id
     * @param libelle The Hashtag's libelle
     * @param created The Hashtag's created date
     * @param tweets The Collection of {@link fr.pastekweb.hashdroid.model.Tweet}
     */
    public HashTag(int id, String libelle, Date created, Collection<Tweet> tweets)
    {
        this.id = id;
        this.libelle = libelle;
        this.created = created;
        this.tweets = new ArrayList<>(tweets);

        this.inDeleteMode = false;
        this.isSelected = false;
    }

    /**
     * Gets the hastag's id
     * @return The hashtag's id
     */
    public int getId(){ return id; }

    /**
     * Gets the HashTag's libelle
     * @return The HashTag's libelle
     */
    public String getLibelle()
    {
        return libelle;
    }

    /**
     * Gets the HashTag's libelle with leading #
     * @return The HashTags's libelle for display
     */
    public String getLibelleToRender() { return "#" + libelle; }

    /**
     * Gets the hashtag created date
     * @return The hashtag created date
     */
    public Date getCreated() { return created; }

    /**
     * Gets the List of Tweets
     * @return The list of Tweets
     */
    public ArrayList<Tweet> getTweets()
    {
        return tweets;
    }

    /**
     * Adds a tweet to the hash tag
     * @param tweet The tweet to add
     */
    public void addTweet(Tweet tweet)
    {
        this.tweets.add(tweet);
    }

    /**
     * Empty the List of Tweets
     */
    public void emptyTweets() { this.tweets = new ArrayList<>(); }

    /**
     * Set the hashtag delete mode status (show/hide a checkbox in the list view)
     * @param state The new status of the hashtag
     */
    public void setInDeleteMode(boolean state) { inDeleteMode = state; }

    /**
     * Get whether the hashtag is in delete mode
     * @return True if the hashtag is in delete mode
     */
    public boolean isInDeleteMode() { return inDeleteMode; }

    /**
     * Set the hashtag selection status
     * @param selected The new selection status of the hashtag
     */
    public void setIsSelected(boolean selected) { isSelected = selected; }

    /**
     * Get whether the hashtag is selected
     * @return True if the hashtag is selected
     */
    public boolean isSelected() { return isSelected; }

    @Override
    public String toString(){ return "#"+libelle; }
}
