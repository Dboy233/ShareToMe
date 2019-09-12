package com.DBoy.share.to.me;

public abstract class BaseShareData {



    @Override
    public String toString() {
        return getStrings();
    }

    protected abstract String getStrings();
}
