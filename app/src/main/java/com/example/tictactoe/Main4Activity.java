package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main4Activity extends AppCompatActivity {

    Spinner spinner2;
    private PlayerDB db;

    String playerTwoName = "";
    boolean chooseValid = false;

    Button startGameButton;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        db = new PlayerDB(this);

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getBaseContext(), Main3Activity.class);
                startActivity(intent);
            }
        });

        final String playerOneName = getIntent().getStringExtra("player1Name");
        Log.d("PLAYER ONE", "PLAYER ONE NAME: " + playerOneName);

        startGameButton = findViewById(R.id.startGame_button);
        startGameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(chooseValid == true){
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    intent.putExtra("player1Name", playerOneName);
                    intent.putExtra("player2Name", playerTwoName);
                    startActivity(intent);

                   
                }
                else{
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;

                    String text = "PLEASE CHOOSE A VALID NAME!";
                    //create a new toast which will display the winner of the round
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });

        spinner2 = findViewById(R.id.p2_spinner);
        getAvailableNames();
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                try {
                    Log.d("CHANGED NAME", "CHANGED NAME: " + parentView.getItemAtPosition(position).toString());

                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;



                    String text = "";
                    //create a new toast which will display the winner of the round


                    if(playerOneName.equals(parentView.getItemAtPosition(position).toString())){
                        text = parentView.getItemAtPosition(position).toString() + " HAS ALREADY BEEN TAKEN, PLEASE CHOOSE ANOTHER NAME";
                        chooseValid = false;
                    }
                    else{
                        text = "PLAYER TWO NAME SET";
                        playerTwoName = parentView.getItemAtPosition(position).toString();
                        chooseValid = true;
                    }

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
            spinner2.setAdapter(dataAdapter);

        }
        catch(Exception e){
            e.printStackTrace();
        }



    }
}
