package com.example.a15218.myutil.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.example.a15218.myutil.cache.ImageTool.HexHelper;
import com.jakewharton.disklrucache.DiskLruCache;
import java.io.File;
import java.io.IOException;

/**
 * Created by 15218 on 2018/4/3.
 */

public class ImageDiscache implements ImageCache {

    private static final int MAX_LOOG_SIZE = 50*1024*1024;
    private DiskLruCache mDiskLruCache;
    private ImageDiscache(){

    }
    static class InstanceHelper{
        public static final ImageDiscache imageDiscache = new ImageDiscache();
    }
    public static ImageDiscache getInstance(){
        return InstanceHelper.imageDiscache;
    }

    public ImageDiscache with(Context context){
        initDiskLruCache(context);
        return this;
    }
    private void initDiskLruCache(Context context) {
        try {
            mDiskLruCache =
                    DiskLruCache.open(getPath(context, "bitmap"), getAppVersion(context), 1, MAX_LOOG_SIZE);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private File getPath(Context context, String fileName) {
        String filePath;
        if (!Environment.isExternalStorageRemovable() &&
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            filePath = context.getExternalCacheDir().getPath();
        } else {
            filePath = context.getCacheDir().getPath();
        }
        return new File(filePath + File.separator + fileName);
    }

    private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    @Override
    public void putImage(String url, Bitmap bitmap) {
        String key = HexHelper.hashKeyForDisk(url);
        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,editor.newOutputStream(0));
                editor.commit();
            mDiskLruCache.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public Bitmap getImage(String url) {
        String key = HexHelper.hashKeyForDisk(url);
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if (snapshot!=null) {
                return BitmapFactory.decodeStream(snapshot.getInputStream(0));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

}
