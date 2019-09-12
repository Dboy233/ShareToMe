package com.is.myshare;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.is.myshare.tome.BaseShareData;
import com.is.myshare.tome.ShareToMe;
import com.is.myshare.tome.ShareToMeActivity;

public class MainActivity extends ShareToMeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ShareToMe.handleShareToMe(this,getIntent(), new ShareToMe.HandleListener() {
            @Override
            public void handleType(String type) {
                Log.d("DDDd","type"+type);
            }

            @Override
            public void handleContent(BaseShareData shareData) {
                Log.d("DDDd","data"+shareData.toString());
            }

            @Override
            public void handleError(String e) {
                Log.d("DDDd","error = "+e);
            }
        });


    }
}
