package com.is.myshare;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.DBoy.share.to.me.BaseShareData;
import com.DBoy.share.to.me.ShareImageData;
import com.DBoy.share.to.me.ShareMultipleImagesData;
import com.DBoy.share.to.me.ShareToMe;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tbruyelle.rxpermissions2.RxPermissions;

public class MainActivity extends AppCompatActivity {

    TextView mTextView;
    RecyclerView mRecyclerView;
    ImageAdapter mBaseQuickAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe();

        mTextView = findViewById(R.id.text_title);
        mRecyclerView = findViewById(R.id.img_recycler);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBaseQuickAdapter = new ImageAdapter(R.layout.item_layout);
        mRecyclerView.setAdapter(mBaseQuickAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        final ImageView viewById = findViewById(R.id.imge_img);

        ShareToMe.handleShareToMe(this, getIntent(), new ShareToMe.HandleListener() {
            /**
             *
             * @param type  Original Sharing Type image/* ,text/plain ,text/x-vcard<p/>
             */
            @Override
            public void handleType(String type) {
                Log.d("Dboy", "type =>" + type);
            }

            /**
             * @param shareData 处理分享数据的实体基类
             */
            @Override
            public void handleContent(BaseShareData shareData) {

                Log.d("Dboy", "data =>" + shareData.toString());

                BaseShareData.ShareDataType dataType = shareData.getDataType();

                if (BaseShareData.ShareDataType.SHARE_MULTIPLE_IMAGES_DATA == dataType) {
                    ShareMultipleImagesData shareMultipleImagesData = (ShareMultipleImagesData) shareData;
                    mBaseQuickAdapter.addData(shareMultipleImagesData.getImgPath());
                } else if (BaseShareData.ShareDataType.SHARE_IMG_DATA == dataType) {
                    ShareImageData shareImageData = (ShareImageData) shareData;
                    viewById.setVisibility(View.VISIBLE);
                    String path = shareImageData.getPath();
                    Glide.with(getApplicationContext()).load(path).into(viewById);
                }

            }

            /**
             * @param e error
             */
            @Override
            public void handleError(String e) {
                Log.d("Dboy", "error =>" + e);
            }
        });
    }


    class ImageAdapter extends BaseQuickAdapter<ShareImageData, BaseViewHolder> {

        public ImageAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, ShareImageData item) {
            ImageView imgView = helper.getView(R.id.item_img);
            Glide.with(helper.itemView).load(item.getPath()).into(imgView);
        }
    }

}
