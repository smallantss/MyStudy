package com.example.mystudy

import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.mystudy.widgets.FlowLayoutManager
import com.example.mystudy.widgets.ItemPositionModel
import com.example.mystudy.widgets.ShowItem
import kotlinx.android.synthetic.main.activity_drag_word.*


/**
 * 拖动单词的Activity
 */
class DragWordActivity : AppCompatActivity() {

    val TAG = "MainActivity"
    private var list= ArrayList<ShowItem>()
    private val flowAdapter: FlowAdapter? = null
    private val res = arrayOf("He", "is", "our", "teacher")

    //记录正确答案的位置
    private val rightIndex = 4


    //记录最开始的位置
    private var firstX: Float = 0.toFloat()
    private var firstY: Float = 0.toFloat()

    //记录开始点击的View中的位置
    private var firstClickX: Float = 0.toFloat()
    private var firstClickY: Float = 0.toFloat()

    //记录屏幕差
    private var tempX: Float = 0.toFloat()
    private var tempY: Float = 0.toFloat()

    //流式布局LayoutManager
    private var flowLayoutManager: FlowLayoutManager? = null

    //存储点的位置
    private var itemList = ArrayList<ItemPositionModel>()

    //长按触发移动单词 记录当前是否可以移动
    private var canMove: Boolean = false
    //记录当前被移动单词的中点坐标
    private val center = Point()

    //记录被移动视图的大小
    private var mViewWidth: Float = 0.toFloat()
    private var mViewHeight: Float = 0.toFloat()

    //记录recyclerview的位置
    private var rvX: Float = 0.toFloat()
    private var rvY: Float = 0.toFloat()
    private var rvHeight: Float = 0.toFloat()

    //记录当前插入的
    private var currSelectIndex = -1

