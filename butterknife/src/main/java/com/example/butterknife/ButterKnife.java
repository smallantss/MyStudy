package com.example.butterknife;

import android.app.Activity;

import java.lang.reflect.Constructor;

public class ButterKnife {

    public static Unbinder bind(Activity activity) {

        try {
            //获取xxx_ViewBinding
            Class<? extends Unbinder> bindClassName = (Class<? extends Unbinder>) Class.forName(activity.getClass().getName() + "_ViewBinding");
            Constructor<? extends Unbinder> bindConstructor = bindClassName.getConstructor(activity.getClass());
            Unbinder unbinder = bindConstructor.newInstance(activity);
            return unbinder;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Unbinder.EMPTY;
    }
}
