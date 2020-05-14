package com.ceri.projet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;  //utilisé dans populate()
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;    //implementation 'com.google.code.gson:gson:2.8.6'

import java.util.ArrayList;
import java.util.List;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE;

public class MuseumDbHelper extends SQLiteOpenHelper {

    private static final String TAG = MuseumDbHelper.class.getSimpleName();

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "museum.db";

    public static final String TABLE_NAME = "museum";

    public static final String _ID = "_id";
    public static final String COLUMN_WEB_ID = "web_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BRAND = "brand";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_TIME_FRAME = "time_frame";
    public static final String COLUMN_CATEGORIES = "categories";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PICTURES = "pictures";
    public static final String COLUMN_TECHNICAL_DETAILS = "technical_details";
    public static final String COLUMN_LAST_UPDATE = "last_update";

    public MuseumDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_BOOK_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_WEB_ID + " TEXT NOT NULL, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_BRAND + " TEXT, " +
                COLUMN_YEAR + " INTEGER, " +
                COLUMN_TIME_FRAME + " TEXT, " +
                COLUMN_CATEGORIES + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_PICTURES + " TEXT, " +
                COLUMN_TECHNICAL_DETAILS + " INTEGER, " +
                COLUMN_LAST_UPDATE+ " TEXT, " +
                // To assure the application have just one item entry per
                // item name and brand, it's created a UNIQUE
                " UNIQUE (" + COLUMN_NAME + ", " +
                COLUMN_BRAND + ") ON CONFLICT ROLLBACK);";

        db.execSQL(SQL_CREATE_BOOK_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Fills ContentValues result from an Item object
     */
    private ContentValues fill(Item item) {
        Gson gson = new Gson();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WEB_ID, item.getWebId());
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_BRAND, item.getBrand());
        values.put(COLUMN_YEAR, item.getYear());
        values.put(COLUMN_TIME_FRAME, gson.toJson(item.getTimeFrame()));
        values.put(COLUMN_CATEGORIES, gson.toJson(item.getCategories()));
        values.put(COLUMN_DESCRIPTION, item.getDesc());
        values.put(COLUMN_PICTURES, gson.toJson(item.getPictures()));
        values.put(COLUMN_TECHNICAL_DETAILS, gson.toJson(item.getTechnicalDetails()));
        values.put(COLUMN_LAST_UPDATE, item.getLastUpdate());
        return values;
    }

    /**
     * Adds a new item
     * @return  true if the item was added to the table ; false otherwise (case when the pair (name, brand) is
     * already in the data base)
     */
    public boolean addItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = fill(item);

        Log.d(TAG, "adding: "+ item.getName()+" with id="+ item.getId());

        // Inserting Row
        // The unique used for creating table ensures to have only one copy of each pair (name, brand)
        // If rowID = -1, an error occured
        long rowID = db.insertWithOnConflict(TABLE_NAME, null, values, CONFLICT_IGNORE);
        db.close(); // Closing database connection

        return (rowID != -1);
    }

    /**
     * Updates the information of an item inside the data base
     * @return the number of updated rows
     */
    public int updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = fill(item);

        // updating row
        return db.updateWithOnConflict(TABLE_NAME, values, _ID + " = ?",
                new String[] { String.valueOf(item.getId()) }, CONFLICT_IGNORE);
    }

    /**
     * Returns a cursor on all the items of the data base
     */
    public Cursor fetchAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null,
                null, null, null, null, COLUMN_NAME +" ASC", null);

        Log.d(TAG, "call fetchAllItems()");

        return cursor;
    }

    /**
     * Returns a list on all the items of the data base
     */
    public List<Item> getAllItems() {
        List<Item> res = new ArrayList<>();
        Cursor c = this.fetchAllItems();
        while(c.moveToNext()) {
            res.add(this.cursorToItem(c));
        }
        c.close();

        return res;
    }

    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, _ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

//    public void populate() {
//        Log.d(TAG, "call populate()");
//        addTeam(new Item("RC Toulonnais", "Top 14"));
//        addTeam(new Item("ASM Clermont Auvergne", "Top 14"));
//        addTeam(new Item("Stade Rochelais", "Top 14"));
//        addTeam(new Item("Bath Rugby","Rugby Union Premiership"));
//        addTeam(new Item("Edinburgh","Pro14"));
//        addTeam(new Item("Stade Toulousain", "Top 14"));
//        addTeam(new Item("Wasps","Rugby Union Premiership"));
//        addTeam(new Item("Bristol Rugby","Rugby Union Premiership"));
//        addTeam(new Item("CA Brive","Pro14"));
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        long numRows = DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM "+TABLE_NAME, null);
//        Log.d(TAG, "nb of rows="+numRows);
//        db.close();
//    }

    public Item cursorToItem(Cursor cursor) {

//        jespere que le ArrayList.class detecte automatiquement le type template (Integer, String,..)
        Gson gson = new Gson();
        ArrayList<Integer> timeFrames = gson.fromJson(cursor.getString(cursor.getColumnIndex(COLUMN_TIME_FRAME)), ArrayList.class);
        ArrayList<String> categories = gson.fromJson(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORIES)), ArrayList.class);
        ArrayList<String> pictures = gson.fromJson(cursor.getString(cursor.getColumnIndex(COLUMN_PICTURES)), ArrayList.class);
        ArrayList<String> technicalDetails = gson.fromJson(cursor.getString(cursor.getColumnIndex(COLUMN_TECHNICAL_DETAILS)), ArrayList.class);

        Item item = new Item(cursor.getLong(cursor.getColumnIndex(_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_WEB_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(COLUMN_BRAND)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_YEAR)),
                timeFrames,
                categories,
                cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                pictures,
                technicalDetails,
                cursor.getString(cursor.getColumnIndex(COLUMN_LAST_UPDATE))
        );

        return item;
    }

    public Item getItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null,
                _ID+" = ?", new String[]{String.valueOf(id)}, null, null, null, "1");
        if (cursor != null)
            cursor.moveToFirst();
        else
            return null;
        return this.cursorToItem(cursor);
    }

    public Item getItem(String name, String brand) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null,
                COLUMN_NAME+" = ? and "+COLUMN_BRAND+" = ?", new String[]{name, brand}, null, null, null, "1");
        if (cursor != null)
            cursor.moveToFirst();
        else
            return null;
        return this.cursorToItem(cursor);
    }


}
