package fr.pastekweb.hashdroid.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import fr.pastekweb.hashdroid.model.HashTag;

/**
 * Created by antoine on 30/01/2015.
 */
public class HashDroidDBOpenHelper extends SQLiteOpenHelper
{
    public static final String DB_NAME;
    public static final int DB_VERSION;
    private static final ArrayList<String> DB_CREATE;
    private static final ArrayList<String> DB_UPGRADE;

    static
    {
        DB_NAME = "hashdroid_dev";
        DB_VERSION = 1;
        DB_CREATE = new ArrayList<>();
        DB_CREATE.addAll(TweetDB.getCreateTablesQueries());
        DB_CREATE.addAll(HashTagDB.getCreateTablesQueries());
        DB_UPGRADE = new ArrayList<>();
        DB_UPGRADE.addAll(HashTagDB.getDropTablesQueries());
        DB_UPGRADE.addAll(TweetDB.getDropTablesQueries());
    }

    /**
     * @see android.database.sqlite.SQLiteOpenHelper#SQLiteOpenHelper(android.content.Context, String, android.database.sqlite.SQLiteDatabase.CursorFactory, int)
     */
    public HashDroidDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    /**
     * Creates the database
     * @param db
     */
    public void onCreate(SQLiteDatabase db)
    {
        for (String query : DB_CREATE) {
            db.execSQL(query);
        }
    }

    /**
     * Updates the database
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        for (String query : DB_UPGRADE) {
            db.execSQL(query);
        }
        onCreate(db);
    }
}
