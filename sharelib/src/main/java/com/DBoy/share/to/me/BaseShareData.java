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
     * @return 判断是否是Image数据
     */
    public boolean isImageData() {
        return getDataType() == ShareDataType.SHARE_IMG_DATA;
    }

    /**
     * @return 基类自行强制转换数据为 ShareImageData
     */
    public ShareImageData getImageData() {
        return (ShareImageData) this;
    }

    /**
     * @return 判断是否是多组图片数据
     */
    public boolean isMultipleImagesData() {
        return getDataType() == ShareDataType.SHARE_MULTIPLE_IMAGES_DATA;
    }

    /**
     * @return 基类自行强制转换数据为 ShareMultipleImagesData
     */
    public ShareMultipleImagesData getMultipleImagesData() {
        return (ShareMultipleImagesData) this;
    }

    /**
     * @return 判断是否是文字数据
     */
    public boolean isTextData() {
        return getDataType() == ShareDataType.SHARE_TEXT_DATA;
    }

    /**
     * @return 基类自行强制转换数据为 ShareTextData
     */
    public ShareTextData getTextData() {
        return (ShareTextData) this;
    }

    /**
     * @return 判断是否是VCard数据
     */
    public boolean isVCardData() {
        return getDataType() == ShareDataType.SHARE_VCARD_DATA;
    }

    /**
     * @return 基类自行强制转换数据为 ShareVCardData
     */
    public ShareVCardData getVCardData() {
        return (ShareVCardData) this;
    }

    public enum ShareDataType {
        SHARE_IMG_DATA,
        SHARE_MULTIPLE_IMAGES_DATA,
        SHARE_TEXT_DATA,
        SHARE_VCARD_DATA
    }

}
