// IRemote.aidl
package com.example.mystudy.aidl;
import com.example.mystudy.aidl.ICallback;
// Declare any non-default types here with import statements

interface IRemote {
    void sendData(String aString);
    String getData();
    void handleCallback(ICallback callback);
}
