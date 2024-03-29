package com.example.tictactoe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rhildred on 11/8/2016.
 */
public class PlayerDB {
    // database constants
    public static final String DB_NAME = "player.sqlite";
    public static final int    DB_VERSION = 1;
    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // create tables
            db.execSQL("CREATE TABLE players (id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , name VARCHAR NOT NULL , wins INTEGER NOT NULL  DEFAULT 0, losses INTEGER NOT NULL  DEFAULT 0, ties INTEGER NOT NULL  DEFAULT 0,gamewins INTEGER NOT NULL DEFAULT 0 )");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE \"players\"");
            Log.d("Task list", "Upgrading db from version "
                    + oldVersion + " to " + newVersion);

            onCreate(db);
        }
    }

    // database and database helper objects
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    // constructor
    public PlayerDB(Context context) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
        openWriteableDB();
        closeDB();
    }
    // private methods
    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    private void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB() {
        if (db != null)
            db.close();
    }

    ArrayList<HashMap<String, String>> getPlayers(){
        ArrayList<HashMap<String, String>> data =
                new ArrayList<HashMap<String, String>>();
        openReadableDB();
        Cursor cursor = db.rawQuery("SELECT name, wins, losses, ties FROM players ORDER BY wins DESC",null );
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", cursor.getString(0));
            map.put("wins", cursor.getString(1));
            map.put("losses", cursor.getString(2));
            map.put("ties", cursor.getString(3));
            data.add(map);
        }
        if (cursor != null)
            cursor.close();
        closeDB();

        return data;
    }

    ArrayList<HashMap<String, String>> getPlayerNames(){
        ArrayList<HashMap<String, String>> data =
                new ArrayList<HashMap<String, String>>();
        openReadableDB();
        Cursor cursor = db.rawQuery("SELECT name FROM players",null );
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", cursor.getString(0));
            data.add(map);
        }
        if (cursor != null)
            cursor.close();
        closeDB();

        return data;
    }

    List<String> getPlayerNamesForDisplaying(){
        List<String> playerNames = new ArrayList<String>();
        openReadableDB();
        Cursor cursor = db.rawQuery("SELECT name FROM players",null );
        while (cursor.moveToNext()) {
            //<String, String> map = new HashMap<String, String>();
           // map.put("name", cursor.getString(0));
            playerNames.add(cursor.getString(0));
        }
        if (cursor != null)
            cursor.close();
        closeDB();

        return playerNames;
    }


    String getPlayer(){
        ArrayList<HashMap<String, String>> data =
                new ArrayList<HashMap<String, String>>();
        String name = "";
        openReadableDB();
        Cursor cursor = db.rawQuery("SELECT name FROM players WHERE name == 'Ryan'",null );
        while (cursor.moveToNext()) {

            name = cursor.getString(0);
        }
        if (cursor != null)
            cursor.close();
        closeDB();

        return name;
    }

    String[] getPlayerStats(String name){
        //ArrayList<HashMap<String, String>> data =
                //new ArrayList<HashMap<String, String>>();
        //String name = "";
        String[] playerStats = new String[4];

        openReadableDB();
        Cursor cursor = db.rawQuery("SELECT name, wins, losses, ties FROM players WHERE name == '" + name + "'",null );
        while (cursor.moveToNext()) {


            playerStats[0] = cursor.getString(0);
            playerStats[1] = cursor.getString(1);
            playerStats[2] = cursor.getString(2);
            playerStats[3] = cursor.getString(3);


        }
        if (cursor != null)
            cursor.close();
        closeDB();

        return playerStats;
    }

    int getPlayerWins(String name){
        ArrayList<HashMap<String, String>> data =
                new ArrayList<HashMap<String, String>>();
        int wins = 0;
        openReadableDB();
        Cursor cursor = db.rawQuery("SELECT wins FROM players WHERE name == '" + name + "'",null );
        while (cursor.moveToNext()) {

            wins = cursor.getInt(0);
        }
        if (cursor != null)
            cursor.close();
        closeDB();

        return wins;
    }

    int getPlayerLosses(String name){
        ArrayList<HashMap<String, String>> data =
                new ArrayList<HashMap<String, String>>();
        int losses = 0;
        openReadableDB();
        Cursor cursor = db.rawQuery("SELECT losses FROM players WHERE  name == '" + name + "'",null );
        while (cursor.moveToNext()) {

            losses = cursor.getInt(0);
        }
        if (cursor != null)
            cursor.close();
        closeDB();

        return losses;
    }

    int getPlayerTies(String name){
        ArrayList<HashMap<String, String>> data =
                new ArrayList<HashMap<String, String>>();
        int ties = 0;
        openReadableDB();
        Cursor cursor = db.rawQuery("SELECT ties FROM players WHERE  name == '" + name + "'",null );
        while (cursor.moveToNext()) {

            ties = cursor.getInt(0);
        }
        if (cursor != null)
            cursor.close();
        closeDB();

        return ties;
    }

    boolean checkPlayer(String playerName){
        boolean isFound = true;

        openReadableDB();
        Cursor cursor = db.rawQuery("SELECT name FROM players WHERE name == '" + playerName + "'",null );

        if(cursor.getCount() <= 0){

            isFound = false;
        }
        if (cursor != null){
            cursor.close();
        }

        closeDB();

        return isFound;
    }

    boolean checkForPlayers(){
        boolean isFound = true;

        openReadableDB();
        Cursor cursor = db.rawQuery("SELECT name FROM players",null );

        if(cursor.getCount() <= 1){

            isFound = false;
        }
        if (cursor != null){
            cursor.close();
        }

        closeDB();

        return isFound;
    }

    void insertPlayer(String sName)throws Exception{

        openWriteableDB();
        ContentValues content = new ContentValues();
        content.put("name", sName);
        long nResult = db.insert("players",null, content);
        if(nResult == -1) throw new Exception("no data");
        closeDB();
    }

    void updatePlayerWins(int wins, String name){
        //String strFilter = "_id=" + Id;
        openWriteableDB();
        ContentValues content = new ContentValues();
        content.put("wins", wins);
        db.update("players", content, "name = '" + name + "'", null);
        closeDB();
    }

    void updatePlayerLosses(int losses, String name){
        openWriteableDB();
        ContentValues content = new ContentValues();
        content.put("losses", losses);
        db.update("players", content, "name = '" + name + "'", null);
        closeDB();
    }

    void updatePlayerTies(int ties, String name){
        openWriteableDB();
        ContentValues content = new ContentValues();
        content.put("ties", ties);
        db.update("players", content, "name = '" + name + "'", null);
        closeDB();
    }

    void deleteUser(String name){
        openWriteableDB();
        //ContentValues content = new ContentValues();
        //content.put("ties", ties);
        db.delete("players", "name = '" + name + "'", null);
        closeDB();
    }
}
