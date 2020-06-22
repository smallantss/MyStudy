package com.example.mystudy.rx.permissions;

import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.subjects.PublishSubject;

/**
 * 作为一个隐藏的Fragment
 */
public class RxFragment extends Fragment {

    private Map<String, PublishSubject<Permission>> mSubjects = new HashMap<>();

    public void requestPermissions(String[] permissions){
        requestPermissions(permissions,10);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode!=10)return;
        boolean[] shouldShowRequest = new boolean[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            shouldShowRequest[i] = shouldShowRequestPermissionRationale(permissions[i]);
        }
        onRequestPermissionsResult(permissions,grantResults,shouldShowRequest);
    }

    void onRequestPermissionsResult(String[] permissions,int[] grantResults,boolean[] shouldShowRequest){
        int size = permissions.length;
        for (int i = 0; i < size; i++) {
            PublishSubject<Permission> subject = mSubjects.get(permissions[i]);
            if (subject==null){
                return;
            }
            mSubjects.remove(permissions[i]);
            boolean granted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
            subject.onNext(new Permission(permissions[i],granted,shouldShowRequest[i]));
            subject.onComplete();
        }
    }

    PublishSubject<Permission> getSubjectByPermission(String permission){
        return mSubjects.get(permission);
    }

    void setSubjectForPermission(String permission,PublishSubject<Permission> subject){
        mSubjects.put(permission,subject);
    }

    boolean containPermission(String permission){
        return mSubjects.containsKey(permission);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isRevoked(String permission) {
        FragmentActivity activity = getActivity();
        return activity.getPackageManager().isPermissionRevokedByPolicy(permission,activity.getPackageName());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isGranted(String permission) {
        final FragmentActivity activity = getActivity();
        return activity.checkSelfPermission(permission)== PackageManager.PERMISSION_GRANTED;
    }
}
