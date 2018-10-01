package com.example.tan.rpcgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Integer images[] = {R.drawable.questionmark, R.drawable.paper, R.drawable.scissors, R.drawable.rock};
    private int playerChoice = 0;
    private int computerChoice = 0;
    public int playerScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compareResults();
        setInitialImage();
        setRandomComputerChoice();
        setOptionRock();
        setOptionPaper();
        setOptionScissors();
    }

    private void setRandomComputerChoice() {
        final Button startBtn = findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                computerChoice = new Random().nextInt(3) + 1;
                setCurrentImageComputer();
                compareResults();
            }
        });
    }

    private void setInitialImage() {
        setCurrentImagePlayer();
        setCurrentImageComputer();
    }

    private void setCurrentImagePlayer() {

        final ImageView imageView = findViewById(R.id.imageDisplayPlayer);
        imageView.setImageResource(images[playerChoice]);

    }

    private void setCurrentImageComputer() {

        final ImageView imageView = findViewById(R.id.imageDisplay);
        imageView.setImageResource(images[computerChoice]);

    }

    private void setOptionRock(){
        final Button rockBtn = findViewById(R.id.rockBtn);
        rockBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                playerChoice = 3;
                setCurrentImagePlayer();
            }
        });
    }

    private void setOptionPaper(){
        final Button rockBtn = findViewById(R.id.paperBtn);
        rockBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                playerChoice = 1;
                setCurrentImagePlayer();
            }
        });
    }

    private void setOptionScissors(){
        final Button rockBtn = findViewById(R.id.scissorsBtn);
        rockBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                playerChoice = 2;
                setCurrentImagePlayer();
            }
        });
    }

    private void compareResults(){
        final TextView score = findViewById(R.id.scoreEdit);
        final TextView winText = findViewById(R.id.winText);

        if(playerChoice > computerChoice || playerChoice == 1 && computerChoice == 3){
            playerScore += 1;
            winText.setText("PLAYER WINS this round!!!");
        }
        if(playerChoice == computerChoice){
            if(playerChoice != 0 && computerChoice !=0){
                winText.setText("That's a TIE!");
            }
        }
        if(computerChoice > playerChoice || computerChoice == 1 && playerChoice == 3){
            winText.setText("Computer won! GAME OVER!");
            Intent gameOverIntent = new Intent(getApplicationContext(), GameOver.class);
            gameOverIntent.putExtra("playerScore", String.valueOf(playerScore));
            startActivity(gameOverIntent);
        }
        score.setText(String.valueOf(playerScore));
    }

    public void gameOver(View view) {
        Intent gameOverIntent = new Intent(getApplicationContext(), GameOver.class);
        gameOverIntent.putExtra("playerScore", String.valueOf(playerScore));
        startActivity(gameOverIntent);
    }
}