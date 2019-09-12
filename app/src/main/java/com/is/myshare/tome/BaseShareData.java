package com.is.myshare.tome;

public abstract class BaseShareData {



    @Override
    public String toString() {
        return getStrings();
    }

    protected abstract String getStrings();
}
