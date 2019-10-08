package com.example.tictactoe;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity {

    //added
    // define the SharedPreferences object
    //public SharedPreferences savedValues;

    // define the SharedPreferences object
    public SharedPreferences savedValues;

    //Variable Declaration
    JSONObject newObject;
    JSONArray newJsonArray;
    JSONArray myButtons;
    String [][] ticArray = new String [8][3];

    String [] players = new String [2];

    String playerTurn;
    String playerWin = "";
    String overallPlayerWinText = "";

    int winComboCounter;
    int player1Wins = 0;
    int player2Wins = 0;
    int numberOfGamesCounter = 1;

    String isCatsGame = "false";

    TextView numberOfGames;
    TextView player1Score;
    TextView player2Score;
    TextView playerOverallWin;

    TextView player1Text;
    TextView player2Text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Set variable values for variable strings, arrays, and Views
        players[0] = "Player 1";
        players[1] = "Player 2";

        playerTurn = players[0];

        numberOfGames = findViewById(R.id.numOfGamesText);
        player1Score = findViewById(R.id.player1Score);
        player2Score = findViewById(R.id.player2Score);
        playerOverallWin = findViewById(R.id.playerOverallWin);

        player1Text = findViewById(R.id.textView);
        player2Text = findViewById(R.id.textView2);

        player1Text.setText(players[0]);
        player2Text.setText(players[1]);

        // get SharedPreferences object
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);


        //Call showWinningString to set TextView of the number of games the user is on out of 3
        showWinningStrings();


        //Call the startGame function
        startGame();


        //set the start game button View and create on onClick function that calls restartGame and startGame function
        Button btnStartGame = findViewById(R.id.startGameBtn);
        btnStartGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                restartGame();
                startGame();
            }
        });

    }

    public void startGame(){

        //create the string array by calling createTicArray
        createTicArray();
        //By using the array created in the above function, create the json array that will hold all of the button information
        createJsonArray();

        //set the playerOverallWin TextView to whomevers turn it is
        playerOverallWin.setText(playerTurn + " Turn");

        //For every button on the page, find the button and then set an onClick listener for each button dynamically.
        for(int i = 1; i < 10; i++){

            //Get the button id and create a new butotn by using the resource identifier as the Id for the button
            String myButtonId = "btn" + i;
            int resourceIdentifier = getResources().getIdentifier(myButtonId, "id", getPackageName());
            Button btnOn = findViewById(resourceIdentifier);

            //when one of the buttons get clicked call the below function
            btnOn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){

                    //get the view (the button that was pressed) and convert it to a string
                    String myView = view.toString();

                    //if the button is on the page run the below code
                    if(myView.indexOf("btn") != -1){

                        //get the buttons id
                        int buttonPressed = myView.indexOf("btn") + 3;
                        System.out.println("The index of button is" + myView.charAt(buttonPressed));

                        //call the lookForButton which will take the id of the button as a parameter
                        lookForButton(Character.toString(myView.charAt(buttonPressed)));
                        Log.d("JSONArray", "JSON Array is " + newJsonArray);
                        Log.d("JSONArray", "JSON Array is " + myButtons);
                    }

                }
            });

        }


    }

    @Override
    public void onPause() {
        //This function is called when the user either goes out of the app or changes orientation of the phone.
        //This sets all variables needed to be saved in the savedInstanceState
        // save the instance variables
        SharedPreferences.Editor editor = savedValues.edit();

        editor.putString("newJSONArray", newJsonArray.toString());
        editor.putString("myButtons", myButtons.toString());
        editor.putString("playerTurn", playerTurn);
        editor.putString("hitOnPause", "true");
        editor.putString("playerWin", playerWin);
        editor.putInt("winComboCounter", winComboCounter);
        editor.putInt("numberOfGames", numberOfGamesCounter);
        editor.putInt("player1Score", player1Wins);
        editor.putInt("player2Score", player2Wins);
        editor.putString("overallPlayerWinText", overallPlayerWinText);
        editor.putString("isCatsGame", isCatsGame);
        editor.putString("player1", players[0]);
        editor.putString("player2", players[1]);


        editor.commit();

        super.onPause();


    }

    @Override
    public void onResume() {
        super.onResume();


        //when the app comes back into focus either by orientation change or by putting the app in the background
        //get all of the variables saves in onSaveInstanceState
        if(savedValues != null) {
            try {

                player1Text = findViewById(R.id.textView);
                player2Text = findViewById(R.id.textView2);

                player1Text.setText(players[0]);
                player2Text.setText(players[1]);


                newJsonArray = new JSONArray(savedValues.getString("newJSONArray", ""));
                myButtons = new JSONArray(savedValues.getString("myButtons", ""));

                players = new String[2];
                players[0] = savedValues.getString("player1", "");
                players[1] = savedValues.getString("player2", "");


                playerTurn = savedValues.getString("playerTurn", "");
                winComboCounter = savedValues.getInt("winComboCounter", 0);
                playerWin = savedValues.getString("playerWin", "");
                isCatsGame = savedValues.getString("isCatsGame", "");


                overallPlayerWinText = savedValues.getString("overallPlayerWinText", "");
                numberOfGamesCounter = savedValues.getInt("numberOfGames", 0);
                player1Wins = savedValues.getInt("player1Score", 0);
                player2Wins = savedValues.getInt("player2Score", 0);

                playerOverallWin.setText(overallPlayerWinText);


                //show the winnings string on the page by calling the showWinningStrings function
                showWinningStrings();


                //If there has been no winner, go into the next persons turn
                //if there has ben a winner set the text to the winner of the game
                if (overallPlayerWinText != "") {
                    playerOverallWin.setText(overallPlayerWinText);
                } else {
                    playerOverallWin.setText(playerTurn + " Turn");
                }


                Log.d("JSONArray", "JSON Array is " + newJsonArray);
                Log.d("JSONArray", "JSON Array is " + myButtons);
                //se the buttons to their corresponding texts (either X's or O's)
                displayButtonText();
            } catch (JSONException e) {

            }
        }

    }

