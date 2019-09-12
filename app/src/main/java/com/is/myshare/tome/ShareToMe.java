package com.is.myshare.tome;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.Address;
import ezvcard.property.Email;
import ezvcard.property.Telephone;

public class ShareToMe {
    //文字分享
    private final static String TYPE_TEXT = "text/plain";
    //图片分享
    private final static String TYPE_IMG = "image/";
    // v card
    private final static String TYPE_VCARD = "text/x-vcard";


    private static String mType = null;
    private boolean isNotShare = false;


    public static void handleShareToMe(Context context, Intent intent, @NonNull HandleListener handleListener) {

        if (intent == null) {
            return;
        }

        if (!Objects.requireNonNull(intent.getAction()).equals(Intent.ACTION_SEND)) {
            return;
        }
        if (intent.getType() == null) {
            handleListener.handleError("类型空");
            return;
        }
        mType = intent.getType();
        handleListener.handleType(mType);
        if (mType.equals(TYPE_TEXT)) {
            handleText(intent, handleListener);
        } else if (mType.startsWith(TYPE_IMG)) {
            handleImage(context, intent, handleListener);
        } else if (mType.equals(TYPE_VCARD)) {
            handleVCard(context, intent, handleListener);
        }

    }

    /**
     * 处理通讯录
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

            byte[] vcardBt;
            String vcardString="null";
            VCard vCard=null;
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

            handleListener.handleContent(new ShareVCardData(vcardString,vCard));

        }
    }

    /**
     * 处理图片 得到路径
     */
    private static void handleImage(Context context, Intent intent, HandleListener handleListener) {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        String path = getRealFilePath(context, imageUri);
        if (path == null) {
            path = imageUri.getPath();
        }
        handleListener.handleContent(new ShareImageData("img", path));
    }

    /**
     * 处理文字
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
     * 获取文件的绝对路径
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public interface HandleListener {
        void handleType(String type);

        void handleContent(BaseShareData shareData);

        void handleError(String e);
    }


}
