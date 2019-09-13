package com.DBoy.share.to.me;

import androidx.annotation.NonNull;

/**
 * 处理类型基类
 *
 * 图片 img       =>{@link ShareImageData}
 * 多组图片 imgs   =>{@link ShareMultipleImagesData}
 * 文字 text      =>{@link ShareTextData}
 * vcard     =>{@link ShareVCardData}
 */
public abstract class BaseShareData {


    /**
     * @return 实体类 类型 ，通过它来强制转换数据
     */
    public abstract ShareDataType getDataType();

    @NonNull
    public abstract String toString();

    public enum ShareDataType {
        SHARE_IMG_DATA,
        SHARE_MULTIPLE_IMAGES_DATA,
        SHARE_TEXT_DATA,
        SHARE_VCARD_DATA
    }
}
