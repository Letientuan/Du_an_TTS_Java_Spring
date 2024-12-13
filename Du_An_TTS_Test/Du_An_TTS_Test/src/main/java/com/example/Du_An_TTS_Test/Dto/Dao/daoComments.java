package com.example.Du_An_TTS_Test.Dto.Dao;

import com.example.Du_An_TTS_Test.Entity.Comments;
import com.example.Du_An_TTS_Test.Entity.Products;
import com.example.Du_An_TTS_Test.Entity.Users;

import javax.xml.crypto.Data;
import java.util.Date;

public class daoComments {
    public Boolean saveComments(Comments comments) {

        if (validateConments(comments)==false) {
            return false;
        }
        return true;
    }

    public Boolean validateConments(Comments comments) {
        if (!isValidText(comments.getComment_text())){
            return false;
        }
        if (!isValiProduct(comments.getProduct_id())){
            return false;
        }

        if (!isValiUser(comments.getUser_id())){
            return false;
        }
        return null;
    }


    private boolean isValidText(String Text) {
        return Text != null && !Text.trim().isEmpty() && Text.length() <= 255;
    }
    private Boolean isValiProduct(Products products){
        return products != null;
    }
    private Boolean isValiUser(Users users){
        return users != null;
    }


}
