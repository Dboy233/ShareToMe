package com.is.myshare;

import android.os.Bundle;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;

import com.DBoy.share.to.me.BaseShareData;
import com.DBoy.share.to.me.ShareToMe;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ShareToMe.handleShareToMe(this, getIntent(), new ShareToMe.HandleListener() {
            @Override
            public void handleType(String type) {
                Log.d("Dboy", "type =>" + type);
            }

            @Override
            public void handleContent(BaseShareData shareData) {
                Log.d("Dboy", "data =>" + shareData.toString());
            }

            @Override
            public void handleError(String e) {
                Log.d("Dboy", "error =>" + e);
            }
        });
    }
}
