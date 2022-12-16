package com.example.browser;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.example.browser.views.NavBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        WebView webView = new WebView(this);
        final History history = new History("https://usu.edu", webView);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(history.getCurrent());


        NavBar navBar = new NavBar(this, history);

        mainLayout.addView(navBar);
        mainLayout.addView(webView);
        setContentView(mainLayout);
    }
}