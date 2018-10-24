package com.example.tan.rpcgame;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GameOver extends AppCompatActivity {

    Bundle extras;
    String playerScore;
    String NameAndScore;
    private static final int REQUEST_CODE_PERMISSION = 1;
    ArrayList<String> filepath = new ArrayList<>();
    String file = "file";
    File textFile = new File(Environment.getExternalStoragePublicDirectory
            (Environment.DIRECTORY_DCIM), file);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        active();

        Button submitNameBtn = findViewById(R.id.submitNameBtn);
        submitNameBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                submitName(v);
            }
        });

        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(GameOver.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(GameOver.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File externalDirectory = new File(String.valueOf
                    (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)));
            ShowDirectoryFilesInList(externalDirectory);
        }
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
        NameAndScore = (submitNameField.getText().toString()) + " " + playerScore;

        if(isExternalStorageWritable()){
            try{
                BufferedWriter bw = new BufferedWriter(new FileWriter(textFile, true));
                bw.write(NameAndScore);
                bw.newLine();
                bw.flush();

                Toast.makeText(this, "Name and score saved", Toast.LENGTH_LONG).show();
            } catch (IOException ex) {ex.printStackTrace();}
        }
        else Toast.makeText(this, "External storage isn't mounted",
                Toast.LENGTH_LONG).show();

        Intent scoreBoardIntent = new Intent(this, ScoreBoard.class);
        startActivity(scoreBoardIntent);
    }

    public void ShowDirectoryFilesInList(File externalDirectory){
        File listFile[] = externalDirectory.listFiles();
        if(listFile != null){
            for(int i = 0; i < listFile.length; i++){
                if(listFile[i].isDirectory()){
                    ShowDirectoryFilesInList(listFile[i]);
                } else filepath.add(listFile[i].getAbsolutePath());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_PERMISSION){
            int grantResultsLength = grantResults.length;
            if(grantResultsLength > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(),
                        "You granted write external storage permission.",
                        Toast.LENGTH_LONG).show();
            }
            else Toast.makeText(getApplicationContext(),
                    "You denied write external storage permission", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isExternalStorageWritable(){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            Log.i("State", "Writable");
            return true;
        }
        else return false;
    }
}
