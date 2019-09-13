package com.DBoy.share.to.me;

import androidx.annotation.NonNull;

import static com.DBoy.share.to.me.BaseShareData.ShareDataType.SHARE_TEXT_DATA;

/**
 * 分享文本内容
 */
public class ShareTextData extends BaseShareData {

    private String title;
    private String content;

    public ShareTextData(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @NonNull
    @Override
    public String toString() {
        return "title='" + title + '\'' +
                ", content='" + content;
    }

    @Override
    public ShareDataType getDataType() {
        return SHARE_TEXT_DATA;
    }
}
