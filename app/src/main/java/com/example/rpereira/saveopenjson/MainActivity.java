package com.example.rpereira.saveopenjson;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private EditText medtName;

    private EditText medtLastname;

    private EditText medtAge;

    private String jsonString = null;

    private Gson gson = null;

    private static final String FILE_NAME = "main_file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        medtName = findViewById(R.id.idedtName);

        medtLastname = findViewById(R.id.idedtLastname);

        medtAge = findViewById(R.id.idedtAge);

        Button mbtnSave = findViewById(R.id.idbtnSave);

        Button mbtnOpen = findViewById(R.id.idbtnOpen);

        Button mbtnClean = findViewById(R.id.idbtnClean);

        mbtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!medtName.getText().toString().isEmpty() && !medtLastname.getText().toString().isEmpty() &&
                        !medtAge.getText().toString().isEmpty()) {

                    @SuppressLint("SimpleDateFormat")
                    DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date date = new Date();
                    Long dateTime = Long.parseLong(dateFormat.format(date));
                    String name = medtName.getText().toString();
                    String lastName = medtLastname.getText().toString();
                    int age = Integer.parseInt(medtAge.getText().toString());

                    Person person = new Person.Builder()
                            .setId(dateTime)
                            .setName(name)
                            .setLastname(lastName)
                            .setAge(age)
                            .build();

                    gson = new Gson();
                    jsonString = gson.toJson(person);

                    if (!jsonString.isEmpty()) {

                        try {

                            FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                            fos.write(jsonString.getBytes());
                            fos.close();

                            Log.i("JSON", "In Json: "+jsonString);
                            toastMsg("Json salvo.");
                            cleamFields();

                        } catch (Exception er) { er.printStackTrace(); }

                    } else {
                        toastMsg("Json está vazio!");
                    }


                } else {
                    toastMsg("Preencha todos os campos");
                }

            }
        });

        mbtnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String jsonRecovered = "";

                // Restore preferences
                try {
                    FileInputStream fis = openFileInput(FILE_NAME);
                    int c;
                    StringBuilder tmp = new StringBuilder();
                    while((c = fis.read()) != -1) {
                        tmp.append(Character.toString((char) c));
                    }
                    fis.close();
                    jsonRecovered = tmp.toString();
                } catch (Exception er) { er.printStackTrace(); }

                if(!jsonRecovered.isEmpty()) {

                    gson = new Gson();

                    Person person = gson.fromJson(jsonRecovered, Person.class);

                    if(person != null) {

                        medtName.setText(person.getmName());
                        medtLastname.setText(person.getmLastname());
                        medtAge.setText(String.valueOf(person.getmAge()));

                    }

                }

            }
        });

        mbtnClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleamFields();
                toastMsg("Clean Fields");
            }
        });

    }

    private void cleamFields() {
        medtName.setText("");
        medtLastname.setText("");
        medtAge.setText("");
        jsonString = null;
        gson = null;
    }

    private void toastMsg(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}
