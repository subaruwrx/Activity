package cput.za.ac.ecommerce_clothingapp.factory.dom.Impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Admin on 2016-06-07.
 */
public class DBAdapter extends SQLiteOpenHelper {

    // Set Table name
    public static final String TABLE_NAME="address";

    // Set Columns
    public static final String COLUMN_ID="id";
    public static final String COLUMN_CITY="city";
    public static final String COLUMN_STREET="street";
    public static final String COLUMN_COUNTRY="country";
    public static final String COLUMN_ZIPCODE="zipCode";

    private static final String DATABASE_NAME="Address.db";
    private static final int DATABASE_VERSION=1;

    // DATABASE CREATION SQL
    private static final String DATABASE_CREATE = " CREATE TABLE "
            +TABLE_NAME +"("
            +COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COLUMN_CITY + " TEXT  NOT NULL , "
            +COLUMN_STREET +" TEXT NOT NULL ,"
            +COLUMN_COUNTRY +" TEXT NOT NULL ,"
            +COLUMN_ZIPCODE +" TEXT NOT NULL);";

    public DBAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(DBAdapter.class.getName(), "upgrading the Database from Version "+oldVersion+" to "+newVersion+" Which will destroy old data");
        database.execSQL(" DROP IF EXISTS "+ TABLE_NAME);
        onCreate(database);
    }

}