//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState){
//        //This function is called when the user either goes out of the app or changes orientation of the phone.
//        //This sets all variables needed to be saved in the savedInstanceState
//
//        super.onSaveInstanceState(savedInstanceState);
//        savedInstanceState.putString("newJSONArray", newJsonArray.toString());
//        savedInstanceState.putString("myButtons", myButtons.toString());
//        savedInstanceState.putString("playerTurn", playerTurn);
//        savedInstanceState.putString("hitOnPause", "true");
//        savedInstanceState.putString("playerWin", playerWin);
//        savedInstanceState.putInt("winComboCounter", winComboCounter);
//        savedInstanceState.putInt("numberOfGames", numberOfGamesCounter);
//        savedInstanceState.putInt("player1Score", player1Wins);
//        savedInstanceState.putInt("player2Score", player2Wins);
//        savedInstanceState.putString("overallPlayerWinText", overallPlayerWinText);
//        savedInstanceState.putString("isCatsGame", isCatsGame);
//        savedInstanceState.putString("player1", players[0]);
//        savedInstanceState.putString("player2", players[1]);
//    }
//
//
//
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState){
//
//
//        //when the app comes back into focus either by orientation change or by putting the app in the background
//        //get all of the variables saves in onSaveInstanceState
//
//        if(savedInstanceState != null) {
//            try {
//
//                super.onRestoreInstanceState(savedInstanceState);
//                player1Text = findViewById(R.id.textView);
//                player2Text = findViewById(R.id.textView2);
//
//                player1Text.setText(players[0]);
//                player2Text.setText(players[1]);
//
//
//                newJsonArray = new JSONArray(savedInstanceState.getString("newJSONArray", ""));
//                myButtons = new JSONArray(savedInstanceState.getString("myButtons", ""));
//
//                players = new String[2];
//                players[0] = savedInstanceState.getString("player1", "");
//                players[1] = savedInstanceState.getString("player2", "");
//
//
//                playerTurn = savedInstanceState.getString("playerTurn", "");
//                winComboCounter = savedInstanceState.getInt("winComboCounter");
//                playerWin = savedInstanceState.getString("playerWin", "");
//                isCatsGame = savedInstanceState.getString("isCatsGame", "");
//
//
//                overallPlayerWinText = savedInstanceState.getString("overallPlayerWinText", "");
//                numberOfGamesCounter = savedInstanceState.getInt("numberOfGames");
//                player1Wins = savedInstanceState.getInt("player1Score");
//                player2Wins = savedInstanceState.getInt("player2Score");
//
//                playerOverallWin.setText(overallPlayerWinText);
//
//
//                //show the winnings string on the page by calling the showWinningStrings function
//                showWinningStrings();
//
//
//                //If there has been no winner, go into the next persons turn
//                //if there has ben a winner set the text to the winner of the game
//                if (overallPlayerWinText != "") {
//                    playerOverallWin.setText(overallPlayerWinText);
//                } else {
//                    playerOverallWin.setText(playerTurn + " Turn");
//                }
//
//
//                Log.d("JSONArray", "JSON Array is " + newJsonArray);
//                Log.d("JSONArray", "JSON Array is " + myButtons);
//                //se the buttons to their corresponding texts (either X's or O's)
//                displayButtonText();
//            } catch (JSONException e) {
//
//            }
//        }
//     }

     public void showWinningStrings(){
        //set the TextViews below
         numberOfGames.setText(numberOfGamesCounter + " OF " + "3");
         player1Score.setText(Integer.toString(player1Wins));
         player2Score.setText(Integer.toString(player2Wins));

     }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void restartGame(){
        //Set the text of each button to an empty string
        for(int i = 1; i < 10; i++){

            String myButtonId = "btn" + i;
            int resourceIdentifier = getResources().getIdentifier(myButtonId, "id", getPackageName());
            Button btnOn = findViewById(resourceIdentifier);

            btnOn.setText("");
        }

        //reset all of the variables below as the game has been reset
        playerTurn = players[0];
        playerWin = "";
        winComboCounter = 0;

        player1Wins = 0;
        player2Wins = 0;
        numberOfGamesCounter = 1;
        overallPlayerWinText = "";
        isCatsGame = "false";

        player1Score.setText("0");
        player2Score.setText("0");
        numberOfGames.setText("1 OF 3");
        playerOverallWin.setText("");

    }

    public void displayButtonText(){
        try {

            //Go through the json array and iterate through each of the buttons
            for (int i = 0; i < myButtons.length(); i++) {
                JSONObject thisButton = myButtons.getJSONObject(i);

                Iterator<String> myKeys = thisButton.keys();

                //Iterate through each of the keys inside of the byButton json array
                while(myKeys.hasNext()){
                    String thisKey = myKeys.next();

                    Log.d("thisKey", "this key value is: " + thisButton.get(thisKey));
                    String isFound = "false";

                    //If the button is found from the json array key value pair then call the loadButtonImagesFromSaveState function
                    //If the button's key value is false that means that it has yet to be chosen from one of the players
                    if(isFound.equals(thisButton.get(thisKey).toString())){

                    }else{
                        loadButtonImagesFromSaveState(thisKey, thisButton.get(thisKey).toString());
                    }


                }

            }
        }catch(JSONException e){
            //Log.d("isFound", "The button has not been found");
        }
    }

    public void loadButtonImagesFromSaveState(String buttonId, String player){
        //Get the button's ID and then find the view by using the resourceIdentifier
        int resourceIdentifier = getResources().getIdentifier(buttonId, "id", getPackageName());
        Button btnOn = findViewById(resourceIdentifier);

        //If the player variable has a value of player one then set the buttons text to X
        //If the player variable has a value of player two then set the buttons text to O
        if(player.equals(players[0])){
            btnOn.setText("X");
        }
        else{
            btnOn.setText("O");
        }
    }


    public void createTicArray(){

        //create a new  multidimensional string array
        //this will be used to keep track of what the winning conditions are.
        ticArray = new String [8][3];

        ticArray[0][0] = "btn1";
        ticArray[0][1] = "btn2";
        ticArray[0][2] = "btn3";

        ticArray[1][0] = "btn1";
        ticArray[1][1] = "btn4";
        ticArray[1][2] = "btn7";

        ticArray[2][0] = "btn1";
        ticArray[2][1] = "btn5";
        ticArray[2][2] = "btn9";

        ticArray[3][0] = "btn2";
        ticArray[3][1] = "btn5";
        ticArray[3][2] = "btn8";

        ticArray[4][0] = "btn3";
        ticArray[4][1] = "btn6";
        ticArray[4][2] = "btn9";

        ticArray[5][0] = "btn4";
        ticArray[5][1] = "btn5";
        ticArray[5][2] = "btn6";

        ticArray[6][0] = "btn7";
        ticArray[6][1] = "btn8";
        ticArray[6][2] = "btn9";

        ticArray[7][0] = "btn7";
        ticArray[7][1] = "btn5";
        ticArray[7][2] = "btn3";


    }

    public void createJsonArray(){
        //create a new json array
        newJsonArray = new JSONArray();

        try {
            //For every single jsonobject in the array set the buttonId with a value of an empty string
            //Also put the counter to 0 and checked to false

            //ticArray values is used to determine which button has been pressed
            //counter is used to determine the number of times that winning set of buttons have been pressed
            //checked is used to identify if this winning condition have been checked or not.

            for (int i = 0; i < 8; i++) {
                newObject = new JSONObject();
                for (int y = 0; y < 3; y++) {
                    newObject.put(ticArray[i][y], "");
                }
                newObject.put("counter", 0);
                newObject.put("checked", "false");
                newJsonArray.put(newObject);
            }
        }catch(JSONException e){

        }

        try {
            //create a new json array and set the key value pairs to the buttonid and if that button has been pressed
            myButtons = new JSONArray();
            JSONObject newButton;
            for (int i = 1; i < 10; i++) {
                newButton = new JSONObject();
                String buttonName = "btn" + i;
                newButton.put(buttonName, "false");
                myButtons.put(newButton);
            }
        }catch(JSONException e){

        }

    }

    public void lookForButton(String buttonNumber){
        //set the buttonPressed string to buttonNumber (which resembles the number of the button) and the string button
        String buttonPressed = "btn" + buttonNumber;

        try {
            //Iterate through the myButton json array
            for (int i = 0; i < myButtons.length(); i++) {

                //get the jsonObject in the josn array at position i
                JSONObject thisButton = myButtons.getJSONObject(i);

                Iterator<String> myKeys = thisButton.keys();

                //iterate through the keys in the json array object
                while(myKeys.hasNext()){
                    String thisKey = myKeys.next();

                    //If the button being passed in has been pressed (equal to the key)
                    if(buttonPressed.equals(thisKey)){
                        //get the object using the key
                        Object value = thisButton.get(thisKey);

                        //if the value of the key is false (meaning the button has not been already clicked)
                        if(value.toString().equals("false")){

                            //change the buttons value to the key to whome evers turn it is
                            thisButton.put(buttonPressed, playerTurn);

                            //call the updateWinCondition function and pass in the buttonPressed String
                            updateWinCondition(buttonPressed);

                            //check to see if a player has won
                            //if the playerWin string is empty and there has been no cats game run the below code
                            if(playerWin.equals("") && isCatsGame.equals("false")){
                                //set the buttons image (or text) by passing in the buttonPressed String
                                setButtonImage(buttonPressed);
                                //call the changePlayersTurn to change whose turn it is
                                changePlayersTurn();
                             //if there has been a cats game and there is still no winner run the below code
                            }else if(isCatsGame.equals("true") || playerWin != ""){
                                //if the number of games that have been played is equal to 3 (meaning all rounds have bene played run the below code
                                if(numberOfGamesCounter == 3){
                                    //if player 1 wins then set text to player 1 winns
                                    //if player 2 wins then set text to player 2 wins
                                    //if neither of th eplayers win then set the text to TIE GAME
                                    if(player1Wins > player2Wins){

                                        overallPlayerWinText = players[0] + " Wins The Game!";
                                    }
                                    else if(player1Wins < player2Wins){

                                        overallPlayerWinText = players[1] + " Wins The Game!";
                                    }
                                    else{
                                        overallPlayerWinText = "ITS A TIE!";
                                    }
                                    //set the TextView to the overallPlayerWinText (whoever won the Game)
                                    playerOverallWin.setText(overallPlayerWinText);
                                }
                                else{
                                    //if the number of rounds played is less than 3 then increment the number of rounds played by 1 and call newRound method and then startGame round

                                    numberOfGamesCounter++;
                                    numberOfGames.setText(numberOfGamesCounter + " OF 3");

                                    newRound();
                                    startGame();
                                }
                            }

                        }
                    }

                }

            }
        }catch(JSONException e){
        }

    }


    public void setButtonImage(String buttonId){

        //get the button view
        int resourceIdentifier = getResources().getIdentifier(buttonId, "id", getPackageName());
        Button btnOn = findViewById(resourceIdentifier);


        //for whome evers turn it is set the buttons text to either X or O
        if(playerTurn.equals(players[0])){
            btnOn.setText("X");
        }
        else{
            btnOn.setText("O");
        }

    }

    public void updateWinCondition(String buttonPressed){

        try {
            //Iterate through the newJsonArray json array
            for (int i = 0; i < newJsonArray.length(); i++) {

                //set the JSON object to the newJsonArray at position i
                JSONObject winComboButtons = newJsonArray.getJSONObject(i);

                //get all the strings for that object
                Iterator<String> myKeys = winComboButtons.keys();

                //iterate through all of th ekeys within that object
                while(myKeys.hasNext()){
                    String thisKey = myKeys.next();

                    //if the button that has been pressed is equal to the key then run the below code
                    if(buttonPressed.equals(thisKey)) {

                        //se the object to the josn object (using the key)
                        Object value = winComboButtons.get(thisKey);

                        //if the value objects value is equal to an empty string (has yet to be pressed)
                        //Then set the value to whom ever's turn it is
                        //Increment counter for this button
                        //this means that once this winning condition has counter equalling to three it can then be checked to see if al of the condition matches the same text (either X or O)
                        if (value.toString().equals("")) {
                            winComboButtons.put(buttonPressed, playerTurn);

                            int counter = (int) winComboButtons.get("counter") + 1;
                            winComboCounter = counter;
                            winComboButtons.put("counter", counter);

                        }

                        //if the counter for that winning condition is equal to three (all buttons have been pressed for that winning condition) and the winning condition has yet to already be checked\
                        //run the below code
                        if (winComboCounter == 3 && winComboButtons.get("checked").toString().equals("false")) {
                            Log.d("winComboButton", winComboButtons.toString());
                            //set the checked key value to true (so it is not checked again)
                            winComboButtons.put("checked", "true");

                            //call the checkWinConditions by passing in the winComboButtons (the winning conditions)
                            checkWinConditions(winComboButtons);
                        }

                    }

                }
            }
            //if there has been no winner call checkCatsGame to see if there has been a cats game
            if(playerWin.equals("")){
                checkCatsGame();
            }

        }catch(JSONException e){

        }

    }

    public void checkWinConditions(JSONObject comboButtons){

        //get all the keys from the passed in json object
        Iterator<String> myKeys = comboButtons.keys();

        int counter = 0;
        try {

            //iterate through each of the keys from the passed in json object
            while (myKeys.hasNext()) {
                String thisKey = myKeys.next();
                Object value = comboButtons.get(thisKey);

                //if the button in the winning condition has been clicked by the same user increment counter by 1
                if(playerTurn.equals(value.toString())){
                    counter++;
                }

            }


            //if all three buttons in the winning condition has been pressed by the same user then run the below code
            if(counter == 3){


                //set the playerwin text to the players turn value
                playerWin = playerTurn;


                //create a new context, a duration
                Context context = getApplicationContext();
                CharSequence text = "";
                int duration = Toast.LENGTH_SHORT;

                //if the playersturn is equal to player 1 run the below code
                if(playerTurn.equals(players[0])){
                    //increment player1Wins by one (as this is used to determine who wins the set of 3 games)
                    player1Wins++;
                    //set the player1Score TextView to the player1Wins Counter
                    player1Score.setText(Integer.toString(player1Wins));
                    text = players[0];

                }
                else{
                    //if the playersturn is equal to player 2
                    //increment player2Wins by one (as this is used to determine who wins the set of 3 games)
                    player2Wins++;
                    //set the player2Score TextView to the player2Wins Counter
                    player2Score.setText(Integer.toString(player2Wins));
                    text = players[1];

                }

                //set the text to the player that won the round
                text = text + " Wins the Round!";

                //create a new toast whcih will display the winner of the round
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                Log.d("winner",playerTurn + " wins!");
            }
        }catch(JSONException e){
            Log.d("checkWinConditionFailed",e.getLocalizedMessage().toString());
        }

    }

    public void newRound(){

        //iterate througheach of the buttons and set their text to an empty string
        for(int i = 1; i < 10; i++){

            String myButtonId = "btn" + i;
            int resourceIdentifier = getResources().getIdentifier(myButtonId, "id", getPackageName());
            Button btnOn = findViewById(resourceIdentifier);

            btnOn.setText("");
        }

        //reset variables for the new round
        playerTurn = players[0];
        playerWin = "";
        winComboCounter = 0;
        isCatsGame = "false";


        playerOverallWin.setText(playerTurn);

    }

    public void checkCatsGame(){

        int tieGameCounter = 0;

        try {
            //iterate through each of the json array objects
            for (int i = 0; i < newJsonArray.length(); i++) {

                JSONObject tieComboButtons = newJsonArray.getJSONObject(i);

                //if the winning condition has already been checked for all 3 buttons and has already been checked for a winner increment tieGameCounter by 1
                if(tieComboButtons.get("checked").toString() == "true"){
                    tieGameCounter++;
                }


            }

            //if all of the winning condition have been checked and niether player wins run the below code.
            if(tieGameCounter == newJsonArray.length()){

                //set he isCatsGame to true
                isCatsGame = "true";

                //create a new toast to let th eplayers know that it was a tied game
                Context context = getApplicationContext();
                CharSequence text = "";
                int duration = Toast.LENGTH_SHORT;

                text = "TIED ROUND";

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }
        }catch(JSONException e){

        }


    }

    public void changePlayersTurn(){

        //if its players 1 turn set the text to player 1 else set the text to player 2
        if(playerTurn == players[0]){
            playerTurn = players[1];

        }
        else{
            playerTurn = players[0];
        }

        playerOverallWin.setText(playerTurn + " Turn");
    }

}
