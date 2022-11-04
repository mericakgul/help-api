package com.mericakgul.helpapi.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DeletedDateUtil {

    private DeletedDateUtil(){

    }

    public static Date getDefaultDeletedDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            return simpleDateFormat.parse("1970-01-01 00:00:00.000");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
