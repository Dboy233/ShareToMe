package com.DBoy.share.to.me;

import ezvcard.VCard;

public class ShareVCardData extends BaseShareData {
    private String content;

    private VCard mVCard;


    public ShareVCardData(String content, VCard VCard) {
        this.content = content;
        mVCard = VCard;
    }

    @Override
    protected String getStrings() {
        return content;
    }
}
