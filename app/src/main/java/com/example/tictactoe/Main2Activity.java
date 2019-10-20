package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    private PlayerDB db;
    Button addPlayer;
    TextView newPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        db = new PlayerDB(this);

        addPlayer = findViewById(R.id.add_player);
        newPlayer = findViewById(R.id.newPlayerAdded);

        addPlayer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                try {
                    db.insertPlayer("Ryan");
                    Log.d("DATABASE ADD PLAYER", "INSERTED NEW PLAYER");
                    showPlayer();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    public void showPlayer(){
        //set the TextViews below
        try {
            String data = db.getPlayer();
            Log.d("Player", data);




        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
}
