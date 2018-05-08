package com.example.a15218.myutil.cache;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.example.a15218.myutil.cache.ImageTool.HexHelper;

/**
 * Created by 15218 on 2018/4/5.
 */

public class ImageLruCache implements ImageCache{

    LruCache<String,Bitmap> mLruCache;

    private ImageLruCache(){
       initLruCache();
    }

    public static ImageLruCache getInstance(){
        return ImageLruCacheHelper.IMAGE_LRU_CACHE;
    }

    private void initLruCache(){
       int maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);
       int cacheSize = maxMemory/8;
       mLruCache = new LruCache<String, Bitmap>(cacheSize){
           @Override
           protected int sizeOf(String key, Bitmap value) {
               return value.getRowBytes()*value.getHeight()/1024;
           }
       };
    }

    static class ImageLruCacheHelper{
        public static final ImageLruCache IMAGE_LRU_CACHE = new ImageLruCache();
    }
    @Override
    public void putImage(String url,Bitmap bitmap) {
        String key = HexHelper.hashKeyForDisk(url);
        if (mLruCache.get(key)==null){
            mLruCache.put(key,bitmap);
        }
    }

    @Override
    public Bitmap getImage( String url) {
        String key = HexHelper.hashKeyForDisk(url);
        return mLruCache.get(key);
    }
}
