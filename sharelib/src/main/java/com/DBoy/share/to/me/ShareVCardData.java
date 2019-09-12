package com.DBoy.share.to.me;

import ezvcard.VCard;

public class ShareVCardData extends BaseShareData {
    private String content;

    private VCard mVCard;


    public ShareVCardData(String content, VCard VCard) {
        this.content = content;
        mVCard = VCard;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public VCard getVCard() {
        return mVCard;
    }

    public void setVCard(VCard VCard) {
        mVCard = VCard;
    }

    @Override
    protected String getStrings() {
        return content;
    }
}
