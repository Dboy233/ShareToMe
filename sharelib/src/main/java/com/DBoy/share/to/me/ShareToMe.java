package com.DBoy.share.to.me;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ezvcard.Ezvcard;
import ezvcard.VCard;

public class ShareToMe {
    /**
     * 文字分享 Text sharing
     */
    public final static String TYPE_TEXT = "text/plain";
    /**
     * 图片分享 Picture sharing
     */
    public final static String TYPE_IMG = "image/";
    /**
     * VCard 分享
     */
    public final static String TYPE_VCARD = "text/x-vcard";

    /**
     * 处理分享给我的数据
     * @param activity 活动
     * @param shareCallBack 回调
     */
    public static void handleShareToMe(Activity activity, @NonNull ShareCallBack shareCallBack) {
        handleShareToMe(activity.getApplicationContext(),activity.getIntent(),shareCallBack);
    }

    /**
     * 处理分享给我的数据
     *
     * @param context        上下文
     * @param intent         分享意图
     * @param shareCallBack 分享处理回调
     */
    public static void handleShareToMe(Context context, Intent intent, @NonNull ShareCallBack shareCallBack) {
        //没有intent数据
        //No intent data
        if (intent == null) {
            return;
        }

        String type = intent.getType();
        //没有数据类型
        //No data type
        if (type == null) {
            return;
        }
        //通知得到分享类型
        shareCallBack.callType(type);

        //判断分享类型
        //Judging the type of sharing
        if (Objects.requireNonNull(intent.getAction()).equals(Intent.ACTION_SEND)) {
            if (type.equals(TYPE_TEXT)) {
                handleText(intent, shareCallBack);
            } else if (type.startsWith(TYPE_IMG)) {
                handleImage(context, intent, shareCallBack);
            } else if (type.equals(TYPE_VCARD)) {
                handleVCard(context, intent, shareCallBack);
            }
        } else if (Objects.requireNonNull(intent.getAction()).equals(Intent.ACTION_SEND_MULTIPLE)) {
            if (type.startsWith(TYPE_IMG)) {
                handleMultipleImages(context, intent, shareCallBack);
            }
        } else {
            //有待补充其他类型的分享
            //Other types of sharing to be added
        }

    }

    /**
     * 处理多组图片
     * Processing multiple sets of pictures
     */
    private static void handleMultipleImages(Context context, Intent intent, ShareCallBack shareCallBack) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris == null || imageUris.size() == 0) {
            shareCallBack.onError("没有获取分享图片列表");
            return;
        }

        List<ShareImageData> shareImageData = new ArrayList<>();

        for (Uri uris : imageUris) {
            String[] realFilePath = getRealFilePath(context, uris);
            if (realFilePath == null || realFilePath[0] == null || realFilePath[1] == null) {
                shareCallBack.onError("Uri :" + uris.toString() + "parsing failed");
                continue;
            }
            String imgName = realFilePath[0];
            String imgPath = realFilePath[1];
            shareImageData.add(new ShareImageData(imgName, imgPath));
        }
        shareCallBack.onSuccess(new ShareMultipleImagesData(shareImageData));
    }

    /**
     * 处理通讯录
     * Handling Address Book vcard
     */
    private static void handleVCard(Context context, Intent intent, ShareCallBack shareCallBack) {
        if (intent.hasExtra(Intent.EXTRA_STREAM)) {
            Bundle extras = intent.getExtras();
            if (extras == null) {
                shareCallBack.onError("Not have EXTRAS");
                return;
            }
            Uri uri = extras.getParcelable(Intent.EXTRA_STREAM);
            if (uri == null) {
                shareCallBack.onError("Not have EXTRA_STREAM");
                return;
            }

            byte[] vcardBt = null;
            String vcardString = "null";
            VCard vCard = null;
            try (InputStream stream = context.getContentResolver().openInputStream(uri)) {

                if (stream == null) {
                    shareCallBack.onError("Can't open stream for " + uri);
                    return;
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[2048];
                int bytesRead;
                while ((bytesRead = stream.read(buffer)) > 0) {
                    baos.write(buffer, 0, bytesRead);
                }
                vcardBt = baos.toByteArray();
                vcardString = new String(vcardBt, 0, vcardBt.length, StandardCharsets.UTF_8);
                vCard = Ezvcard.parse(vcardString).first();
            } catch (IOException ioe) {
                shareCallBack.onError(ioe.getMessage());
            }
            if (vCard == null) {
                shareCallBack.onError("VCard Parsing failed");
                return;
            }

            shareCallBack.onSuccess(new ShareVCardData(vcardString, vCard, vcardBt));

        }
    }

    /**
     * 处理图片 得到路径
     * Processing Pictures to Get Paths
     */
    private static void handleImage(Context context, Intent intent, ShareCallBack shareCallBack) {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        String[] imgData = getRealFilePath(context, imageUri);
        if (imgData == null||imgData[0]==null||imgData[1]==null) {
            shareCallBack.onError("Uri :"+imageUri.toString()+" Parsing failed");
            return;
        }
        shareCallBack.onSuccess(new ShareImageData(imgData[0], imgData[1]));
    }

    /**
     * 处理文字
     * Processing text
     */
    private static void handleText(Intent intent, ShareCallBack shareCallBack) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        String sharedTitle = intent.getStringExtra(Intent.EXTRA_TITLE);
        if (sharedText == null) {
            sharedText = "NULL";
        }
        if (sharedTitle == null) {
            sharedTitle = "NULL";
        }
        shareCallBack.onSuccess(new ShareTextData(sharedTitle, sharedText));
    }

    /**
     * 获取Img的绝对路径和名字
     * Get the absolute path and name of Img
     */
    private static String[] getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String name = null;
        String imgPath = null;
        if (scheme == null)
            imgPath = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            imgPath = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        imgPath = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        if (imgPath == null) {
            imgPath = uri.getPath();
        }

        int lastPoint = Objects.requireNonNull(imgPath).lastIndexOf(".");
        int startName = Objects.requireNonNull(imgPath).lastIndexOf("/");
        name = imgPath.substring(startName + 1, lastPoint);

        return new String[]{name, imgPath};
    }



    public interface ShareCallBack {
        /**
         * @param type 原始分享类型 image/* ,text/plain ,text/x-vcard
         *             Original Sharing Type image/* ,text/plain ,text/x-vcard
         */
        void callType(String type);

        /**
         * @param shareData 处理分享数据的实体基类
         *                  Entity base classes for processing shared data
         */
        void onSuccess(BaseShareData shareData);

        /**
         * @param e error
         */
        void onError(String e);
    }


}
