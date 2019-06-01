package com.example.mystudy

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.support.constraint.ConstraintLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.mystudy.aidl.DESCRIPTOR
import com.example.mystudy.aidl.MessengerService
import com.example.mystudy.aidl.MyBinder
import com.example.mystudy.aidl.MyService
import kotlinx.android.synthetic.main.activity_vp_indicator.*

class VpIndicatorActivity : AppCompatActivity() {

    val texts = listOf("第一页", "第二页", "第三页", "第四页")
    val colors = listOf("#FF8247", "#FA8072", "#EE30A7", "#BF3EFF")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vp_indicator)

        val dir = getExternalFilesDir(Environment.DIRECTORY_ALARMS)
        L(dir.absolutePath)

        bindRemoteService()

        bindMessenger()

        initViewPager()

        testCp()
    }

    private fun testCp() {
        val uri = Uri.parse("content://com.example.mystudy.provoder")
        contentResolver.query(uri,null,null,null,null)
    }

    private fun initViewPager() {
        viewPager.adapter = object : PagerAdapter() {

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val view = LayoutInflater.from(container.context).inflate(R.layout.layout_vp, container, false) as TextView
                view.text = texts[position]
                view.setBackgroundColor(Color.parseColor(colors[position]))
                container.addView(view)
                return view
            }

            override fun isViewFromObject(p0: View, p1: Any): Boolean {
                return p0 == p1
            }

            override fun getCount() = 4

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as View)
            }

        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            var distance: Int = 0
            var dis2 = 0
            var curPos = 0
            var newCurPos = 0
            var isStop = true
            var preOffset = 0f
            var isDrag = false

            override fun onPageScrollStateChanged(state: Int) {

                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    newCurPos = curPos
                    isDrag = false
                } else {
                    isDrag = true
                }
            }

            private var isNewPage = false

            override fun onPageScrolled(pos: Int, posOffset: Float, posOffsetPiexls: Int) {
                L("onPageScrolled:pos->$pos ,posOffset->$posOffset")
                val view2L = IntArray(2)
                val view1L = IntArray(2)
                val viewL = IntArray(2)
                val vieL = IntArray(2)
                view2.getLocationInWindow(view2L)
                view1.getLocationInWindow(view1L)
                view.getLocationInWindow(viewL)
                vie.getLocationInWindow(vieL)
                distance = view2L[0] - view1L[0]
                dis2 = viewL[0] - vieL[0]
    //                L("view1->${view1L[0]}")
    //                L("view2->${view2.width}")
    //                L("distance-> $distance")
    //                L("view-> ${viewL[0]},${viewL[1]}")
    //                L("vie-> ${vieL[0]},${vieL[1]}")
                if (posOffset != 0f && isDrag) {

                    if (posOffset > preOffset) {
                        //下一页

                        viewMove.translationX = newCurPos * distance + distance * posOffset

    //                        if (preOffset!=0f){
    //                            if (isNewPage){
    //                                viewMove.translationX = distance * posOffset + (newCurPos-1) * distance
    //                            }else{
    //
    //                            }
    //                        }
                    } else {
                        //上一页
                        viewMove.translationX = newCurPos * distance - distance * posOffset

    //                        if (newCurPos==0){
    //                            viewMove.translationX = distance * posOffset
    //                        }else{
    //                            if (isNewPage){
    //                                viewMove.translationX = (newCurPos) * distance + distance * posOffset
    //                            }else{
    //                                viewMove.translationX = (newCurPos-1) * distance + distance * posOffset
    //                            }
    //                        }

                    }
                }
                preOffset = posOffset
                isNewPage = false
            }

            //仅当新页面被选中的时候才调用
            override fun onPageSelected(pos: Int) {
                L("onPageSelected:pos-> $pos")
                curPos = pos
            }

        })
    }

    private fun bindRemoteService() {
        val intent = Intent(this, MyService::class.java)
        bindService(intent, mConn, Context.BIND_AUTO_CREATE)
    }

    private lateinit var binder: IBinder

    val mConn = object :ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {

        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            binder = service as MyBinder
        }

    }

    private fun bindMessenger() {
        val intent = Intent(this, MessengerService::class.java)
        bindService(intent, mMessengerConn, Context.BIND_AUTO_CREATE)
    }

    private lateinit var messenger: Messenger

    val mMessengerConn = object :ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {

        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            messenger = Messenger(service)
        }

    }

    //创建接受消息的Messenger对象
    val replyHandler = @SuppressLint("HandlerLeak")
    object :Handler(){
        override fun handleMessage(msg: Message?) {
            when(msg?.what){
                120->{
                    Log.e("TAG","接受到服务器返回的${msg.arg1}")
                }
            }
            super.handleMessage(msg)
        }
    }
    val replyMessenger = Messenger(replyHandler)



    fun onClick(v:View){
        //创建message，Messenger发送message
        val message = Message.obtain()
        message.what = 110
        message.arg1 = 5
        //相当于告诉了服务端是哪个客户端发的消息
        message.replyTo = replyMessenger
        messenger.send(message)

        val data = Parcel.obtain()
        val reply = Parcel.obtain()
        data.writeInterfaceToken(DESCRIPTOR)
        data.writeInt(3)
        data.writeInt(6)
        binder.transact(7,data,reply,0)
        Toast.makeText(this,"${reply.readInt()}",Toast.LENGTH_LONG).show()
        reply.recycle()
        data.recycle()
    }


    fun L(s: String) {
        Log.e("TAG", s)
    }

    fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).toInt()
    }

    fun dip2px(dipValue: Float): Int {
        val scale = resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

}
