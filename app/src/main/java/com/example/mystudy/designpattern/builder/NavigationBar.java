package com.example.mystudy.designpattern.builder;

import android.content.Context;
import android.view.ViewGroup;

public class NavigationBar extends AbsNavigationBar<NavigationBar.Builder> {

    protected NavigationBar(Builder builder) {
        super(builder);
    }

    public static class Builder extends AbsNavigationBar.Builder{

        public Builder(Context context, int resId, ViewGroup parent) {
            super(context, resId, parent);
        }

        @Override
        public AbsNavigationBar create() {
            return new NavigationBar(this);
        }
    }
}
