package cput.za.ac.ecommerce_clothingapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cput.za.ac.ecommerce_clothingapp.config.databases.MyContract;
import cput.za.ac.ecommerce_clothingapp.domain.Address;
import cput.za.ac.ecommerce_clothingapp.factory.DatasourceDAO;
import cput.za.ac.ecommerce_clothingapp.factory.dom.Impl.DBAdapter;
import cput.za.ac.ecommerce_clothingapp.factory.dom.Impl.DatasourceDAOImpl;

/**
 * Created by Admin on 2016-06-07.
 */
public class AddAddress extends Activity {

    Button btnCancel;
    Button btnSubmit;
    EditText e_street;
    EditText e_city;
    EditText e_zipCode;
    EditText e_country;


    DatasourceDAO dao = new DatasourceDAOImpl(this);
    Address address;
    Address updateAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        btnCancel = (Button) findViewById(R.id.Btn_Cancel);
        btnSubmit = (Button) findViewById(R.id.Btn_submit);
        e_street  = (EditText) findViewById(R.id.E_street);
        e_city  = (EditText) findViewById(R.id.E_city);
        e_zipCode  = (EditText) findViewById(R.id.E_zipCode);
        e_country = (EditText) findViewById(R.id.E_country);


        Intent message = getIntent();
        final String action = getIntent().getStringExtra("action");

        if (action.equalsIgnoreCase("update")) {
            btnSubmit.setText("Update");
            final String id =getIntent().getStringExtra("Id");
            //updateAddress = populateFields((Long).parseLong(id));
        }
        else if (action.equalsIgnoreCase("delete")) {
            e_street.setEnabled(false);
            e_city.setEnabled(false);
            e_zipCode.setEnabled(false);
            e_country.setEnabled(false);
            btnSubmit.setText("delete");

        }

        final Intent intent  = new Intent(this, AddressList.class);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (!e_street.getText().toString().isEmpty() || !e_city.getText().toString().isEmpty() || !e_country.getText().toString().isEmpty() || !e_zipCode.getText().toString().isEmpty() ) {
                    if (action.equalsIgnoreCase("create")) {
                        address = new Address.Builder()
                                .street(e_street.getText().toString())
                                .city(e_city.getText().toString())
                                .zipCode(e_zipCode.getText().toString())
                                .country(e_country.getText().toString())
                                .build();
                       // dao.createAddress(address);
                        addValues(address);
                        startActivity(intent);
                        displaySuccess();
                    }
                    else if(action.equalsIgnoreCase("update")) {
                        //then update
                        address = new Address.Builder()
                                .copy(updateAddress)
                                .street(e_street.getText().toString())
                                .city(e_city.getText().toString())
                                .zipCode(e_zipCode.getText().toString())
                                .country(e_country.getText().toString())
                                .build();
                        //dao.updatePerson(person);
                        updateValues(address);
                        startActivity(intent);
                        displaySuccess();
                    }


                } else {
                    displayError();
                }

            }

        });
    }

    public void addValues(Address entity) {

        ContentValues values = new ContentValues();
        values.put(DBAdapter.COLUMN_ID, entity.getId());
        values.put(DBAdapter.COLUMN_CITY, entity.getCity());
        values.put(DBAdapter.COLUMN_STREET, entity.getStreet());
        values.put(DBAdapter.COLUMN_COUNTRY, entity.getCountry());
        values.put(DBAdapter.COLUMN_ZIPCODE,entity.getZipCode());

        Uri uri = getContentResolver().insert(MyContract.Address1.CONTENT_URI, values);
        Toast.makeText(getBaseContext(),
                uri.toString() + " inserted!", Toast.LENGTH_LONG).show();

    }

    public void updateValues(Address entity)
    {
        ContentValues values = new ContentValues();
        values.put(DBAdapter.COLUMN_ID, entity.getId());
        values.put(DBAdapter.COLUMN_CITY, entity.getCity());
        values.put(DBAdapter.COLUMN_STREET, entity.getStreet());
        values.put(DBAdapter.COLUMN_COUNTRY, entity.getCountry());
        values.put(DBAdapter.COLUMN_ZIPCODE,entity.getZipCode());

        getContentResolver().update(
                MyContract.Address1.CONTENT_URI, values, DBAdapter.COLUMN_ID + " = ? ",
                new String[]{String.valueOf(entity.getId())});

        Toast.makeText(getBaseContext(),
                " Updated!", Toast.LENGTH_LONG).show();


    }

    private Address populateFields(Long id) {

        Address address = dao.findAddress(id);
        if(address != null) {
            e_street.setText(address.getStreet());
            e_city.setText(address.getCity());
            e_country.setText(address.getCountry());
            e_zipCode.setText(address.getZipCode());
            Toast.makeText(this, "populated", Toast.LENGTH_LONG).show();

        }
        else
        {
            Toast.makeText(this, "Failed to populate contact details", Toast.LENGTH_LONG).show();

        }
        return address;
    }


    private void displaySuccess() {

        Toast.makeText(this, "Saved Successful", Toast.LENGTH_LONG).show();

    }
    private void displayError() {

        Toast.makeText(this, "Please enter all information required", Toast.LENGTH_LONG).show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_address, menu);
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
