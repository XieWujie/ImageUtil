package com.example.a15218.myutil;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 15218 on 2018/3/31.
 */

public class DiscacheUtil {
    public DiscacheUtil(Context context) {
      initDisLucache(context);
    }

    DiskLruCache diskLruCache;
    private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }
    public File getDiscacheDir(Context context,String pathName){
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())||!Environment.isExternalStorageRemovable()){
            cachePath = context.getExternalCacheDir().getPath();
        }else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath+File.separator+pathName);
    }
    private void initDisLucache(Context context){
        try{
            File fileDir = getDiscacheDir(context,"bitmap");
            if (!fileDir.exists()){
                fileDir.mkdir();
            }
            diskLruCache = DiskLruCache.open(fileDir,getAppVersion(context),1,20*1024*1024);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
            out = new BufferedOutputStream(outputStream, 8 * 1024);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
    public Bitmap getCache(String url){
        String key = hashKeyForDisk(url);
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
            Bitmap bitmap = BitmapFactory.decodeStream(snapshot.getInputStream(0));
            return bitmap;
        }catch (IOException i){
            i.printStackTrace();
        }
        return null;
    }
    public void setCache(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String key = hashKeyForDisk(url);
                try {
                    DiskLruCache.Editor editor = diskLruCache.edit(key);
                    if (downloadUrlToStream(url,editor.newOutputStream(0))){
                        editor.commit();
                    }else {
                        editor.abort();
                    }
                    diskLruCache.flush();
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        }).start();
    }


}
