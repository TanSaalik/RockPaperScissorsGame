package com.example.tan.rpcgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {

    SharedPreferences.Editor editor;
    Bundle extras;
    String playerScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        active();
    }
    public void active(){
        final TextView scoreEdit = findViewById(R.id.scoreEdit1);
        extras = getIntent().getExtras();
        if (extras != null) {
            playerScore = extras.getString("playerScore");
            scoreEdit.setText(playerScore);
        }
    }

    public void playAgain(View view) {
        Intent playAgainIntent = new Intent(this, MainActivity.class);
        startActivity(playAgainIntent);
    }

    public void submitName(View view) {
        EditText submitNameField = findViewById(R.id.submitNameField);
        SharedPreferences username = getSharedPreferences("userName", Context.MODE_PRIVATE);
        editor = username.edit();
        editor.putString("userName", submitNameField.getText().toString());
        editor.apply();

        Intent scoreBoardIntent = new Intent(this, ScoreBoard.class);
        scoreBoardIntent.putExtra("playerScore", playerScore);
        startActivity(scoreBoardIntent);
    }
}
