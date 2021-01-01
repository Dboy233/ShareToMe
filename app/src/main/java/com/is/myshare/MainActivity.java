package com.is.myshare;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.DBoy.share.to.me.BaseShareData;
import com.DBoy.share.to.me.ShareImageData;
import com.DBoy.share.to.me.ShareMultipleImagesData;
import com.DBoy.share.to.me.ShareTextData;
import com.DBoy.share.to.me.ShareToMe;
import com.DBoy.share.to.me.ShareVCardData;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import ezvcard.VCard;
import ezvcard.property.StructuredName;

public class MainActivity extends AppCompatActivity {
    //单个图片分享展示IV
    ImageView imageView;
    //分享内容以文本展示View
    TextView mTextView;
    //展示多组图片分享
    RecyclerView mRecyclerView;
    ImageAdapter mBaseQuickAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //检查权限
        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).subscribe();

        imageView = findViewById(R.id.imge_img);
        mTextView = findViewById(R.id.text_title);
        mRecyclerView = findViewById(R.id.img_recycler);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBaseQuickAdapter = new ImageAdapter(R.layout.item_layout);
        mRecyclerView.setAdapter(mBaseQuickAdapter);


    }

    /**
     * 在onResume中处理
     */
    @Override
    protected void onResume() {
        super.onResume();
        checkHandleShare();
    }

    private void checkHandleShare() {
        ShareToMe.handleShareToMe(this, getIntent(), new ShareToMe.ShareCallBack() {
            /**
             *
             * @param type  Original Sharing Type image/* ,text/plain ,text/x-vcard<p/>
             */
            @Override
            public void callType(String type) {
                Log.d("Dboy", "type =>" + type);
            }

            /**
             * @param shareData 处理分享数据的实体基类
             */
            @Override
            public void onSuccess(BaseShareData shareData) {
                mTextView.setText(shareData.toString());
                switch (shareData.getDataType()) {
                    case SHARE_IMG_DATA:
                        //多组图片分享
                        ShareMultipleImagesData shareMultipleImagesData = shareData.getMultipleImagesData();
                        mBaseQuickAdapter.addData(shareMultipleImagesData.getImgPath());
                        break;
                    case SHARE_MULTIPLE_IMAGES_DATA:
                        //单个图片分享
                        ShareImageData shareImageData = shareData.getImageData();
                        imageView.setVisibility(View.VISIBLE);
                        String path = shareImageData.getPath();
                        Glide.with(getApplicationContext()).load(path).into(imageView);
                        break;
                    case SHARE_TEXT_DATA:
                        //文本分享
                        ShareTextData textData = shareData.getTextData();
                        break;
                    case SHARE_VCARD_DATA:
                        //明信片分享
                        ShareVCardData vCardData = shareData.getVCardData();
                        VCard vCard = vCardData.getVCard();
                        String fileName = null;
                        for (int i = 0; i < vCard.getStructuredNames().size(); i++) {
                            StructuredName structuredName = vCard.getStructuredNames().get(i);
                            if (structuredName != null) {
                                fileName = structuredName.getFamily();
                                fileName += "_" + structuredName.getGiven();
                            }
                        }
                        if (fileName == null) {
                            Toast.makeText(MainActivity.this, "给VCard一个名字吧", Toast.LENGTH_LONG).show();
                            break;
                        }
                        File file = vCardData.getFileBuilder()
                                .setFileName(fileName + ".vcf")
                                .setPath(Environment.getExternalStorageDirectory().getPath())
                                .createFile();
                        Log.d("Dboy", "save to : " + file.getAbsolutePath());
                        Toast.makeText(MainActivity.this, "save to : " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        break;
                }
            }


            @Override
            public void onError(String e) {
                Log.d("Dboy", "error =>" + e);
            }
        });
    }

    /**
     * 多组图片适配器
     */
    static class ImageAdapter extends BaseQuickAdapter<ShareImageData, BaseViewHolder> {

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
