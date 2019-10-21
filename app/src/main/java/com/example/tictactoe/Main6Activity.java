package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main6Activity extends AppCompatActivity {

    private PlayerDB db;
    public ListView playersView;

    public String playerNameOn;
    public EditText playerName, playerWins, playerLosses, playerTies, userInput;
    public Button deleteUser, addPlayer;
    public ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        playerNameOn = "";

        playerName = findViewById(R.id.editText4);
        playerWins = findViewById(R.id.editText);
        playerLosses = findViewById(R.id.editText2);
        playerTies = findViewById(R.id.editText3);

        userInput = findViewById(R.id.player_name_input);

        deleteUser = findViewById(R.id.delete_button);
        deleteUser.setVisibility(View.GONE);

        db = new PlayerDB(this);

        playersView = findViewById(R.id.player_listview);




        getListOfPlayers();

        playersView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                playerNameOn = name;
                getInfo(name);
                deleteUser.setVisibility(View.VISIBLE);
                Log.d("name", "PERSON NAME: " + name);
            }
        });

        deleteUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                deleteUser();
            }
        });

        EditText playerName = findViewById(R.id.player_name_input);
        //playerName.requestFocus();

        final TextView myErrorMessage = findViewById(R.id.popupErrorMessage);
        // set the custom dialog components - text, image and button


        addPlayer = findViewById(R.id.add_player_button);
        addPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EditText playerName = dialog.findViewById(R.id.player_name_input);
                String playerNameString = userInput.getText().toString();

                if(playerNameString.equals("")){
                    myErrorMessage.setText("Please put in a name!");
                }else{
                    boolean isFound = checkValid(playerNameString);

                    if(isFound == true){
                        myErrorMessage.setText("Name Already exists, please try again!");
                    }
                    else{
                        myErrorMessage.setText("");
                        addPlayer(playerNameString);
                        userInput.setText("");
                        userInput.clearFocus();

                        InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        });

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Main2Activity.class);
                startActivity(intent);
            }
        });

    }

    public boolean checkValid(String name){
        boolean isFound = db.checkPlayer(name.toLowerCase());

        if(isFound == true){
            return true;
        }
        else{
            return false;
        }

    }

    public void addPlayer(String name){
        try {
            db.insertPlayer(name.toLowerCase());
            Log.d("DATABASE ADD PLAYER", "INSERTED NEW PLAYER");


            resetForm();
            getListOfPlayers();

            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;



            String text = "PLAYER ADDED";
            //create a new toast which will display the winner of the round
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();


        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void getInfo(String name){
        String[] playerInfo = db.getPlayerStats(name);

        playerName.setEnabled(false);
        playerWins.setEnabled(false);
        playerLosses.setEnabled(false);
        playerTies.setEnabled(false);

        playerName.setText(playerInfo[0].toUpperCase());
        playerWins.setText(playerInfo[1].toUpperCase());
        playerLosses.setText(playerInfo[2].toUpperCase());
        playerTies.setText(playerInfo[3].toUpperCase());


    }



    public void getListOfPlayers(){


        // create a List of Map<String, ?> objects
        List<String> players = db.getPlayerNamesForDisplaying();

        // create the resource, from, and to variables
        //int resource = R.layout.listview_playeritem;
       // String[] from = {"name", "wins", "losses", "ties"};
        //int[] to = {R.id.nameTextView, R.id.winsTextView, R.id.lossesTextView, R.id.tiesTextView};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                players);


        playersView.setAdapter(arrayAdapter);

        // create and set the adapter
        //SimpleAdapter adapter =
           //     new SimpleAdapter(this, data, resource, from, to);
        //players.setAdapter(adapter);
    }

    public void deleteUser(){
        db.deleteUser(playerNameOn);
        resetForm();
        getListOfPlayers();

    }

    public void resetForm(){
        playerName.setText("");
        playerWins.setText("");
        playerLosses.setText("");
        playerTies.setText("");

        deleteUser.setVisibility(View.GONE);
    }
}


