package com.example.mystudy.aop;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Stack;

public class NetCheckUtils {
    public static final String TAG = "NetCheckUtils";
    private boolean isConnected = false;
    private boolean isRegistered = false;
    private Context context;
    private volatile static NetCheckUtils mUtils;
    private Stack<Activity> stack;

    private NetCheckUtils(Context context) {
        this.context = context.getApplicationContext();
        stack = new Stack<>();
    }

    public static NetCheckUtils getInstance(Context context) {
        if (mUtils == null) {
            synchronized (NetCheckUtils.class) {
                if (mUtils == null) {
                    mUtils = new NetCheckUtils(context);
                }
            }
        }
        return mUtils;
    }

    private final BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(@NonNull Context context, Intent intent) {
            boolean wasConnected = isConnected;
            isConnected = isConnected(context);
            if (wasConnected != isConnected) {
                l("connectivity changed, isConnected: " + isConnected);
                //网络连接变化
                notifyChanged(isConnected);
            }

            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NetType preType = mCurNetType;
                        mCurNetType = getNetType(context);
                        if (preType == mCurNetType) {
                            return;
                        }
                        netChange(mCurNetType);
                    }
                }, 1000);
            }
        }
    };

    private Activity getCurrentActivity() {
        return stack.lastElement();
    }

    private void netChange(NetType netType) {
        try {
            Method[] methods;
            Activity activity = getCurrentActivity();
            if (activity == null) {
                return;
            }
            Class<?> clazz = activity.getClass();
            methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                method.setAccessible(true);
                NetChange checkNet = method.getAnnotation(NetChange.class);
                if (checkNet != null) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length != 1) {
                        throw new RuntimeException(method.getName() + "Method can only have one parameter");
                    }
                    if (checkNet.netType() == netType && parameterTypes[0].equals(NetType.class)) {
                        method.invoke(activity, netType);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void notifyChanged(boolean conn) {
        try {
            Method[] methods;
            Activity activity = getCurrentActivity();
            if (activity == null) {
                return;
            }
            Class<?> clazz = activity.getClass();
            methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                method.setAccessible(true);
                NetChange checkNet = method.getAnnotation(NetChange.class);
                if (checkNet != null) {
                    method.invoke(activity, conn);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private NetworkInfo getNetInfo(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
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

    private boolean isNetAvailableByDns() {
        try {
            InetAddress inetAddress = InetAddress.getByName("www.baidu.com");
            return inetAddress != null;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void registerLifecycle() {
        ((Application) context).registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                //？？？？？？
                register(activity.getApplication().getApplicationContext());
                stack.push(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                stack.remove(activity);
//                ？？？？
                if (stack.empty()) {
                    unregister(activity.getApplication().getApplicationContext());
                }
            }
        });
    }

    private NetType mCurNetType;

    private void register(Context context) {
        if (isRegistered) {
            return;
        }
        isConnected = isConnected(context);
        mCurNetType = getNetType(context);
        l("register,isConnected->" + isConnected);
        try {
            context.registerReceiver(connectivityReceiver,
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            isRegistered = true;
        } catch (SecurityException e) {
            l("Failed to register" + e);
        }
    }

    private void unregister(Context context) {
        l("unregister,isRegistered->" + isRegistered);
        if (!isRegistered) {
            return;
        }
        context.unregisterReceiver(connectivityReceiver);
        isRegistered = false;
    }

    private void l(String msg) {
        Log.e(TAG, msg);
    }

    public static void init(Context context) {
        NetCheckUtils instance = getInstance(context);
        instance.registerLifecycle();
        instance.register(context);
    }

}
