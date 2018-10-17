package com.example.tan.rpcgame;

import android.Manifest;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Map;

public class ScoreBoard extends AppCompatActivity {

    ListView lstFiles;
    String NameAndScore = "";
    String playerScoreExtras;
    String file = "file";

    ArrayList<String> listItems = new ArrayList<>();

    ArrayList<String> filepath = new ArrayList<>();
    ArrayAdapter<String> listAdapter;
    String line = null;

    private static final int REQUEST_CODE_PERMISSION = 1;

    File textFile = new File(Environment.getExternalStoragePublicDirectory
            (Environment.DIRECTORY_DCIM), file);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);

        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);

        submitToList();

        lstFiles = findViewById(R.id.scoreBoardList);

        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(ScoreBoard.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(ScoreBoard.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File externalDirectory = new File (String.valueOf
                    (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)));
            ShowDirectoryFilesInList(externalDirectory);
        }

        lstFiles.setAdapter(listAdapter);

        onSave();
        onShow();
    }

    public void playAgainFromScoreBoard(View view) {
        Intent playAgainIntent = new Intent(this, MainActivity.class);
        startActivity(playAgainIntent);
    }

    public void submitToList(){
        Bundle extrasScore = getIntent().getExtras();
        if(extrasScore != null){
            playerScoreExtras = extrasScore.getString("playerScore");
        }

        SharedPreferences playerData = getSharedPreferences("userName", Context.MODE_PRIVATE);
        String userName1 = playerData.getString("userName", "");

        NameAndScore = userName1 + "          " + playerScoreExtras;

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

    private boolean isExternalStorageReadable(){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())){
            Log.i("State", "Readable");
            return true;
        }
        else return false;
    }

    public void onSave() {
        if(isExternalStorageWritable()){
            try{
                FileOutputStream fileOutputStream = new FileOutputStream(textFile);
                OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);
                fileOutputStream.write(NameAndScore.getBytes());
                writer.append(NameAndScore);
                writer.close();
                fileOutputStream.close();

                Toast.makeText(this, "File saved", Toast.LENGTH_LONG).show();
            } catch (IOException ex) {ex.printStackTrace();}
        }
        else Toast.makeText(this, "External storage isn't mounted",
                Toast.LENGTH_LONG).show();
    }

    public void onShow() {
        if(isExternalStorageReadable()){
            StringBuilder stringBuilder = new StringBuilder();
            try{
                FileInputStream fileInputStream = new FileInputStream(textFile);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                while((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line + "\n");
                    listItems.add(NameAndScore);
                    lstFiles.setAdapter(listAdapter);
                }
                fileInputStream.close();

            } catch (IOException ex) {ex.printStackTrace();}
        } else Toast.makeText(this, "Can't read from external storage",
                Toast.LENGTH_SHORT).show();
        onSave();
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
}
