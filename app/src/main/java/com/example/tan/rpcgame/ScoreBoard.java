package com.example.tan.rpcgame;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Map;

public class ScoreBoard extends AppCompatActivity {

    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        submitToList();
    }

    public void playAgainFromScoreBoard(View view) {
        Intent playAgainIntent = new Intent(this, MainActivity.class);
        startActivity(playAgainIntent);
    }

    public void submitToList(){
        Bundle extrasScore = getIntent().getExtras();
        String userName = getSharedPreferences("userName", 0).toString();
        SharedPreferences playerData = getSharedPreferences("name", Context.MODE_PRIVATE);
        editor = playerData.edit();
        editor.putString("name", userName + "   " + extrasScore);
        final ListView list = findViewById(R.id.scoreBoardList);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        list.setAdapter(adapter);
        editor.apply();
        editor.commit();

        Map<String, ?> allEntries = playerData.getAll();

        for(Map.Entry<String, ?> entry : allEntries.entrySet()){
            listItems.add(entry.getValue().toString());
        }

    }
}
