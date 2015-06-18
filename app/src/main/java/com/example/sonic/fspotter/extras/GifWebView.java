package com.example.sonic.fspotter.extras;

import android.content.Context;
import android.webkit.WebView;

/**
 * Created by sonic on 18.06.15.
 */
public class GifWebView extends WebView {

    public GifWebView(Context context, String path) {
        super(context);

        loadUrl(path);
    }
}
