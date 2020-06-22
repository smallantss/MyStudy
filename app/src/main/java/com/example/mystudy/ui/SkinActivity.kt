package com.example.mystudy.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import com.example.mystudy.R

class SkinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        LayoutInflaterCompat.setFactory2(LayoutInflater.from(this),this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skin)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        if (openSkin()){
            name?.let {
                return when(it){
                    "TextView"->{
                        TextView(context,attrs).apply {
                            setTextColor(Color.parseColor("#000000"))
                        }
                    }
                    "ImageView"->{
                        ImageView(context,attrs).apply {
                            if (id==R.id.ivBg){
                                setImageResource(R.drawable.bg_night)
                            }else if (id==R.id.ivHead){
                                setImageResource(R.drawable.ic_night)
                            }
                        }
                    }
                    "View"->{
                        View(context,attrs).apply {
                            setBackgroundColor(Color.parseColor("#FFFFFF"))
                        }
                    }
                    else->{
                        super.onCreateView(name, context, attrs)
                    }
                }
            }
        }

        return super.onCreateView(name, context, attrs)
    }

    private fun openSkin(): Boolean {
        return false
    }
}
