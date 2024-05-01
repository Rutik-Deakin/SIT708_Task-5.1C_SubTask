package com.example.videoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewVideo extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video);
        Intent intent = getIntent();
        String videoURLFromIntent = intent.getStringExtra("url");
        String videoId = "";

        if (videoURLFromIntent.length() != 0) {
            Pattern pattern = Pattern.compile("(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%2Fvideos%2F|youtu.be%2F|\\/v%2F|e%2F|watch\\?v=|&v=)([^#\\&\\?\\n]*)(?:[\\w-+]*&?%3D)?");
            Matcher matcher = pattern.matcher(videoURLFromIntent);

            if (matcher.find()) {
                videoId = matcher.group(0);
            } else {
                Toast.makeText(ViewVideo.this, "Could not get the valid link!", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(ViewVideo.this, "Could not get the valid link!", Toast.LENGTH_SHORT).show();
            return;
        }

        String stringJavaScript = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <body>\n" +
                "    <!-- 1. The <iframe> (and video player) will replace this <div> tag. -->\n" +
                "    <div id=\"player\"></div>\n" +
                "\n" +
                "    <script>\n" +
                "      // 2. This code loads the IFrame Player API code asynchronously.\n" +
                "      var tag = document.createElement('script');\n" +
                "\n" +
                "      tag.src = \"https://www.youtube.com/iframe_api\";\n" +
                "      var firstScriptTag = document.getElementsByTagName('script')[0];\n" +
                "      firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);\n" +
                "\n" +
                "      // 3. This function creates an <iframe> (and YouTube player)\n" +
                "      //    after the API code downloads.\n" +
                "      var player;\n" +
                "      function onYouTubeIframeAPIReady() {\n" +
                "        var width = Math.min((window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth) - 15, 640);\n" +
                "        player = new YT.Player('player', {\n" +
                "          height: '250',\n" +
                "          width: width,\n" +
                "          videoId: '" + videoId + "',\n" +
                "          playerVars: {\n" +
                "            'playsinline': 1\n" +
                "          },\n" +
                "          events: {\n" +
                "            'onReady': onPlayerReady,\n" +
                "          }\n" +
                "        });\n" +
                "      }\n" +
                "\n" +
                "      // 4. The API will call this function when the video player is ready.\n" +
                "      function onPlayerReady(event) {\n" +
                "        event.target.playVideo();\n" +
                "        Android.onPlayerReady(); // Notify Android that player is ready\n" +
                "      }\n" +
                "\n" +
                "      // 5. The API calls this function when the player's state changes.\n" +
                "      //    The function indicates that when playing a video (state=1),\n" +
                "      //    the player should play for six seconds and then stop.\n" +
                "      function stopVideo() {\n" +
                "        player.stopVideo();\n" +
                "      }\n" +
                "    </script>\n" +
                "  </body>\n" +
                "</html>";

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadData(stringJavaScript, "text/html", "utf-8");
    }
}
