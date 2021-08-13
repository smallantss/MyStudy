package com.example.opengl.opencv

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.mystudy.BuildConfig
import com.example.mystudy.R
import com.example.mystudy.loge
import com.example.mystudy.service.InstallApkBroadCastReceiver
import com.example.mystudy.silenceInstall
import kotlinx.android.synthetic.main.activity_opencv.*
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import kotlin.experimental.and

class OpencvActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opencv)
        tvVersion.text = BuildConfig.VERSION_NAME
        loadOpenCV()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        }
        btn1.setOnClickListener {
//            OpenCvUtil().changeCompare(this,iv)
//            normalizeMat()
            silenceInstall(Environment.getExternalStorageDirectory().absolutePath + "/app-debug.apk")
        }
        btn2.setOnClickListener {
            addWeight(1.5, -0.5, 100.0)
        }
        btn3.setOnClickListener {
            addWeight(0.5, 0.5, 0.0)
        }
        btn4.setOnClickListener {
            addWeight(0.5, 0.5, 100.0)
        }
    }

    private fun normalizeMat(){
        val src = Mat(400, 400, CvType.CV_32FC3)
        val data = FloatArray(400 * 400 * 3)
        val random = java.util.Random()
        for (i in data.indices){
            data[i] = random.nextGaussian().toFloat()
        }
        src.put(0, 0, data)
        val dst = Mat()
        //归一化处理，NORM_MINMAX代表最小与最大值归一化方法
        //dst = (x-min)/(max-min)*(b-a)+a
        //x代表src像素值，min、max代表src像素中最小最大值
        //对src各个通道计算完成得到最终归一化结果
        //若结果有正负值，则显示前调用convertScaleAbs来对负值取绝对值图像
        Core.normalize(src, dst, 0.0, 255.0, Core.NORM_MINMAX, -1, Mat())
        val dst8U = Mat()
        //类型转换
        dst.convertTo(dst8U, CvType.CV_8UC3)
        //创建了400*400的高斯噪声图像
        simpleMatToBitmap(dst8U)
    }

    private fun otherMatOperator(){
        val bitmap1 = BitmapFactory.decodeResource(resources, R.mipmap.a)
        val src1 = Mat()
        Utils.bitmapToMat(bitmap1, src1)
        val bitmap2 = BitmapFactory.decodeResource(resources, R.mipmap.black)
        val src2 = Mat()
        Utils.bitmapToMat(bitmap2, src2)
        val res = Mat()
        //取反：常用于二值操作
        Core.bitwise_not(src1, res)
        //取与：混合两张图象，可以降低混合图像亮度，输出图像任意位置像素小于等于任意输入图像的像素值
        Core.bitwise_and(src1, src2, res)
        //取或：混合两张图象，可以提高混合图像亮度，输出图像任意位置像素大于等于任意输入图像的像素值
        Core.bitwise_or(src1, src2, res)
        //异或：对输入图像的叠加取反
        Core.bitwise_xor(src1, src2, res)

        val dst = Mat()
        //线性绝对值放缩变换：输入Mat对象数据求绝对值，并转为CV_8UC1类型的输出Mat
        Core.convertScaleAbs(res, dst)
        //归一化：输入res归一化范围为[low,high]，输出图像dst类型默认res类型，遮罩层默认Mat()
        val low = 0.0
        val high = 10.0
        Core.normalize(res, dst, low, high)
    }

    private fun addWeight(a: Double, b: Double, c: Double) {
        val bitmap1 = BitmapFactory.decodeResource(resources, R.mipmap.a)
        val src1 = Mat()
        Utils.bitmapToMat(bitmap1, src1)
        val bitmap2 = BitmapFactory.decodeResource(resources, R.mipmap.black)
        val src2 = Mat()
        Utils.bitmapToMat(bitmap2, src2)
        val res = Mat()
        //res = src1*a+src2*b+c
        Core.addWeighted(src1, a, src2, b, c, res)
        simpleMatToBitmap(res)
    }

    private fun addLight() {
        val bitmap1 = BitmapFactory.decodeResource(resources, R.mipmap.a)
        val src1 = Mat()
        Utils.bitmapToMat(bitmap1, src1)
        val res = Mat()
        //b>0,调亮;<0，调暗
        val b = -100.0
        Core.add(src1, Scalar(b, b, b), res)
        simpleMatToBitmap(res)
    }

    private fun addCompare2(index: Int) {
        val bitmap1 = BitmapFactory.decodeResource(resources, R.mipmap.d)
        val src1 = Mat()
        Utils.bitmapToMat(bitmap1, src1)
        loge("src1:${src1.channels()}")
        var list = ArrayList<Mat>()
        Core.split(src1, list)
        list.removeAt(index)
        val newSrc = Mat()
        Core.merge(list, newSrc)
        loge("newSrc:${newSrc.channels()}")
        simpleMatToBitmap(newSrc)
//        val res = Mat()
//        //b在 0~3.0，>1提高对比度，<1降低对比度。
//        val b = 2.0
//        Core.multiply(newSrc, Scalar(b, b, b), res)
//        simpleMatToBitmap(res)
    }

    private fun addCompare() {
        val bitmap1 = BitmapFactory.decodeResource(resources, R.mipmap.a)
        val src1 = Mat()
        Utils.bitmapToMat(bitmap1, src1)
        loge(src1.channels().toString())
        val res = Mat()
        //b在 0~3.0，>1提高对比度，<1降低对比度。
        val b = 0.8
        Core.multiply(src1, Scalar(b, b, b, b), res)
        simpleMatToBitmap(res)
    }

    private fun matMathOperator() {
        val bitmap1 = BitmapFactory.decodeResource(resources, R.mipmap.a)
        val src1 = Mat()
        Utils.bitmapToMat(bitmap1, src1)
        //根据Mat1创建一个Mat2
        val src2 = Mat.zeros(src1.rows(), src1.cols(), src1.type())
        //在Mat2上的中心绘制一个⚪
        Imgproc.circle(src2, Point(src1.cols() / 2.toDouble(), src1.rows() / 2.toDouble()), 60, Scalar(0.0, 0.0, 255.0, 0.0), -1, 8, 0)
        val res = Mat()
        Core.add(src1, src2, res)
        simpleMatToBitmap(res)
    }

    //获取均值与方差
    private fun getAverageAndVariance() {
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.white)
        val mat = Mat()
        Utils.bitmapToMat(bitmap, mat)
        logMat(mat)
        val gray = Mat()
        //转为灰度图像
        Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY)
        val means = MatOfDouble()
        val stddevs = MatOfDouble()
        //各个通道的均值/方差，长度与channel一致
        Core.meanStdDev(gray, means, stddevs)
        loge("均值：${means.toArray().contentToString()}")
        loge("方差：${stddevs.toArray().contentToString()}")
        val width = gray.cols()
        val height = gray.rows()
        val data = ByteArray(gray.channels() * width * height)
        gray.get(0, 0, data)
        val t = means.toArray()[0].toInt()
        var pv: Int
        for (i in data.indices) {
            pv = data[i].toInt() and 0xff
            //根据均值进行二值分割
            if (pv > t) {
                data[i] = 255.toByte()
            } else {
                data[i] = 0
            }
        }
        gray.put(0, 0, data)
        //将dst对象转为bitmap
        Utils.matToBitmap(gray, bitmap)
        iv.setImageBitmap(bitmap)
    }

    fun changeColor(context: Context, iv: ImageView) {
        val bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.a)
        val mat = Mat()
        Utils.bitmapToMat(bitmap, mat)
        val gray = Mat()
        //转为灰度图像
        Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY)
        val means = MatOfDouble()
        val stddens = MatOfDouble()
        Core.meanStdDev(gray, means, stddens)
        loge("均值：${means.toArray().contentToString()}")
        loge("方差：${stddens.toArray().contentToString()}")
        val mean = means.toArray()
        val stdden = stddens.toArray()
        val width = gray.cols()
        val height = gray.rows()
        val data = ByteArray(width * height)
        gray[0, 0, data]
        var pv = 0
        val t = mean[0].toInt()
        for (i in data.indices) {
            //byte转int，先byte转int再&0xff
            pv = data[i].toInt() and 0xff
            if (pv > t) {
                data[i] = 255.toByte()
            } else {
                data[i] = 0
            }
        }
        gray.put(0, 0, data)
        simpleMatToBitmap(gray)
    }

    private fun simpleMatToBitmap(mat: Mat) {
        val bitmap1 = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(mat, bitmap1)
        iv.setImageBitmap(bitmap1)
    }

    private fun splitAndMerge() {
        var bitmap = BitmapFactory.decodeResource(resources, R.mipmap.a)
        val dst = Mat()
        Utils.bitmapToMat(bitmap, dst)
        logMat(dst)
        val splitList = ArrayList<Mat>()
        if (dst.channels() > 1) {
            //源Mat分割成多个Mat
            Core.split(dst, splitList)
        }
        splitList.forEach {
            logMat(it)
        }
        //创建目标dst对象
        val ndst = Mat()
        Core.merge(splitList, ndst)
        //根据源mat大小创建bitmap
        bitmap = Bitmap.createBitmap(ndst.cols(), ndst.rows(), Bitmap.Config.ARGB_8888)
        //将源mat对象颜色值复制到dst对象上
        val m = Mat(ndst.rows(), ndst.cols(), CvType.CV_8UC3)
        Imgproc.cvtColor(ndst, m, Imgproc.COLOR_BGR2RGBA)
        //将dst对象转为bitmap
        Utils.matToBitmap(m, bitmap)
        iv.setImageBitmap(bitmap)
    }

    private fun handleMatByTotal() {
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.a)
        val mat = Mat()
        Utils.bitmapToMat(bitmap, mat)
        val channels = mat.channels()
        val width = mat.cols()
        val height = mat.rows()
        val data = ByteArray(channels * width * height)
        mat.get(0, 0, data)
        val t = data.size / 2
        for (i in 0 until t) {
            data[i] = (100 - (data[i].toInt() and 0xff)).toByte()
        }
        mat.put(0, 0, data)
        Utils.matToBitmap(mat, bitmap)
        iv.setImageBitmap(bitmap)
    }

    private fun handleMatByRow() {
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.a)
        val dst = Mat()
        Utils.bitmapToMat(bitmap, dst)
        logMat(dst)
        val channels = dst.channels()
        val width = dst.cols()
        val height = dst.rows()
        val data = ByteArray(channels * width)
        var pv: Int
        for (row in 0 until height / 2) {
            //以(0,0)位置为起点，获取data长度的数据
            dst.get(row, 0, data)
            for (col in data.indices) {
                //遍历宽度，修改每个值
                pv = (data[col].toInt() and 0xff)
                pv = 100 - pv
                data[col] = pv.toByte()
            }
            dst.put(row, 0, data)
        }
        Utils.matToBitmap(dst, bitmap)
        iv.setImageBitmap(bitmap)
    }

    private fun handleMatByPixel() {
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.a)
        val dst = Mat()
        Utils.bitmapToMat(bitmap, dst)
        logMat(dst)
        val channels = dst.channels()
        val width = dst.cols()
        val height = dst.rows()
        val data = ByteArray(channels)
        var b: Byte
        var g: Byte
        var r: Byte
        for (row in 0 until 1) {
            for (col in 0 until 1) {
                //将像素点的数据保存到data里
                dst.get(row, col, data)
                loge("before data:${data.contentToString()}")
                b = data[0] and 0xff.toByte()
                g = data[1] and 0xff.toByte()
                r = data[2] and 0xff.toByte()
                loge("before r:${r},g:${g},b:${b}")
                //取反
                b = (255 - b).toByte()
                g = (255 - g).toByte()
                r = (255 - r).toByte()
                loge("after r:${r},g:${g},b:${b}")
                //保存
                data[0] = b
                data[1] = g
                data[2] = r
                dst.put(row, col, data)
            }
        }
        Utils.matToBitmap(dst, bitmap)
        iv.setImageBitmap(bitmap)
    }

    private fun bitmapToMat() {
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.a)
        val dst = Mat()
        //空的bitmap转为mat
        Utils.bitmapToMat(bitmap, dst)
        logMat(dst)
    }

    //mat转bitmap
    private fun matToBitmap() {
        //创建mat源对象
        val mat = Mat(100, 100, CvType.CV_8UC3)
        //根据源mat大小创建bitmap
        val bitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888)
        //创建目标dst对象
        val dst = Mat()
        //将源mat对象颜色值复制到dst对象上
        Imgproc.cvtColor(mat, dst, Imgproc.COLOR_BGR2RGBA)
        //将dst对象转为bitmap
        Utils.matToBitmap(dst, bitmap)
    }

    //mat转bitmap
    private fun matToBitmap2() {
        val mat = Mat(100, 100, CvType.CV_8UC3)
        val bitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888)
        val m = Mat()
        //将bitmap转为空的Mat对象
        Utils.bitmapToMat(bitmap, m)
        //在m上绘制
        Imgproc.circle(m, Point(10.0, 10.0), 10, Scalar(0.0, 255.0, 0.0))
        //绘制完成转为bitmap
        Utils.matToBitmap(m, bitmap)
    }

    //mat上绘制
    private fun matDraw() {
        val mat = Mat(100, 100, CvType.CV_8UC3)
        //背景红色
        mat.setTo(Scalar(0.0, 0.0, 255.0, 255.0))
        val start = Point(10.0, 10.0)
        val end = Point(20.0, 20.0)
        //mat对象，起点，终点，宽度(像素)，，偏移0像素,绿色线
        Imgproc.line(mat, start, end, Scalar(0.0, 255.0, 0.0, 255.0), 1, 8, 0)
        //在end处绘制半径为10的⚪，颜色为blue
        Imgproc.circle(mat, end, 10, Scalar(255.0, 0.0, 0.0, 255.0))
        val rect = Rect()
        rect.x = 10
        rect.y = 10
        rect.width = 10
        rect.height = 10
        //绘制一个矩形
        Imgproc.rectangle(mat, rect, Scalar(255.0, 255.0, 0.0, 255.0))
        val center = Point()
        center.x = 20.0
        center.y = 20.0
        val rotatedRect = RotatedRect(center, Size(20.0, 10.0), 30.0)
        //(20,20)为中心绘制宽20高10，旋转30°，颜色为黄色椭圆?
        Imgproc.ellipse(mat, rotatedRect, Scalar(0.0, 255.0, 255.0, 255.0))
        //文字左下角为(30,20)绘制黄色文字
        Imgproc.putText(mat, "A Text", Point(30.0, 20.0), 1, 1.0, Scalar(0.0, 255.0, 255.0, 255.0))
        //根据mat创建一个bitmap
        val bitmap = Bitmap.createBitmap(mat.rows(), mat.cols(), Bitmap.Config.ARGB_8888)
        val dst = Mat()
        //空的bitmap转为mat
        Utils.bitmapToMat(bitmap, dst)
        //有内容的mat的颜色转换到空的bitmap上
        Imgproc.cvtColor(mat, dst, Imgproc.COLOR_BGRA2RGBA)
        //最终转移后的mat转bitmap
        Utils.matToBitmap(dst, bitmap)
        iv.setImageBitmap(bitmap)
    }

    //修改bitmap的像素颜色
    private fun changeBitmapPixel() {
        var bitmap = BitmapFactory.decodeResource(resources, R.mipmap.a)
        bitmap = bitmap.copy(bitmap.config, true)
        val width = bitmap.width
        val height = bitmap.height
        var r = 0
        var g = 0
        var b = 0
        for (j in 0 until height) {
            for (i in 0 until width) {
                val pixel = bitmap.getPixel(i, j)
                r = Color.red(pixel)
                g = Color.green(pixel)
                b = Color.blue(pixel)
                r = 255 - r
                g = 255 - g
                b = 255 - b
                bitmap.setPixel(i, j, Color.rgb(r, g, b))
            }
        }
        iv.setImageBitmap(bitmap)
    }

    //保存mat为图片
    private fun saveMatToPic() {
        val mat = Mat(100, 100, CvType.CV_8UC3)
        //BGRA
        mat.setTo(Scalar(0.0, 255.0, 0.0, 255.0))
        Imgcodecs.imwrite(Environment.getExternalStorageDirectory().absolutePath + "/a.png", mat)
    }

    //将bitmap灰度处理
    private fun bitmapToGray() {
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.app_icon)
        val src = Mat()
        val dst = Mat()
        Imgcodecs.imread("", Imgcodecs.IMREAD_COLOR)
        Utils.bitmapToMat(bitmap, src)
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGRA2GRAY)
        Utils.matToBitmap(dst, bitmap)
        iv.setImageBitmap(bitmap)
        logMat(src)
        src.release()
        dst.release()
    }

    private fun logMat(mat: Mat) {
        val w = mat.cols()
        val h = mat.rows()
        val channel = mat.channels()
        val depth = mat.depth()
        val type = mat.type()
        val dim = mat.dims()
        loge("w:$w,h:$h,channel:$channel,depth:$depth,type:$type,dim:$dim")
    }

    //测试Mat对象的创建
    private fun testMat() {
        val mat = Mat()
        mat.create(intArrayOf(3, 3), CvType.CV_8UC3)
        mat.create(3, 3, CvType.CV_8UC3)
        mat.create(Size(3.0, 3.0), CvType.CV_8UC3)
        var mat2 = Mat.eye(3, 3, CvType.CV_8UC3)
        mat2 = Mat.eye(Size(3.0, 3.0), CvType.CV_8UC3)
        mat2 = Mat.zeros(Size(3.0, 3.0), CvType.CV_8UC3)
        mat.setTo(Scalar(0.0, 0.0, 0.0))
        mat.setTo(Mat())

    }

    //加载OpenCV库
    private fun loadOpenCV() {
        if (OpenCVLoader.initDebug()) {
            loge("load opencv success")
        } else {
            loge("load opencv failed")
        }
    }
}