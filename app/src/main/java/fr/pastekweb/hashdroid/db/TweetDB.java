package fr.pastekweb.hashdroid.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Date;
import java.util.ArrayList;

import fr.pastekweb.hashdroid.model.HashTag;
import fr.pastekweb.hashdroid.model.Tweet;

/**
 * Created by antoine on 30/01/2015.
 */
public class TweetDB
{
    public static final String DB_TABLE = "hd_tweet";
    public static final String COL_ID = "id";
    public static final String COL_AUTHOR = "author";
    public static final String COL_TEXT = "text";
    public static final String COL_CREATED = "created";
    public static final String COL_HASHTAG_ID = "hashtag_id";

    private HashDroidDBOpenHelper dbOpenHelper;

    /**
     * Initialize the connexion to the database
     * @param context
     */
    public TweetDB(Context context)
    {
        dbOpenHelper = new HashDroidDBOpenHelper(context, HashDroidDBOpenHelper.DB_NAME, null, HashDroidDBOpenHelper.DB_VERSION);
    }

    /**
     * Gets the create tables queries
     * @return Create table queries
     */
    public static ArrayList<String> getCreateTablesQueries()
    {
        ArrayList<String> queries = new ArrayList<>();
        queries.add(
            "CREATE TABLE "+DB_TABLE+" ("+
                COL_ID+" INT PRIMARY KEY, "+
                COL_AUTHOR+" VARCHAR(100), "+
                COL_TEXT+" VARCHAR(140), "+
                COL_CREATED+" TEXT, "+
                COL_HASHTAG_ID+" int, "+
            "FOREIGN KEY ("+COL_HASHTAG_ID+") REFERENCES "+HashTagDB.DB_TABLE+"("+HashTagDB.COL_ID+") ON DELETE CASCADE);"
        );
        return queries;
    }

    /**
     * Gets drop tables queries
     * @return Drop tables queries
     */
    public static ArrayList<String> getDropTablesQueries()
    {
        ArrayList<String> queries = new ArrayList<>();
        queries.add("DROP TABLE IF EXISTS "+DB_TABLE);
        return queries;
    }

    /**
     * Inserts a tweet into the database
     * @param tweet The tweet to add
     */
    public void create(Tweet tweet)
    {
        ContentValues values = new ContentValues();
        values.put(COL_ID, tweet.getId());
        values.put(COL_AUTHOR, tweet.getAuthor());
        values.put(COL_TEXT, tweet.getText());
        values.put(COL_CREATED, tweet.getCreated().toString());
        values.put(COL_HASHTAG_ID, tweet.getHashTag().getId());

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.insert(DB_TABLE, null, values);
        db.close();
    }

    /**
     * Retrieves a tweet by its id from the database
     * @param id The tweet's id
     * @return The tweet
     */
    public Tweet retrieve(int id)
    {
        String query = "SELECT * FROM "+DB_TABLE+" tw"
            +" INNER JOIN "+HashTagDB.DB_TABLE+" ht ON tw."+COL_HASHTAG_ID+"=tw."+HashTagDB.COL_ID
            +" WHERE tw."+COL_ID+"=?";

        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        String whereArgs[] = {Integer.toString(id)};
        Cursor cursor = db.rawQuery(query, whereArgs);
        cursor.moveToFirst();

        String author = cursor.getString(1);
        String text = cursor.getString(2);
        Date created = Date.valueOf(cursor.getString(3));
        HashTag hashTag = new HashTag(
            cursor.getInt(4),
            cursor.getString(5),
            Date.valueOf(cursor.getString(6))
        );

        db.close();
        return new Tweet(id, author, text, created, hashTag);
    }
}
