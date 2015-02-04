package fr.pastekweb.hashdroid.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import fr.pastekweb.hashdroid.model.HashTag;
import fr.pastekweb.hashdroid.model.Tweet;

/**
 * Created by antoine on 30/01/2015.
 */
public class HashTagDB
{
    public static final String DB_TABLE = "hd_hashtag";
    public static final String DB_TABLE_TWT_HT = "hd_twt_ht";
    public static final String COL_ID = "id";
    public static final String COL_LIBELLE = "libelle";
    public static final String COL_CREATED = "created";
    public static final String COL_HASHTAG_ID = "hashtag_id";
    public static final String COL_TWEET_ID = "tweet_id";


    private HashDroidDBOpenHelper dbOpenHelper;
    private Context context;

    /**
     * Initialize the connexion to the database
     * @param context
     */
    public HashTagDB(Context context)
    {
        this.context = context;
        dbOpenHelper = new HashDroidDBOpenHelper(context, HashDroidDBOpenHelper.DB_NAME, null, HashDroidDBOpenHelper.DB_VERSION);
    }

    /**
     * Gets create tables queries
     * @return Create tables queries
     */
    public static ArrayList<String> getCreateTablesQueries()
    {
        ArrayList<String> queries = new ArrayList<>();
        queries.add("CREATE TABLE "+DB_TABLE+" ("+COL_ID+" int PRIMARY KEY, "+COL_LIBELLE+" varchar(100), "+COL_CREATED+" TEXT);");
        queries.add("CREATE TABLE "+DB_TABLE_TWT_HT+" ("+COL_HASHTAG_ID+" int, "+COL_TWEET_ID+" int," +
                " PRIMARY KEY ("+COL_HASHTAG_ID+", "+COL_TWEET_ID+")" +
                " FOREIGN KEY ("+COL_HASHTAG_ID+") REFERENCES "+DB_TABLE+"("+COL_ID+") ON DELETE CASCADE" +
                " FOREIGN KEY ("+COL_TWEET_ID+") REFERENCES "+TweetDB.DB_TABLE+"("+TweetDB.COL_ID+") ON DELETE CASCADE);");
        return queries;
    }

    /**
     * Gets drop tables queries
     * @return Drop tables queries
     */
    public static ArrayList<String> getDropTablesQueries()
    {
        ArrayList<String> queries = new ArrayList<>();
        queries.add("DROP TABLE IF EXISTS "+DB_TABLE_TWT_HT+";");
        queries.add("DROP TABLE IF EXISTS"+DB_TABLE);
        return queries;
    }

    /**
     * Inserts a hashtag into the database
     * @param hashTag The hashtag to add
     */
    public void create(HashTag hashTag)
    {
        ContentValues values = new ContentValues();
        values.put(COL_ID, hashTag.getId());
        values.put(COL_LIBELLE, hashTag.getLibelle());
        values.put(COL_CREATED, hashTag.getCreated().toString());

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.insert(DB_TABLE, null, values);

        TweetDB tweetDB = new TweetDB(context);
        for (Tweet tweet : hashTag.getTweets()) {
            tweetDB.create(tweet);
            values = new ContentValues();
            values.put(COL_TWEET_ID, tweet.getId());
            values.put(COL_HASHTAG_ID, hashTag.getId());
            db.insert(DB_TABLE_TWT_HT, null, values);
        }
    }

    /**
     * Retrieves a hashtag with all the related tweets
     * @param id The hashtag's id
     * @return The instance of the hashtag
     */
    public HashTag retrieve(int id)
    {
        String[] columns = new String[] {COL_ID, COL_LIBELLE, COL_CREATED};
        String where = COL_ID + " = \"%?%\"";
        String whereArgs[] = {Integer.toString(id)};
        String order = COL_CREATED + " desc";
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(DB_TABLE, columns, where, whereArgs, null, null, order);

        String libelle = cursor.getString(cursor.getColumnIndex(COL_LIBELLE));
        Date created = parseDate(cursor.getString(cursor.getColumnIndex(COL_CREATED)));
        HashTag hashTag = new HashTag(id, libelle, created);

        String query = "SELECT * FROM "+DB_TABLE_TWT_HT+" twht"
                     +" INNER JOIN "+TweetDB.DB_TABLE+" tw ON twht."+COL_TWEET_ID+"=tw."+TweetDB.COL_ID
                     +" WHERE twht."+COL_HASHTAG_ID+"=?";
        cursor = db.rawQuery(query, whereArgs);
        while (!cursor.isAfterLast()) {
            long tid = cursor.getLong(0);
            String a = cursor.getString(1);
            String t = cursor.getString(2);
            Date c = parseDate(cursor.getString(2));
            hashTag.addTweet(new Tweet(tid, a, t, c));

            cursor.moveToNext();
        }

        return hashTag;
    }

    /**
     * Retrieves all the hashtag
     * @return
     */
    public ArrayList<HashTag> retrieveAll()
    {
        String[] columns = new String[] {COL_ID, COL_LIBELLE, COL_CREATED};

        String order = COL_CREATED+" desc";
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(DB_TABLE, columns, null, null, null, null, order);

        ArrayList<HashTag> hashTags = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Log.d("DBDebug", "Changed");
                int id = cursor.getInt(0);
                String libelle = cursor.getString(1);
                Date created = parseDate(cursor.getString(2));
                //Date created = new Date();
                hashTags.add(new HashTag(id, libelle, created));
            } while(cursor.moveToNext());
        }
        return hashTags;
    }

    /**
     * Clear the list of tweets linked to the hashtag
     * @param hashtag
     */
    public void emptyTweets(HashTag hashtag)
    {
        String where = COL_HASHTAG_ID+"=?";
        String whereArgs[] = {String.valueOf(hashtag.getId())};

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.delete(DB_TABLE_TWT_HT, where, whereArgs);
    }

    private Date parseDate(String textDate)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            return dateFormat.parse(textDate);
        } catch(ParseException e) {
            return new Date();
        }
    }
}
