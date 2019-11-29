package com.example.butterknife;

import android.app.Activity;

public class Utils {

    public static <T> T findViewById(Activity activity,int resId){
        return (T)activity.findViewById(resId);
    }
}
