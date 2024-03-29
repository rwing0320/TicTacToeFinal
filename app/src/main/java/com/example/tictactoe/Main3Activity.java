package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main3Activity extends AppCompatActivity {

    Spinner spinner1;
    private PlayerDB db;

    String playerOneName = "";

    Button continueButton;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        db = new PlayerDB(this);

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getBaseContext(), Main2Activity.class);
                startActivity(intent);
            }
        });

        continueButton = findViewById(R.id.startGame_button);
        continueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getBaseContext(), Main4Activity.class);
                intent.putExtra("player1Name", playerOneName);
                startActivity(intent);
            }
        });

        spinner1 = findViewById(R.id.p2_spinner);
        getAvailableNames();
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                try {
                    Log.d("CHANGED NAME", "CHANGED NAME: " + parentView.getItemAtPosition(position).toString());
                    playerOneName = parentView.getItemAtPosition(position).toString();

                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;



                    String text = "PLAYER ONE NAME SET";
                    //create a new toast which will display the winner of the round
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }catch(Exception e){
                    Log.d("SPINNER ERROR", "SPINNER ERROR: " + e.toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        //onChangeSpinnerItemListener();
    }


    public void getAvailableNames(){

        try {
            List<String> list = new ArrayList<String>();
            ArrayList<HashMap<String, String>> data = db.getPlayerNames();
            Log.d("Player", data.toString());



            for (HashMap<String, String> map : data) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    //view.append(entry.getKey() + " => " + entry.getValue());
                    list.add(entry.getValue().toUpperCase());
                    Log.d("objectKey", "KEY: " + entry.getKey());
                    Log.d("objectValue", "VALUE: " + entry.getValue());
                }
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(dataAdapter);

        }
        catch(Exception e){
            e.printStackTrace();
        }



    }
}
