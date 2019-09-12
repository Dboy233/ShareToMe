package com.is.myshare.tome;

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

    @Override
    protected String getStrings() {
        return toString();
    }

    @Override
    public String toString() {
        return "name='" + name + '\'' +
                ", path='" + path;
    }
}
