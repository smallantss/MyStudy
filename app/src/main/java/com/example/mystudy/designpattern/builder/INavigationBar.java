package com.example.mystudy.designpattern.builder;

import android.view.View;
import android.view.ViewGroup;

public interface INavigationBar {

    void attachParent(View view, ViewGroup parent);

    void attachNavigationParams();

}
