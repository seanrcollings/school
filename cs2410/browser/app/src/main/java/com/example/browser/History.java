package com.example.browser;

import android.util.Log;
import android.webkit.WebView;

// Stores History entries as a Linked List
public class History extends LinkedList<String> {
    ListNode<String> curr;
    WebView webView;

    public History(String data, WebView webView) {
        super(data);
        curr = tail;
        this.webView = webView;
    }

    public String getCurrent() {
        return curr.getData();
    }

    public void addToHistory(String url) {
        tail = curr;
        if (url != curr.getData()){
            insertEnd(url);
            Log.i("History", "Appended " + url + " to history " + toString());
            curr = tail;
            webView.loadUrl(curr.getData());
        }
    }

    public void loadUrl() {
        webView.loadUrl(curr.getData());
    }

    public void moveBack() {
        if (curr.prev != null) {
            curr = curr.prev;
            webView.loadUrl(curr.getData());
        } else {
            Log.i("History", "At the top of the history " + toString());
        }
    }

    public void moveForward() {
        if (curr.next != null) {
            curr = curr.next;
            webView.loadUrl(curr.getData());
        } else {
            Log.i("History", "At the bottom of the history " + toString());
        }
    }
}
