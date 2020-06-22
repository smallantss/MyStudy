package com.example.mystudy.designpattern.builder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mystudy.R;

public class DefaultNavigationBar extends AbsNavigationBar<DefaultNavigationBar.Builder> {

    protected DefaultNavigationBar(Builder builder) {
        super(builder);
    }

    @Override
    public void attachNavigationParams() {
        super.attachNavigationParams();
        TextView tv = fbi(R.id.tv_title);
        tv.setVisibility(getBuilder().leftVisible);
    }

    public static class Builder extends AbsNavigationBar.Builder{

        int leftVisible = View.VISIBLE;

        public Builder(Context context, ViewGroup parent) {
            super(context, R.layout.layout_title, parent);
        }

        @Override
        public AbsNavigationBar create() {
            return new DefaultNavigationBar(this);
        }

        public Builder setLeftText(CharSequence text){
            setText(R.id.tv_title,text);
            return this;
        }

        public Builder hideLeft(){
            leftVisible = View.INVISIBLE;
            return this;
        }
    }
}
