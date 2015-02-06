package fr.pastekweb.hashdroid.model;

import java.util.Date;

import twitter4j.Status;

/**
 * Created by antoine on 30/01/2015.
 */
public class Tweet
{
    /**
     * The id of the Tweet
     */
    private long id;

    /**
     * The author of the tweet
     */
    private String author;

    /**
     * The text of the tweet
     */
    private String text;

    /**
     * The created date of the tweet
     */
    private Date created;

    /**
     * The hashtag which the tweet belongs to
     */
    private HashTag hashTag;

    /**
     * Creates a new instance of Tweet
     * @param id The id of the Tweet
     * @param author The author of the Tweet
     * @param text The text of the tweet
     * @param created The created date of the tweet
     * @param hashTag The hashtag which the tweet belongs to
     */
    public Tweet(long id, String author, String text, Date created, HashTag hashTag)
    {
        this.id = id;
        this.author = author;
        this.text = text;
        this.created = created;
        this.hashTag = hashTag;
    }

    public static Tweet factory(Status tweet, HashTag hashTag) {
        return new Tweet(
            tweet.getId(),
            tweet.getUser().getScreenName(),
            tweet.getText(),
            tweet.getCreatedAt(),
            hashTag
        );
    }

    /**
     * Gets the id of the tweet
     * @return The id
     */
    public long getId() { return id; }

    /**
     * Gets the author of the Tweet
     * @return The author
     */
    public String getAuthor()
    {
        return author;
    }

    /**
     * Gets the text of the Tweet
     * @return The text
     */
    public String getText()
    {
        return text;
    }

    /**
     * Gets the created date of the Tweet
     * @return The created date of the tweet
     */
    public Date getCreated()
    {
        return created;
    }

    /**
     * gets the hashtag which the Tweet belongs to
     * @return
     */
    public HashTag getHashTag() { return hashTag; }
}
