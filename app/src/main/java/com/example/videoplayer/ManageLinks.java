package com.example.videoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ManageLinks extends AppCompatActivity {

    private Button addToPlaylistButton, myPlaylistButton, playButton;
    private EditText youtubeLinkEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_links);

        // Retrieve logged-in user information from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoggedInUser", Context.MODE_PRIVATE);
        long userId = sharedPreferences.getLong("userId", 0);
        String username = sharedPreferences.getString("username", "");
        String fullName = sharedPreferences.getString("fullName", "");

        if (userId == 0) {
            Intent intent = new Intent(ManageLinks.this, MainActivity.class);
            startActivity(intent);
        }

        youtubeLinkEditText = findViewById(R.id.youtubeLinkEditText);
        addToPlaylistButton = findViewById(R.id.addToPlaylistButton);
        myPlaylistButton = findViewById(R.id.myPlaylistButton);
        playButton = findViewById(R.id.playButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = youtubeLinkEditText.getText().toString().trim();
                if (!checkUrl(link)) {
                    Toast.makeText(ManageLinks.this, "Please provide valid link!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(ManageLinks.this, ViewVideo.class);
                intent.putExtra("url", link);
                startActivity(intent);
            }
        });

        addToPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = youtubeLinkEditText.getText().toString().trim();
                if (!checkUrl(link)) {
                    Toast.makeText(ManageLinks.this, "Please provide valid link!", Toast.LENGTH_SHORT).show();
                    return;
                }
                PlaylistService playlistService = new PlaylistService(ManageLinks.this);
                long recordId = playlistService.addPlaylist(userId, link);
                if (recordId != 0) {
                    Toast.makeText(ManageLinks.this, "Video added to your playlist!", Toast.LENGTH_SHORT).show();
                    youtubeLinkEditText.setText(null);
                } else {
                    Toast.makeText(ManageLinks.this, "Video is NOT added to your playlist!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageLinks.this, ViewPlayList.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkUrl(String url) {
        return url.length() != 0;
    }
}