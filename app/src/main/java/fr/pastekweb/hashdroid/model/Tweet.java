package fr.pastekweb.hashdroid.model;

import java.util.Date;

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
     * Creates a new instance of Tweet
     * @param id The id of the Tweet
     * @param author The author of the Tweet
     * @param text The text of the tweet
     * @param created The created date of the tweet
     */
    public Tweet(long id, String author, String text, Date created)
    {
        this.id = id;
        this.author = author;
        this.text = text;
        this.created = created;
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
}
