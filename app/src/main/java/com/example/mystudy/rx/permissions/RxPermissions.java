package com.example.mystudy.rx.permissions;

import android.os.Build;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

/**
 * rxPermissions
 * .request(Manifest.permission.CAMERA)
 * .subscribe(granted -> {
 * if (granted) {
 * <p>
 * } else {
 * <p>
 * }
 * });
 */
public class RxPermissions {

    private static final String TAG = "RxFragment";
    private RxFragment rxFragment;

    public RxPermissions(FragmentActivity activity) {
        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(TAG);
        if (fragment != null) {
            rxFragment = (RxFragment) fragment;
        } else {
            rxFragment = new RxFragment();
            fm.beginTransaction().add(rxFragment, TAG).commitNow();
        }
    }

    public Observable<Boolean> request(String... permissions) {
        return Observable.just(new Object()).compose(ensure(permissions));
    }

    private <T> ObservableTransformer<T, Boolean> ensure(final String... permissions) {
        return new ObservableTransformer<T, Boolean>() {
            @Override
            public ObservableSource<Boolean> apply(Observable<T> o) {
//                return Observable.just(new Object())
//                        .flatMap(new Function<Object, Observable<Permission>>() {
//                            @Override
//                            public Observable<Permission> apply(Object o) throws Exception {
//                                return requestImpl(permissions);
//                            }
//                        })
//                        .buffer(permissions.length)
//                        .flatMap(new Function<List<Permission>, ObservableSource<Boolean>>() {
//                            @Override
//                            public ObservableSource<Boolean> apply(List<Permission> permissions) throws Exception {
//                                if (permissions.isEmpty()){
//                                    return Observable.empty();
//                                }
//                                for (Permission permission : permissions) {
//                                    if (!permission.granted){
//                                        return Observable.just(false);
//                                    }
//                                }
//                                return Observable.just(true);
//                            }
//                        });

                return Observable.just(new Object())
                        .flatMap(new Function<Object, Observable<Permission>>() {
                            @Override
                            public Observable<Permission> apply(Object o) throws Exception {
                                return requestImpl(permissions);
                            }
                        })
                        .flatMap(new Function<Permission, ObservableSource<Boolean>>() {
                            @Override
                            public ObservableSource<Boolean> apply(Permission permission) throws Exception {
                                if (permission.granted){

                                }
                                return null;
                            }
                        });
            }
        };
    }

    private Observable<Permission> requestImpl(final String... permissions) {
        List<Observable<Permission>> list = new ArrayList<>(permissions.length);
        List<String> unrequestPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (isGranted(permission)){
                list.add(Observable.just(new Permission(permission,true,false)));
                continue;
            }
            if (isRevoked(permission)){
                list.add(Observable.just(new Permission(permission,false,false)));
                continue;
            }

            PublishSubject<Permission> subject = rxFragment.getSubjectByPermission(permission);
            if (subject==null){
                unrequestPermissions.add(permission);
                subject = PublishSubject.create();
                rxFragment.setSubjectForPermission(permission,subject);
            }
            list.add(subject);
        }
        if (!unrequestPermissions.isEmpty()){
            String[] unrequestPermissionArray = unrequestPermissions.toArray(new String[unrequestPermissions.size()]);
            requestPermissionFromFragment(unrequestPermissionArray);
        }
        Log.e("TAG",list.toString());
        return Observable.concat(Observable.fromIterable(list));
    }

    private void requestPermissionFromFragment(String[] permissionArray) {
        rxFragment.requestPermissions(permissionArray);
    }

    //Revoked by a policy, return a denied Permission object
    private boolean isRevoked(String permission) {
        return !isMarshmallow() || rxFragment.isRevoked(permission);
    }

    private boolean isGranted(String permission) {
        return !isMarshmallow() || rxFragment.isGranted(permission);
    }

    private boolean isMarshmallow(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

}
