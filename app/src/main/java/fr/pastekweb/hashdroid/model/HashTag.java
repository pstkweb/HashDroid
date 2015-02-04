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

    @Override
    public String toString(){ return "#"+libelle; }
}
