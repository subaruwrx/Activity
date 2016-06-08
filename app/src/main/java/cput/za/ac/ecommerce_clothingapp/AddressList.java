package cput.za.ac.ecommerce_clothingapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import cput.za.ac.ecommerce_clothingapp.config.databases.MyContract;
import cput.za.ac.ecommerce_clothingapp.domain.Address;
import cput.za.ac.ecommerce_clothingapp.factory.DatasourceDAO;
import cput.za.ac.ecommerce_clothingapp.factory.dom.Impl.DBAdapter;
import cput.za.ac.ecommerce_clothingapp.factory.dom.Impl.DatasourceDAOImpl;

/**
 * Created by Admin on 2016-06-07.
 */
public class AddressList extends Activity {
    Button btnAddAddress;
    ListView listView;
    ArrayAdapter mArrayAdapter;
    ArrayList<Address> mArrayList = new ArrayList<Address>();
    DatasourceDAO dao = new DatasourceDAOImpl(this);

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        btnAddAddress = (Button) findViewById(R.id.btn_AddAddress);
        listView = (ListView) findViewById(R.id.main_listView);
        AlertDialog.Builder alert = new AlertDialog.Builder(AddressList.this);
        //mArrayList = dao.getPersons();

        mArrayList = getAllAddresses();
        mArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mArrayList);
        listView.setAdapter(mArrayAdapter);
        mArrayAdapter.notifyDataSetChanged();



        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressList.this, AddAddress.class);
                intent.putExtra("action", "create");
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                {
                    Intent intent = new Intent(AddressList.this, AddAddress.class);
                    intent.putExtra("action", "update");
                    intent.putExtra("Id", ""+mArrayList.get((int)id).getId());
                    startActivity(intent);
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AddressList.this);
                alert.setTitle("Deleting Selected Item ");
                alert.setMessage("Are you sure you want to delete selected Item " + mArrayList.get((int) id).getStreet());
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Address address = null;
                        try{
                            address = dao.findAddress((Long)mArrayList.get((int)id).getId());
                            int count = getContentResolver().delete(MyContract.Address1.CONTENT_URI,  DBAdapter.COLUMN_ID + " = ?", new String[]{String.valueOf(address.getId())});
                            //dao.deletePerson(p);
                            Toast.makeText(AddressList.this, "Item "+ count+" - "+address.getStreet()+" Deleted succesfully", Toast.LENGTH_LONG).show();
                        }catch(Exception ex){
                            Toast.makeText(AddressList.this, "Failed to delete an Item " + address.getStreet(), Toast.LENGTH_LONG).show();
                        }

                        onCreate(savedInstanceState);
                        mArrayAdapter.notifyDataSetChanged();
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert.show();
                return false;
            }


        });
    }

    private ArrayList<Address> getAllAddresses() {

        Cursor cursor = getContentResolver().query(MyContract.Address1.CONTENT_URI, null,null, null, null);
        ArrayList<Address> persons = new ArrayList<Address>();
        Address address;
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
                persons.add(address);
            } while (cursor.moveToNext());
        }
        return persons;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.address_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
