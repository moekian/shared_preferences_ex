package com.mohammadkiani.sharedpreferencesdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText nameEditText, numberEditText, occupationEditText;
    private ArrayList<Contact> contacts;


    // instance of shared preferences
    SharedPreferences sharedPreferences;

    public static final String SHARED_PREFERENCES_NAME = "username";
    public static final String KEY_NAME = "key_username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // instantiate shared preferences
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        // write into my shared preferences
//        sharedPreferences.edit().putString(KEY_NAME, "Mo").apply();

        // read from shared preferences
        String username = sharedPreferences.getString(KEY_NAME, "NA");

//        Log.i(TAG, "onCreate: " + username);

        ArrayList names = new ArrayList(Arrays.asList("Mo", "kian", "Na", "sari"));
//        sharedPreferences.edit().putStringSet("names", new HashSet<>(names)).apply();

        // read
        Set<String> retrievedNames = sharedPreferences.getStringSet("names", new HashSet<>());
//        Log.i(TAG, "onCreate: " + retrievedNames.toString());

        // writing an object into shared preferences
//        try {
//            sharedPreferences.edit().putString("names_serialized", ObjectSerializer.serialize(names)).apply();
//            Log.i(TAG, "onCreate: " + ObjectSerializer.serialize(names));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // read from shared preferences
        ArrayList deserializedNames = new ArrayList();
        String receivedNames = sharedPreferences.getString("names_serialized", null);
        Log.i(TAG, "onCreate: serialized: " + receivedNames);
        try {
            deserializedNames = (ArrayList) ObjectSerializer.deserialize(receivedNames);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "onCreate: deserialized: " + deserializedNames);

        // initialize the edit texts
        nameEditText = findViewById(R.id.name_et);
        numberEditText = findViewById(R.id.number_et);
        occupationEditText = findViewById(R.id.occupation_et);

        contacts = new ArrayList<>();
    }

    public void addContact(View v) {

        Contact contact = new Contact(nameEditText.getText().toString().trim(),
                Integer.parseInt(numberEditText.getText().toString().trim()),
                occupationEditText.getText().toString().trim());
        contacts.add(contact);

        try {
            sharedPreferences.edit().putString("names_serialized", ObjectSerializer.serialize((Serializable) contacts)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }

        getContacts();
    }

    private void getContacts() {

        String receivedSerializedString = sharedPreferences.getString("names_serialized", null);
        try {
            contacts = (ArrayList<Contact>) ObjectSerializer.deserialize(receivedSerializedString);
            for (Contact c: contacts)
                Log.i(TAG, "addContact: " + c.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
