package com.example.a15218.myutil;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 15218 on 2018/4/1.
 */

public class CondenseImage {
    public static Bitmap decodeBitmapFromResources(Resources resources,int res,int reqWidth,int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources,res,options);
        options.inSampleSize = getInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources,res,options);
    }

    public static Bitmap decodeBitmapFromInputStream(InputStream in,int reqWidth,int reqHeight){
        BufferedInputStream inputStream = new BufferedInputStream(in); //BufferedInputStream 支持来回读写操作
        inputStream.mark(Integer.MAX_VALUE);//标记流的初始位置
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream,null,options);
        options.inSampleSize = getInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        try {
            inputStream.reset(); //重置读写时流的初始位置
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(inputStream,null,options);

    }
    private static int getInSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
        int oldWidth = options.outWidth;
        int oldHeight = options.outHeight;
        int inSampleSize = 1;
        if (oldHeight>reqHeight||oldWidth>reqWidth){
            int haftWidth = oldWidth/2;
            int haftHeight = oldHeight/2;
            while (haftHeight/inSampleSize>=reqHeight&&haftWidth/inSampleSize>=reqWidth){
                inSampleSize*=2;
            }
        }
        return inSampleSize;
    }
}
