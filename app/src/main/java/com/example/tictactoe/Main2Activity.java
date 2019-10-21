package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    private PlayerDB db;
    Button addPlayer, startGame, scoreBoard;
    Button closeHomePopupButton;
    TextView newPlayer;

    final Context homeContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        db = new PlayerDB(this);

        addPlayer = findViewById(R.id.add_player);
        startGame = findViewById(R.id.start_game);
        scoreBoard = findViewById(R.id.highscores);


        startGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                checkEmpty();
            }
        });



        scoreBoard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getBaseContext(), Main5Activity.class);
                startActivity(intent);
            }
        });

        addPlayer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //showPopupMenu();
                Intent intent = new Intent(getBaseContext(), Main6Activity.class);
                startActivity(intent);
            }
        });
    }


    public void checkEmpty(){
        boolean isFound = db.checkForPlayers();

        if(isFound == false){
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;



            String text = "PLEASE ADD ATLEAST 2 PEOPLE BEFORE STARTING A GAME! CLICK ADD PLAYER";
            //create a new toast which will display the winner of the round
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            addPlayer.requestFocus();

        }
        else{
            goToplayerSelection();
        }
    }

    public void goToplayerSelection(){
        Intent intent = new Intent(this, Main3Activity.class);
        startActivity(intent);
    }



}
