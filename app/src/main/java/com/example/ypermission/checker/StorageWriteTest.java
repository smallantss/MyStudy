package com.example.ypermission.checker;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

class StorageWriteTest {

    static boolean check() {
        // 如果是非正常情况，没法检测，要返回true，不然会陷入死循环检测
        String state = Environment.getExternalStorageState();
        if (!TextUtils.equals(Environment.MEDIA_MOUNTED, state)) {
            return true;
        }
        File directory = Environment.getExternalStorageDirectory();
        return directory.exists() && directory.canWrite();
    }
}