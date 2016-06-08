package cput.za.ac.ecommerce_clothingapp.factory.dom.Impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import cput.za.ac.ecommerce_clothingapp.domain.Address;
import cput.za.ac.ecommerce_clothingapp.factory.DatasourceDAO;

/**
 * Created by Admin on 2016-06-07.
 */
public class DatasourceDAOImpl implements DatasourceDAO {

    private SQLiteDatabase database;
    private DBAdapter dbHelper;

    public DatasourceDAOImpl(Context context) {
        dbHelper = new DBAdapter(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    @Override
    public void createAddress(Address entity) {

        open();
        ContentValues values = new ContentValues();
        values.put(DBAdapter.COLUMN_ID, entity.getId());
        values.put(DBAdapter.COLUMN_CITY, entity.getCity());
        values.put(DBAdapter.COLUMN_STREET, entity.getStreet());
        values.put(DBAdapter.COLUMN_COUNTRY, entity.getCountry());
        values.put(DBAdapter.COLUMN_ZIPCODE,entity.getZipCode());

        // Inserting Row
        database.insert(DBAdapter.TABLE_NAME, null, values);
        close(); // Closing database connection

    }

    @Override
    public void updateAddress(Address entity) {
        open();
        ContentValues values = new ContentValues();
        values.put(DBAdapter.COLUMN_ID, entity.getId());
        values.put(DBAdapter.COLUMN_CITY, entity.getCity());
        values.put(DBAdapter.COLUMN_STREET, entity.getStreet());
        values.put(DBAdapter.COLUMN_COUNTRY, entity.getCountry());
        values.put(DBAdapter.COLUMN_ZIPCODE,entity.getZipCode());


        // updating row
        database.update(DBAdapter.TABLE_NAME, values, DBAdapter.COLUMN_ID + " = ? ",
                new String[]{String.valueOf(entity.getId())});
        close();

    }

    @Override
    public Address findAddress(Long id) {
        open();
       String selectQuery = "SELECT  * FROM " + DBAdapter.TABLE_NAME + " where "+ DBAdapter.COLUMN_ID+ " = " +id;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null)
            cursor.moveToFirst();
        Address address = new Address.Builder()
                .id(cursor.getLong(0))
                .street(cursor.getString(1))
                .city(cursor.getString(2))
                .zipCode(cursor.getString(3))
                .country(cursor.getString(4))
                .build();

        close();

        return address;
    }

    @Override
    public void deleteAddress(Address address) {
        open();
        database.delete(DBAdapter.TABLE_NAME, DBAdapter.COLUMN_ID + " = ?", new String[]{String.valueOf(address.getId())});
        close();

    }

    @Override
    public ArrayList<Address> getAddress() {
        String selectQuery = "SELECT  * FROM " + DBAdapter.TABLE_NAME;
        ArrayList<Address> address1= new ArrayList<Address>();
        Address address;
        open();
        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                address = new Address.Builder()
                        .id(cursor.getLong(0))
                        .street(cursor.getString(1))
                        .city(cursor.getString(2))
                        .zipCode(cursor.getString(3))
                        .country(cursor.getString(4))
                        .build();
                address1.add(address);
            } while (cursor.moveToNext());
        }

        close();
        return address1;
    }
}