package com.DBoy.share.to.me;

import androidx.annotation.NonNull;

import ezvcard.VCard;

import static com.DBoy.share.to.me.BaseShareData.ShareDataType.SHARE_VCARD_DATA;

public class ShareVCardData extends BaseShareData {

    private String content;

    private VCard mVCard;

    private byte[] vCardBt;


    public ShareVCardData(String content, VCard VCard, byte[] vCardBt) {
        this.content = content;
        mVCard = VCard;
        this.vCardBt = vCardBt;
    }


    public byte[] getvCardBt() {
        return vCardBt;
    }

    public void setvCardBt(byte[] vCardBt) {
        this.vCardBt = vCardBt;
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
    public ShareDataType getDataType() {
        return SHARE_VCARD_DATA;
    }



    @NonNull
    @Override
    public String toString() {
        return content;
    }
}
