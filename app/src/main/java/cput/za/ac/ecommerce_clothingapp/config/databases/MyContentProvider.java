package cput.za.ac.ecommerce_clothingapp.config.databases;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

import cput.za.ac.ecommerce_clothingapp.factory.dom.Impl.DBAdapter;


public class MyContentProvider extends ContentProvider{

    private DBAdapter dbHelper;
    private SQLiteDatabase database;
    private static final UriMatcher matcher;
    private static final int address = 1;
    private static final int address_ID = 2;

    // projection map for a query
    private static HashMap<String, String> addressMap;

    static{
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MyContract.AUTHORITY, MyContract.Address1.TABLE_NAME, address);
        matcher.addURI(MyContract.AUTHORITY, MyContract.Address1.TABLE_NAME + "/#", address_ID);
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new DBAdapter(context);
        database = dbHelper.getWritableDatabase();
        if (database != null) {
            return true;
        }
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MyContract.Address1.TABLE_NAME);
        switch(matcher.match(uri)) {
            case address:
                queryBuilder.setProjectionMap(addressMap);
                break;
            case address_ID:
                queryBuilder.appendWhere( DBAdapter.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri.toString());
        }
          try{
              if(sortOrder == null || sortOrder == "")
                    sortOrder = DBAdapter.COLUMN_STREET;

              Cursor cursor = queryBuilder.query(dbHelper.getWritableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
              cursor.setNotificationUri(getContext().getContentResolver(), uri);
              return cursor;
          }
          catch(SQLiteException e)
          {
              Log.e(getClass().getCanonicalName(), "Query Exception");
              return null;
          }

    }
    @Override
    public String getType(Uri uri)
    {
        switch (matcher.match(uri)) {
            case address:
                return MyContract.Address1.CONTENT_TYPE;
            case address_ID:
                return MyContract.Address1.CONTENT_TYPE_SINGLE_ROW;
           default:
            throw new IllegalArgumentException("Wrong URI: " + uri.toString());
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        database = dbHelper.getWritableDatabase();
        long row = database.insert(MyContract.Address1.TABLE_NAME, null, values);

        if (row > 0)
        {
            Uri newUri = ContentUris.withAppendedId(MyContract.Address1.CONTENT_URI, row);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        //throw new SQLException("Fail to add a new record into ");
        database.close();
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch(matcher.match(uri))
        {
            case address:
                count = database.delete(MyContract.Address1.TABLE_NAME, selection, selectionArgs);
                break;
            case address_ID:
                String id = uri.getLastPathSegment();
                count = database.delete(MyContract.Address1.TABLE_NAME, DBAdapter.COLUMN_ID + "=" + id +
                        (!TextUtils.isEmpty(selection)? "AND (" +selection +')' : ""), selectionArgs  );
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int count = 0;
        switch(matcher.match(uri))
        {
            case address:
                count = database.update(MyContract.Address1.TABLE_NAME, values, selection, selectionArgs);
                break;
            case address_ID:
                String id = uri.getLastPathSegment();
                count = database.delete(MyContract.Address1.TABLE_NAME, DBAdapter.COLUMN_ID + "=" + id +
                        (!TextUtils.isEmpty(selection)? "AND (" +selection +')' : ""), selectionArgs  );
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI" + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
