package fr.pastekweb.hashdroid.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import fr.pastekweb.hashdroid.model.HashTag;
import fr.pastekweb.hashdroid.model.Tweet;

/**
 * Created by antoine on 30/01/2015.
 */
public class HashTagDB
{
    public static final String DB_TABLE = "hd_hashtag";
    public static final String COL_ID = "id";
    public static final String COL_LIBELLE = "libelle";
    public static final String COL_CREATED = "created";


    private HashDroidDBOpenHelper dbOpenHelper;
    private Context context;

    /**
     * Initialize the connexion to the database
     * @param context The context where the database is accessed
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
        queries.add(
            "CREATE TABLE "+DB_TABLE+" ("+
                COL_ID+" int PRIMARY KEY, "+
                COL_LIBELLE+" varchar(100), "+
                COL_CREATED+" TEXT);"
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
        }
        db.close();
    }

    /**
     * Retrieves a hashtag with all the related tweets
     * @param id The hashtag's id
     * @return The instance of the hashtag
     */
    public HashTag retrieve(int id)
    {
        String[] columns = new String[] {COL_ID, COL_LIBELLE, COL_CREATED};
        String where = COL_ID + " = ?";
        String whereArgs[] = {Integer.toString(id)};
        String order = COL_CREATED + " desc";
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(DB_TABLE, columns, where, whereArgs, null, null, order);

        cursor.moveToFirst();
        String libelle = cursor.getString(1);
        Date created = parseDate(cursor.getString(2));
        HashTag hashTag = new HashTag(id, libelle, created);

        String query = "SELECT * FROM "+TweetDB.DB_TABLE+" WHERE "+TweetDB.COL_HASHTAG_ID+"=?";
        cursor = db.rawQuery(query, whereArgs);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            long tid = cursor.getLong(0);
            String a = cursor.getString(1);
            String t = cursor.getString(2);
            Date c = parseDate(cursor.getString(3));
            hashTag.addTweet(new Tweet(tid, a, t, c, hashTag));

            cursor.moveToNext();
        }
        db.close();
        return hashTag;
    }

    /**
     * Retrieves all the hashtag
     * @return The list of hashtags
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
                int id = cursor.getInt(0);
                String libelle = cursor.getString(1);
                Date created = parseDate(cursor.getString(2));
                hashTags.add(new HashTag(id, libelle, created));
            } while(cursor.moveToNext());
        }
        db.close();
        return hashTags;
    }

    /**
     * Clear the list of tweets linked to the hashtag
     * @param hashtag The hashtag to clear
     */
    public void emptyTweets(HashTag hashtag)
    {
        String where = TweetDB.COL_HASHTAG_ID+"=?";
        String whereArgs[] = {String.valueOf(hashtag.getId())};

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.delete(TweetDB.DB_TABLE, where, whereArgs);
        db.close();
    }

    /**
     * Remove a hashtag from the database
     * @param hashtag The hashtag to remove
     */
    public void delete(HashTag hashtag)
    {
        emptyTweets(hashtag);

        String where = HashTagDB.COL_ID+"=?";
        String whereArgs[] = {String.valueOf(hashtag.getId())};

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.delete(HashTagDB.DB_TABLE, where, whereArgs);
        db.close();
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
