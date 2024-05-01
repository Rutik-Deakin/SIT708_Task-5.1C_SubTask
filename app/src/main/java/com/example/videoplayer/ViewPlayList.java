package com.example.videoplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViewPlayList extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_play_list);

        LinearLayout linearLayout = findViewById(R.id.linksLayout);


        // Retrieve logged-in user information from SharedPreferences
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("LoggedInUser", Context.MODE_PRIVATE);
        long userId = sharedPreferences.getLong("userId", 0);

        if (userId == 0) {
            Intent intent = new Intent(ViewPlayList.this, MainActivity.class);
            startActivity(intent);
            return;
        }
        List<String> playlist = new ArrayList<>();
        PlaylistService playlistService = new PlaylistService(ViewPlayList.this);
        playlist = playlistService.getPlaylistByUserId(userId);

        // Add each link to the LinearLayout
        for (String link : playlist) {
            addLinkToLayout(linearLayout, link);
        }
    }
    // Method to add a link TextView to the LinearLayout
    private void addLinkToLayout(LinearLayout layout, String link) {
        TextView textView = new TextView(this);
        textView.setText(link);
        textView.setTextSize(18);
        textView.setPadding(0, 8, 0, 12);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewPlayList.this, ViewVideo.class);
                intent.putExtra("url", link);
                startActivity(intent);
            }
        });
        layout.addView(textView);
    }
}