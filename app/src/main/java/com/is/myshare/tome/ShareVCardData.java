package com.is.myshare.tome;

import java.util.List;

import ezvcard.VCard;
import ezvcard.property.Address;
import ezvcard.property.Email;

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