    //记录当前被移动的块是否在范围内
    private var isInArea: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag_word)
        //设置正确答案
        tv_right.text = "He is not our teacher."

        flowLayoutManager = FlowLayoutManager()
        recycler_view.layoutManager = flowLayoutManager
        //如果题干有四个单词 那么占位空格就有5个 具体逻辑其实很简单
        for (i in 0 until res.size * 2 + 1) {
            if (i % 2 == 0) {
                //加入占位空格
                list.add(ShowItem("", 1))
            } else {
                //加入真实数据
                list.add(ShowItem(res[(i - 1) / 2], 0))
            }
        }
        recycler_view.adapter = FlowAdapter(list)

        flow_text.setOnLongClickListener(object : View.OnLongClickListener{
            override fun onLongClick(v: View): Boolean {
                if (itemList.size !== list.size) {
                    mViewWidth = v.width.toFloat()
                    mViewHeight = v.height.toFloat()
                    rvX = recycler_view.x
                    rvY = recycler_view.y
                    rvHeight = recycler_view.height.toFloat()
                    //在此处处理对各个item位置信息的保存
                    for (i in 0 until list.size) {
                        val itemView = flowLayoutManager!!.findViewByPosition(i)
                        itemList.add(ItemPositionModel(((rvX + itemView!!.left).toInt()),
                                ((rvY + itemView.top).toInt()) , ((rvX + itemView.right).toInt()),
                                ((rvY + itemView.bottom).toInt()), i))
                    }
                }
                canMove = true
                return true
            }
        })

        flow_text.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                //                Log.e(TAG, "v x:" + v.getX() + " v y:" + v.getY() + " e x:" + event.getX() + " e y:" + event.getY() + " ee x:" + event.getRawX() + " ee y:" + event.getRawY());
                if (event.action == MotionEvent.ACTION_DOWN) {
                    //                    Log.e(TAG, "ACTION_DOWN");
                    //记录当前view的位置
                    firstX = v.getX()
                    firstY = v.getY()
                    firstClickX = event.x
                    firstClickY = event.y
                    tempX = event.rawX - event.x - firstX
                    tempY = event.rawY - event.y - firstY
                } else if (event.action == MotionEvent.ACTION_MOVE) {
                    if (!canMove)
                        return false
                    //                    Log.e(TAG, "ACTION_MOVE  " + (event.getRawX() - firstClickX) + "   " + (event.getRawY() - firstClickY));
                    //移动的时候
                    val positionX = event.rawX - firstClickX - tempX
                    val positionY = event.rawY - firstClickY - tempY
                    v.x = positionX
                    v.y = positionY

                    //被移动块的中点
                    val centerX = (positionX + mViewWidth / 2)
                    val centerY = (positionY + mViewHeight / 2)
                    if (centerY > rvY && centerY < rvHeight + rvY) {
                        isInArea = true
                        //计算被移动点的中点
                        center.set(centerX.toInt(), centerY.toInt())
                        //找出最近的点
                        val point = findPoint()
                        if (point != null) {
                            currSelectIndex = point!!.getPosition()
                            flowAdapter?.notifyDataSetChanged()
                        }
                    } else {
                        currSelectIndex = -1
                        isInArea = false
                        flowAdapter?.notifyDataSetChanged()
                    }
                } else if (event.action == MotionEvent.ACTION_UP) {
                    //                    Log.e(TAG, "ACTION_UP");
                    //如果在RecyclerView的范围内才处理 否则回退到原地
                    if (isInArea) {
                        //添加成功 移除之前的视图
                        v.setVisibility(View.GONE)
                        //检查并设置结果 最好提取出来
                        val result = ShowItem((v as TextView).text.toString(), 0)
                        if (rightIndex === currSelectIndex) {
                            //正确
                            result.setIsRight(1)
                        } else {
                            //错误
                            result.setIsRight(2)
                        }
                        list.add(currSelectIndex + 1, result)
                        list.add(currSelectIndex + 2, ShowItem("", 1))
                        //设置文字
                        val resultBuffer = StringBuffer()
                        for (s in list) {
                            if (s.type == 0) {
                                resultBuffer.append(s.des + " ")
                            }
                        }
                        //获取用户答题的结果 这个可以由后台返回
                        val yourAnswer = resultBuffer.substring(0, resultBuffer.length - 1) + "."
                        val midStr = (v as TextView).text.toString()
                        val start = yourAnswer.indexOf(midStr)
                        val sp = SpannableString(yourAnswer)
                        if (rightIndex === currSelectIndex) {
                            //正确
                            sp.setSpan(ForegroundColorSpan(Color.parseColor("#7CB92F")),
                                    start, start + midStr.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        } else {
                            //错误
                            sp.setSpan(ForegroundColorSpan(Color.parseColor("#D62119")),
                                    start, start + midStr.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }
                        //设置用户答题内容
                        tv_your.text = sp
                        //显示结果
                        ll_answer.visibility = View.VISIBLE
                        //设置下一题
                        tv_jump.text = "下一题"
                    } else {
                        //未成功添加抬起的时候回归原地
                        v.x = firstX
                        v.y = firstY
                    }
                    //
                    canMove = false
                    currSelectIndex = -1
                    flowAdapter?.notifyDataSetChanged()
                }
                return false
            }
        })
    }

    //找出最近的点 只找没有内容的格子
    private fun findPoint(): ItemPositionModel? {
        if (itemList.isEmpty())
            return null
        var distance = Math.sqrt(Math.pow((center.x - itemList[0].getCenter()!!.x).toDouble(), 2.0) + Math.pow((center.y - itemList[0].getCenter()!!.y).toDouble(), 2.0))
        var index = 0
        for (i in 1 until itemList.size) {
            if (i % 2 == 0) {
                val temp = Math.sqrt(Math.pow((center.x - itemList[i].getCenter()!!.x).toDouble(), 2.0) + Math.pow((center.y - itemList[i].getCenter()!!.y).toDouble(), 2.0))
                //                Log.e(TAG, "距离" + temp);
                if (temp <= distance) {
                    distance = temp
                    index = i
                }
            }
        }
        //        Log.e(TAG, "位置" + index);
        return itemList.get(index)
    }


    internal inner class FlowAdapter(private val list: List<ShowItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == 0) {
                //正文内容类型
                MyHolder(View.inflate(this@DragWordActivity,R.layout.flow_item, null))
            } else {
                //占位符类型
                MyHolderDivider(View.inflate(this@DragWordActivity, R.layout.flow_divider, null))
            }
        }

        override fun getItemViewType(position: Int): Int {
            return list[position].type
        }

        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val showItem = list[position]
            if (showItem != null)
                if (showItem!!.type== 0) {
                    val textView = (holder as MyHolder).text
                    textView.setText(list[position].des)
                    if (showItem!!.getIsRight() == 1) {
                        //正确
                        textView.background = resources.getDrawable(R.drawable.shape_green_fram_round)
                        textView.setTextColor(Color.WHITE)
                    } else if (showItem!!.getIsRight() == 2) {
                        //错误
                        textView.background = resources.getDrawable(R.drawable.shape_red_fram_round)
                        textView.setTextColor(Color.WHITE)
                    } else {
                        //按以前的来
                        textView.background = resources.getDrawable(R.drawable.shape_white_fram_round)
                        textView.setTextColor(Color.parseColor("#444444"))
                    }
                } else {
                    if (currSelectIndex === position) {
                        (holder as MyHolderDivider).tv_divider.visibility = View.VISIBLE
                    } else {
                        (holder as MyHolderDivider).tv_divider.visibility = View.GONE
                    }
                }
        }

        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val text: TextView = itemView.findViewById(R.id.flow_text) as TextView

        }

        internal inner class MyHolderDivider(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val tv_divider: TextView = itemView.findViewById(R.id.tv_divider) as TextView

        }
    }
}
