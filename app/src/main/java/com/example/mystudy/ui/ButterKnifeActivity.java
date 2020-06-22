package com.example.mystudy.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.butterknife.ButterKnife;
import com.example.butterknife.Unbinder;
import com.example.butterknife.annotations.BindView;
import com.example.mystudy.R;

public class ButterKnifeActivity extends AppCompatActivity {

    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_butter_knife);

        unbinder = ButterKnife.bind(this);
//        ButterKnifeActivity_ViewBinding binding = new ButterKnifeActivity_ViewBinding(this);

        tv1.setText("changed1111");
        tv2.setText("changed2222");
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
