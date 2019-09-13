package com.DBoy.share.to.me;

import androidx.annotation.NonNull;

import static com.DBoy.share.to.me.BaseShareData.ShareDataType.SHARE_IMG_DATA;

public class ShareImageData extends BaseShareData {

    private String name;

    private String path;

    public ShareImageData(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    @NonNull
    @Override
    public String toString() {
        return "name='" + name + '\'' +
                ", path='" + path;
    }

    @Override
    public ShareDataType getDataType() {
        return SHARE_IMG_DATA;
    }
}
