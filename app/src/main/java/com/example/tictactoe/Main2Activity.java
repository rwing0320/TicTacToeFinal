package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
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
    Button addPlayer;
    Button closeHomePopupButton;
    TextView newPlayer;

    final Context homeContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        db = new PlayerDB(this);

        addPlayer = findViewById(R.id.add_player);


        addPlayer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                showPopupMenu();
//                try {
//                    db.insertPlayer("Ryan");
//                    Log.d("DATABASE ADD PLAYER", "INSERTED NEW PLAYER");
//                    showPlayer();
//                }
//                catch(Exception e){
//                    e.printStackTrace();
//                }
            }
        });
    }


    public void showPopupMenu(){
        // custom dialog
        final Dialog dialog = new Dialog(homeContext);
        dialog.setContentView(R.layout.popupwindow);
        dialog.setTitle("Title...");

        final EditText playerName = dialog.findViewById(R.id.player_name_input);
        //playerName.requestFocus();

        final TextView myErrorMessage = dialog.findViewById(R.id.popupErrorMessage);
        // set the custom dialog components - text, image and button


        Button addPlayer = dialog.findViewById(R.id.add_player_button);
        addPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // EditText playerName = dialog.findViewById(R.id.player_name_input);
                String playerNameString = playerName.getText().toString();

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
                        playerName.setText("");
                        playerName.clearFocus();

                        InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        });

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
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
