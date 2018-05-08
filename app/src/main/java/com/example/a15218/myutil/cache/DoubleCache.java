package com.example.a15218.myutil.cache;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by 15218 on 2018/4/5.
 */

public class DoubleCache implements ImageCache{

    private ImageLruCache mImageLruCache ;
    private ImageDiscache mImageDisCache;
    private DoubleCache(){

    }

    public DoubleCache with(Context context){
        mImageLruCache = ImageLruCache.getInstance();
        mImageDisCache = ImageDiscache.getInstance().with(context);
        return this;
    }
    public static DoubleCache getInstance(){
        return DoubleCaCheHelper.DOUBLE_CACHE;
    }
    static class DoubleCaCheHelper{
        static final DoubleCache DOUBLE_CACHE = new DoubleCache();
    }
    @Override
    public void putImage(String url, Bitmap bitmap) {
        mImageLruCache.putImage(url,bitmap);
        mImageDisCache.putImage(url,bitmap);
    }

    @Override
    public Bitmap getImage(String url) {
        return mImageLruCache.getImage(url)==null?mImageDisCache.getImage(url):mImageLruCache.getImage(url);
    }
}
