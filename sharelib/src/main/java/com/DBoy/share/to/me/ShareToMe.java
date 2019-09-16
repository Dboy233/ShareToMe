package com.DBoy.share.to.me;

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
    //文字分享 Text sharing
    public final static String TYPE_TEXT = "text/plain";
    //图片分享 Picture sharing
    public final static String TYPE_IMG = "image/";
    // v card
    public final static String TYPE_VCARD = "text/x-vcard";


    private static String mType = null;


    public static void handleShareToMe(Context context, Intent intent, @NonNull HandleListener handleListener) {
        //没有intent数据
        //No intent data
        if (intent == null) {
            return;
        }

        mType = intent.getType();
        //没有数据类型
        //No data type
        if (mType == null) {
            return;
        }

        handleListener.handleType(mType);

        //判断分享类型
        //Judging the type of sharing
        if (Objects.requireNonNull(intent.getAction()).equals(Intent.ACTION_SEND)) {
            if (mType.equals(TYPE_TEXT)) {
                handleText(intent, handleListener);
            } else if (mType.startsWith(TYPE_IMG)) {
                handleImage(context, intent, handleListener);
            } else if (mType.equals(TYPE_VCARD)) {
                handleVCard(context, intent, handleListener);
            }
        } else if (Objects.requireNonNull(intent.getAction()).equals(Intent.ACTION_SEND_MULTIPLE)) {
            if (mType.startsWith(TYPE_IMG)) {
                handleMultipleImages(context, intent, handleListener);
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
    private static void handleMultipleImages(Context context, Intent intent, HandleListener handleListener) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris == null || imageUris.size() == 0) {
            handleListener.handleError("没有获取分享图片列表");
            return;
        }

        List<ShareImageData> shareImageData = new ArrayList<>();

        for (Uri uris : imageUris) {
            String[] realFilePath = getRealFilePath(context, uris);
            String imgName = Objects.requireNonNull(realFilePath)[0];
            String imgPath = Objects.requireNonNull(realFilePath)[1];
            shareImageData.add(new ShareImageData(imgName, imgPath));
        }
        handleListener.handleContent(new ShareMultipleImagesData(shareImageData));
    }

    /**
     * 处理通讯录
     * Handling Address Book vcard
     */
    private static void handleVCard(Context context, Intent intent, HandleListener handleListener) {
        if (intent.hasExtra(Intent.EXTRA_STREAM)) {
            Bundle extras = intent.getExtras();
            if (extras == null) {
                handleListener.handleError("No extras");
                return;
            }
            Uri uri = extras.getParcelable(Intent.EXTRA_STREAM);
            if (uri == null) {
                handleListener.handleError("No EXTRA_STREAM");
                return;
            }

            byte[] vcardBt=null;
            String vcardString = "null";
            VCard vCard = null;
            try (InputStream stream = context.getContentResolver().openInputStream(uri)) {

                if (stream == null) {
                    handleListener.handleError("Can't open stream for " + uri);
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
                handleListener.handleError(ioe.getMessage());
            }
            if (vCard == null) {
                handleListener.handleError("解析失败");
                return;
            }

            handleListener.handleContent(new ShareVCardData(vcardString, vCard,vcardBt));

        }
    }

    /**
     * 处理图片 得到路径
     * Processing Pictures to Get Paths
     */
    private static void handleImage(Context context, Intent intent, HandleListener handleListener) {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        String[] imgData = getRealFilePath(context, imageUri);
        handleListener.handleContent(new ShareImageData(Objects.requireNonNull(imgData)[0], Objects.requireNonNull(imgData)[1]));
    }

    /**
     * 处理文字
     * Processing text
     */
    private static void handleText(Intent intent, HandleListener handleListener) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        String sharedTitle = intent.getStringExtra(Intent.EXTRA_TITLE);
        if (sharedText == null) {
            sharedText = mType;
        }
        if (sharedTitle == null) {
            sharedTitle = mType;
        }
        handleListener.handleContent(new ShareTextData(sharedTitle, sharedText));
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

        String[] imgData = {name, imgPath};

        return imgData;
    }

    public interface HandleListener {
        /**
         * @param type 原始分享类型 image/* ,text/plain ,text/x-vcard
         *             Original Sharing Type image/* ,text/plain ,text/x-vcard
         */
        void handleType(String type);

        /**
         * @param shareData 处理分享数据的实体基类
         *                  Entity base classes for processing shared data
         */
        void handleContent(BaseShareData shareData);

        /**
         * @param e error
         */
        void handleError(String e);
    }


}
