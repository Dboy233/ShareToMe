package com.DBoy.share.to.me;

import androidx.annotation.NonNull;

/**
 * 处理类型基类
 * <p>
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

    /**
     * @return 基类自行强制转换数据为 ShareImageData
     */
    public ShareImageData getImageData() {
        return (ShareImageData) this;
    }

    /**
     * @return 基类自行强制转换数据为 ShareMultipleImagesData
     */
    public ShareMultipleImagesData getMultipleImagesData() {
        return (ShareMultipleImagesData) this;
    }


    /**
     * @return 基类自行强制转换数据为 ShareTextData
     */
    public ShareTextData getTextData() {
        return (ShareTextData) this;
    }

    /**
     * @return 基类自行强制转换数据为 ShareVCardData
     */
    public ShareVCardData getVCardData() {
        return (ShareVCardData) this;
    }

    /**
     * 分享类型枚举
     */
    public enum ShareDataType {
        /**
         * 单图片类型数据{@link ShareImageData}
         */
        SHARE_IMG_DATA,
        /**
         * 多图片类型数据{@link ShareMultipleImagesData}
         */
        SHARE_MULTIPLE_IMAGES_DATA,
        /**
         * 文本类型数据{@link ShareTextData}
         */
        SHARE_TEXT_DATA,
        /**
         * Vcard类型数据{@link ShareVCardData}
         */
        SHARE_VCARD_DATA
    }

}
