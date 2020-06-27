package com.example.web;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Intent ServiceIntent;
    WebView webView;
    private ClipboardManager myClipboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView=findViewById(R.id.webView);

        MyService yourservice = new MyService();
        ServiceIntent = new Intent(this, yourservice.getClass());
        if (!isMyServiceRunning(yourservice.getClass())) {
            startService(ServiceIntent);
        }
        Intent cl=new Intent(MainActivity.this,ClipboardService.class);
            myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData abc = myClipboard.getPrimaryClip();
            ClipData.Item item = abc.getItemAt(0);
            String text = item.getText().toString();
            if(text.contains("https")||text.contains("http")||text.contains("www")){
                webView.setWebViewClient(new WebViewClient());
                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webView.loadUrl(text);
                Toast.makeText(getApplicationContext(), "URL Pasted",
                        Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "Please Copy any Valid URL", Toast.LENGTH_SHORT).show();
            }

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }
    @Override
    protected void onDestroy() {
        stopService(ServiceIntent);
        super.onDestroy();
    }

}