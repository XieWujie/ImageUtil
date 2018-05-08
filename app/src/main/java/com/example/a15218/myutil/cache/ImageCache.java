package com.example.a15218.myutil.cache;

import android.graphics.Bitmap;

/**
 * Created by 15218 on 2018/4/3.
 */

public interface ImageCache {
     void putImage(String url, Bitmap bitmap);
     Bitmap getImage(String url);
}
