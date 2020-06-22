package com.example.mystudy.rx.permissions;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * 权限封装类
 * 包括权限名 是否开启 是否不再提示等
 */
public class Permission {

    private String name;
    public boolean granted;
    private boolean shouldShowRequest;
    public Permission(String name ,boolean granted){
        this(name,granted,false);
    }

    public Permission(String name ,boolean granted,boolean shouldShowRequest){
        this.name = name;
        this.granted = granted;
        this.shouldShowRequest = shouldShowRequest;
    }

    public Permission(List<Permission> permissions){
        name = combineName(permissions);
        granted = combineGranted(permissions);
        shouldShowRequest = combineShowRequest(permissions);
    }

    /**
     * 将集合转为String
     */
    private String combineName(List<Permission> permissions){
        return Observable.fromIterable(permissions).map(new Function<Permission, String>() {
            @Override
            public String apply(Permission permission) throws Exception {
                return permission.name;
            }
        }).collectInto(new StringBuilder(), new BiConsumer<StringBuilder, String>() {
            @Override
            public void accept(StringBuilder sb, String s) throws Exception {
                if (s.length()==0){
                    sb.append(s);
                }else {
                    sb.append(", ").append(s);
                }
            }
        }).blockingGet().toString();
    }

    /**
     * 判断权限是否都同意
     */
    private boolean combineGranted(List<Permission> permissions){
        return Observable.fromIterable(permissions).all(new Predicate<Permission>() {
            @Override
            public boolean test(Permission permission) throws Exception {
                return permission.granted;
            }
        }).blockingGet();
    }

    /**
     * 判断权限是否都不再提示
     */
    private boolean combineShowRequest(List<Permission> permissions){
        return Observable.fromIterable(permissions).any(new Predicate<Permission>() {
            @Override
            public boolean test(Permission permission) throws Exception {
                return permission.shouldShowRequest;
            }
        }).blockingGet();
    }
}
