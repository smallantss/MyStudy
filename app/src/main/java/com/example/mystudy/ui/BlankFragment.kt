package com.example.mystudy.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mystudy.R
import com.example.mystudy.aop.NetChange
import com.example.mystudy.aop.NetCheckUtils2
import com.example.mystudy.aop.NetType
import kotlinx.android.synthetic.main.fragment_blank.*


private const val ARG_PARAM1 = "param1"
const val TAG = "BlankFragment"
class BlankFragment : Fragment() {

    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG,"onCreate")
        NetCheckUtils2.getInstance().register(this)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
        Log.e(TAG,"onCreatestate->"+lifecycle.currentState)
    }

    override fun onDetach() {
        NetCheckUtils2.getInstance().unregister(this)
        Log.e(TAG,"onDetachstate->"+lifecycle.currentState)
        super.onDetach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e(TAG,"onDestroyViewstate->"+lifecycle.currentState)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG,"onDestroystate->"+lifecycle.currentState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.e(TAG,"onCreateViewstate->"+lifecycle.currentState)
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(TAG,"onViewCreatedstate->"+lifecycle.currentState)
    }

    override fun onStart() {
        super.onStart()
        Log.e(TAG,"onStartstate->"+lifecycle.currentState)
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG,"onResumestate->"+lifecycle.currentState)
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG,"onPausestate->"+lifecycle.currentState)
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG,"onStopstate->"+lifecycle.currentState)
    }

    fun setData(s:String){
        Log.e(TAG,"setData")
        tv.text = s
    }

    @NetChange
    fun onNetChange(isConnect:Boolean){
        Log.e(TAG, "onNetChange->$isConnect")
    }

    @NetChange(netType = NetType.NET_NO)
    fun onChange(netType: NetType) {
        when (netType) {
            NetType.NET_NO -> {
                Log.e(TAG, "$param1 onNetChange->NET_NO")
            }
            NetType.NET_WIFI -> {
                Log.e(TAG, "$param1 onNetChange->NET_WIFI")
            }
            NetType.NET_MOBILE -> {
                Log.e(TAG, "$param1 onNetChange->NET_MOBILE")
            }
            NetType.NET_UNKNOWN -> {
                Log.e(TAG, "$param1 onNetChange->NET_UNKNOWN")
            }
            else -> {
                Log.e(TAG, "$param1 onNetChange->NET_ALL")
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
                BlankFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                    }
                }
    }
}
