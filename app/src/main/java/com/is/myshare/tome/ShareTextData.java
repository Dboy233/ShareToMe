package com.is.myshare.tome;

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

    @Override
    protected String getStrings() {
        return toString();
    }

    @Override
    public String toString() {
        return "title='" + title + '\'' +
                ", content='" + content ;
    }
}
