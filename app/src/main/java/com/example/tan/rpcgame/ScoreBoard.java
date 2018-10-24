package com.example.tan.rpcgame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ScoreBoard extends AppCompatActivity {

    ListView listScores;
    String file = "file";

    ArrayList<String> listItems = new ArrayList<>();
    ArrayList<String> filepath = new ArrayList<>();
    ArrayAdapter<String> listAdapter;
    String line = null;

    File textFile = new File(Environment.getExternalStoragePublicDirectory
            (Environment.DIRECTORY_DCIM), file);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);

        onShow();
    }

    public void playAgainFromScoreBoard(View view) {
        Intent playAgainIntent = new Intent(this, MainActivity.class);
        startActivity(playAgainIntent);
    }

    private boolean isExternalStorageReadable(){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())){
            Log.i("State", "Readable");
            return true;
        }
        else return false;
    }

    public void onShow() {

        listScores = findViewById(R.id.scoreBoardList);
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File externalDirectory = new File (String.valueOf
                    (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)));
            ShowDirectoryFilesInList(externalDirectory);
        }

        if(isExternalStorageReadable()){
            try{
                FileInputStream fileInputStream = new FileInputStream(textFile);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                while((line = bufferedReader.readLine()) != null){
                    listItems.add(line + "\n");
                    listScores.setAdapter(listAdapter);
                    listAdapter.notifyDataSetChanged();
                }
                bufferedReader.close();
                fileInputStream.close();

            } catch (IOException ex) {ex.printStackTrace();}
        } else Toast.makeText(this, "Can't read from external storage",
                Toast.LENGTH_SHORT).show();
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
