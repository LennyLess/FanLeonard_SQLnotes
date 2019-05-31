package com.example.mycontactapp;

import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity{

    DatabastHelper myDb;
    EditText editName;
    EditText editPhone;
    EditText editEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editText_name);
        editPhone = findViewById(R.id.editText_phone);
        editEmail = findViewById(R.id.editText_email);

        myDb = new DatabastHelper(this);
        Log.d("MyContactApp", "MainActivity: Instantiated DatabaseHelper");

    }

    public void addData(View view){

        boolean isInserted = myDb.insertData(editName.getText().toString(), editPhone.getText().toString(), editEmail.getText().toString() );

        if(isInserted = true){
            Toast.makeText(MainActivity.this, "Success - contact inserted", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(MainActivity.this, "Failed - contact inserted", Toast.LENGTH_LONG).show();

        }

    }

    public void viewData(View view){
        Cursor res = myDb.getAllData();

        if(res.getCount() == 0){
            showMessage("Error", "No data found in the database");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            //append res colum 0, 000 to the buffer - see Stringbuffer and Cursors api's
            buffer.append("ID: " + res.getString(0) + "\n");
            buffer.append("Name: " + res.getString(1) + "\n");
            buffer.append("Phone: " + res.getString(2) + "\n");
            buffer.append("Email: " + res.getString(3) + "\n");
        }

        showMessage("Data", buffer.toString());
    }

    public void showMessage(String title, String message){
        //put Log.d's in here
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }

    public static final String EXTRA_NAME = "com.example.mycontactapp";

    public void searchRecord(View view) {
        Log.d("MyContactApp", "MainActivity: launching search");
        Cursor curs = myDb.getAllData();
        StringBuffer buffer = new StringBuffer();
        //Intent intent = new Intent(this, SearchActivity.class);
        if (editName.getText().toString().isEmpty()
                && editPhone.getText().toString().isEmpty()
                && editEmail.getText().toString().isEmpty()) {
            showMessage("Error", "Nothing to search for!");
            return;
        }

        while (curs.moveToNext()) {
            if ((editName.getText().toString().isEmpty() || editName.getText().toString().equals(curs.getString(1)))
                    && (editPhone.getText().toString().isEmpty() || editPhone.getText().toString().equals(curs.getString(2)))
                    && (editEmail.getText().toString().isEmpty() || editEmail.getText().toString().equals(curs.getString(3)))) {

                buffer.append("ID: " + curs.getString(0) + "\n" +
                        "Name: " + curs.getString(1) + "\n" +
                        "Phone number: " + curs.getString(2) + "\n" +
                        "Home address: " + curs.getString(3) + "\n\n");
            }
        }

        if (buffer.toString().isEmpty()) {
            showMessage("Error", "No matches found");
            return;
        }

        showMessage("Search results", buffer.toString());

    }
}