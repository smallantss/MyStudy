package com.example.opengl.opencv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.example.mystudy.R;
import com.example.mystudy.imageloader.cache.BitmapCache;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class OpenCvUtil {

    public int testByte() {
        byte a = (byte) 255;
        return a & 0xff;
    }

    public void changeCompare(Context context, ImageView iv){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.a);
        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap, mat);
        Mat res = Mat.zeros(mat.rows(),mat.cols(),mat.type());
        double c = 1.2;
        Core.multiply(mat, new Scalar(c,c,c,c),res);
        Bitmap bitmap1 = Bitmap.createBitmap(res.cols(), res.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(res, bitmap1);
        iv.setImageBitmap(bitmap1);
    }

    public void changeColor(Context context, ImageView iv) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.a);
        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap, mat);
        Mat gray = new Mat();
        Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY);
        MatOfDouble means = new MatOfDouble();
        MatOfDouble stddens = new MatOfDouble();
        Core.meanStdDev(gray, means, stddens);
        double[] mean = means.toArray();
        double[] stdden = stddens.toArray();
        int width = gray.cols();
        int height = gray.rows();
        byte[] data = new byte[width * height];
        gray.get(0, 0, data);
        int pv = 0;
        int t = (int) mean[0];
        for (int i = 0; i < data.length; i++) {
            pv = data[i] & 0xff;
            if (pv > t) {
                data[i] = (byte) 255;
            } else {
                data[i] = (byte) 0;
            }
        }
        gray.put(0, 0, data);
        Bitmap bitmap1 = Bitmap.createBitmap(gray.cols(), gray.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(gray, bitmap1);
        iv.setImageBitmap(bitmap1);
    }

    public void handleRow(Context context, ImageView iv) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.a);
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap, dst);
        int channels = dst.channels();
        int width = dst.cols();
        int height = dst.rows();
        byte[] data = new byte[channels * width];
        int pv;
        for (int row = 0; row < height/2; row++) {
            dst.get(row, 0, data);
            for (int col = 0; col < data.length; col++) {
                pv = (data[col] & 0xff);
                pv = 100 - pv;
                data[col] = (byte) pv;
            }
            dst.put(row, 0, data);
        }
        Utils.matToBitmap(dst, bitmap);
        iv.setImageBitmap(bitmap);
//        for (row in 0 until height) {
//            //以(0,0)位置为起点，获取data长度的数据
//            dst.get(row, 0, data)
//            for (col in data.indices) {
//                //遍历宽度，修改每个值
//                pv = (data[col] and 0xff.toByte())
//                loge("pv before:$pv")
//                pv = (255 - pv).toByte()
//                loge("pv after:$pv")
//                data[col] = pv
//            }
//            dst.put(row, 0, data)
//        }
//        Utils.matToBitmap(dst, bitmap)
//        iv.setImageBitmap(bitmap)
    }
}
