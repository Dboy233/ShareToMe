package com.DBoy.share.to.me;

import androidx.annotation.NonNull;

import java.util.List;

import static com.DBoy.share.to.me.BaseShareData.ShareDataType.SHARE_MULTIPLE_IMAGES_DATA;

/**
 * 多组图片
 */
public class ShareMultipleImagesData extends BaseShareData {


    private List<ShareImageData> imgPath;


    public ShareMultipleImagesData() {
    }

    public ShareMultipleImagesData(List<ShareImageData> imgPath) {
        this.imgPath = imgPath;
    }


    public List<ShareImageData> getImgPath() {
        return imgPath;
    }

    public void setImgPath(List<ShareImageData> imgPath) {
        this.imgPath = imgPath;
    }

    @NonNull
    @Override
    public String toString() {
        return "ShareMultipleImagesData{" +
                "imgPath=" + imgPath +
                '}';
    }

    @Override
    public ShareDataType getDataType() {
        return SHARE_MULTIPLE_IMAGES_DATA;
    }


}
