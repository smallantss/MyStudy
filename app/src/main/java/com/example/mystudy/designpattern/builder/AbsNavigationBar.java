package com.example.mystudy.designpattern.builder;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AbsNavigationBar<B extends AbsNavigationBar.Builder> implements INavigationBar {

    private B mBuilder;
    private View mNavigationBar;

    //最终由这个类去调用
    AbsNavigationBar(B builder) {
        this.mBuilder = builder;
        mNavigationBar = LayoutInflater.from(mBuilder.mContext).inflate(mBuilder.mResId, mBuilder.mParent, false);
        attachParent(mNavigationBar, mBuilder.mParent);
        attachNavigationParams();
    }

    @Override
    public void attachParent(View view, ViewGroup parent) {
        parent.addView(view, 0);
    }

    @Override
    public void attachNavigationParams() {
        int textMapSize = mBuilder.mTextMap.size();
        for (int i = 0; i < textMapSize; i++) {
            TextView tv = fbi(mBuilder.mTextMap.keyAt(i));
            tv.setText((CharSequence) mBuilder.mTextMap.valueAt(i));
        }
        int clickMapSize = mBuilder.mClickMap.size();
        for (int i = 0; i < clickMapSize; i++) {
            View view = fbi(mBuilder.mClickMap.keyAt(i));
            view.setOnClickListener((View.OnClickListener) mBuilder.mClickMap.valueAt(i));
        }
    }

    public B getBuilder(){
        return mBuilder;
    }

    public <T> T fbi(int resId) {
        return (T) mNavigationBar.findViewById(resId);
    }

    //存放一些配置信息
    public static abstract class Builder {

        Context mContext;
        int mResId;
        ViewGroup mParent;
        SparseArray mTextMap;
        SparseArray mClickMap;

        protected Builder(Context context, int resId, ViewGroup parent) {
            this.mContext = context;
            this.mResId = resId;
            this.mParent = parent;
            mTextMap = new SparseArray<CharSequence>();
            mClickMap = new SparseArray<View.OnClickListener>();
        }

        public Builder setText(int resId, CharSequence text) {
            mTextMap.put(resId, text);
            return this;
        }

        public Builder setOnClickListener(int resId, View.OnClickListener listener) {
            mClickMap.put(resId, listener);
            return this;
        }

        public abstract AbsNavigationBar create();
    }
}
