package com.example.a15218.myutil.adaper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.a15218.myutil.MyApplication;
import com.example.a15218.myutil.R;
import com.example.a15218.myutil.core.ImageLoader;

import java.util.List;

/**
 * Created by 15218 on 2018/4/5.
 */

public class RcAdaper extends RecyclerView.Adapter<RcAdaper.ViewHolder>{

    List<String> mList;

    public RcAdaper(List<String> list){
        mList = list;
    }
     static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(View view){
            super(view);
            imageView = (ImageView)view.findViewById(R.id.image_view);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String url = mList.get(position);
        ImageView imageView = holder.imageView;
        ImageLoader.with(MyApplication.getContext()).load(imageView,url).error( "http://ov80qs5d9.bkt.clouddn.com/2.jpg");
       // ImageLoader.setCacheType(ImageLoader.IMAGE_DISCACHE,MyApplication.getContext()).load(imageView,url).error( "http://ov80qs5d9.bkt.clouddn.com/2.jpg");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
