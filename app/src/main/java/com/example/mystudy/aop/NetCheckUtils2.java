package com.example.mystudy.aop;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NetCheckUtils2 {

    public static final String TAG = "NetCheckUtils2";
    private volatile static NetCheckUtils2 mUtils;
    private boolean isConnected = false;
    private boolean isRegistered = false;
    private NetType mCurNetType;
    private Map<Object, List<AnnotationMethod>> netObserverList;
    private Application application;

    private NetCheckUtils2() {
        mCurNetType = NetType.NET_NO;
        netObserverList = new HashMap<>();
    }

    public static NetCheckUtils2 getInstance() {
        if (mUtils == null) {
            synchronized (NetCheckUtils2.class) {
                if (mUtils == null) {
                    mUtils = new NetCheckUtils2();
                }
            }
        }
        return mUtils;
    }

    public void init(Application app) {
        if (isRegistered) {
            return;
        }
        application = app;
        try {
            application.registerReceiver(connectivityReceiver,
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            isRegistered = true;
        } catch (SecurityException e) {
            l("Failed to register" + e);
        }
    }

    private NetType getNetType(Context context) {
        NetworkInfo networkInfo = getNetInfo(context);
        if (networkInfo != null && networkInfo.isAvailable()) {
            switch (networkInfo.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    return NetType.NET_WIFI;
                case ConnectivityManager.TYPE_MOBILE:
                    return NetType.NET_MOBILE;
                default:
                    return NetType.NET_UNKNOWN;
            }
        } else {
            return NetType.NET_NO;
        }
    }

    private NetworkInfo getNetInfo(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    public void register(Object object) {
        isConnected = isConnected(application);
        mCurNetType = getNetType(application);
        l("register,isConnected->" + isConnected);
        List<AnnotationMethod> methodList = netObserverList.get(object);
        if (methodList == null) {
            methodList = findAnnotation(object);
            netObserverList.put(object, methodList);
        }
    }

    //找到注册类里面所有注解的方法
    private List<AnnotationMethod> findAnnotation(Object object) {
        List<AnnotationMethod> annotationMethods = new ArrayList<>();
        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            NetChange annotation = method.getAnnotation(NetChange.class);
            if (annotation != null) {
                //获取方法参数
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 1 && parameterTypes[0].isAssignableFrom(NetType.class)) {
                    annotationMethods.add(new AnnotationMethod(method, annotation));
//                    throw new RuntimeException(method.getName() + "Method can only have one parameter and parameterType must be NetType.");
                }
            }
        }
        return annotationMethods;
    }

    public void unregister(Object object) {
        if (!netObserverList.isEmpty()) {
            netObserverList.remove(object);
        }
    }

    private void l(String msg) {
        Log.e(TAG, msg);
    }

    private boolean isConnected(@NonNull Context context) {
        NetworkInfo networkInfo;
        try {
            networkInfo = getNetInfo(context);
        } catch (RuntimeException e) {
            l("Failed to determine connectivity status when connectivity changed" + e);
            return true;
        }
        return networkInfo != null && networkInfo.isConnected();
    }

    private final BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(@NonNull Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NetType preType = mCurNetType;
                        mCurNetType = getNetType(context);
                        if (preType == mCurNetType) {
                            return;
                        }
                        onNetChange(mCurNetType);
                    }
                }, 1000);
            }
        }
    };

    private void onNetChange(NetType netType) {
        try {
            Set<Object> keySet = netObserverList.keySet();
            for (Object object : keySet) {
                List<AnnotationMethod> methodList = netObserverList.get(object);
                if (methodList != null) {
                    for (AnnotationMethod annoMethod : methodList) {
                        NetType methodNet = annoMethod.netChange.netType();
                        if ( methodNet == NetType.NET_ALL || methodNet == netType) {
                            annoMethod.method.invoke(object, netType);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class AnnotationMethod {
    public Method method;
    public NetChange netChange;

    public AnnotationMethod(Method method, NetChange netChange) {
        this.method = method;
        this.netChange = netChange;
    }
}

