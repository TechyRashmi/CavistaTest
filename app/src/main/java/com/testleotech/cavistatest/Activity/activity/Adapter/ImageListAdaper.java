package com.testleotech.cavistatest.Activity.activity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.testleotech.cavistatest.Activity.activity.allClasses.DisplayActivity;
import com.testleotech.cavistatest.Activity.activity.model.ImageModel;
import com.testleotech.cavistatest.R;

import java.util.ArrayList;

public class ImageListAdaper extends RecyclerView.Adapter<ImageListAdaper.ImageListHolder> {


    private ArrayList<ImageModel> imageList;
    private Context mContext;

    public ImageListAdaper(ArrayList<ImageModel> imageList, Context mContext) {
        this.imageList = imageList;
        this.mContext = mContext;


    }

    public ImageListAdaper(Context mContext) {
        this.imageList = imageList;
        this.mContext = mContext;

    }

    @NonNull
            @Override
            public ImageListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_item, null);
        ImageListHolder holder = new ImageListHolder(v);
           return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull ImageListHolder holder, final int position) {

        //Setting image
                Glide.with(mContext)
                        .load(imageList.get(position).getLink())
                        .centerCrop()
                        .placeholder(R.drawable.loading)
                        .into(holder.imageview);


                holder.imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i=new Intent(mContext, DisplayActivity.class);
                        i.putExtra("id",imageList.get(position).getId());
                        i.putExtra("title",imageList.get(position).getTitle());
                        i.putExtra("image",imageList.get(position).getLink());
                        mContext.startActivity(i);
                    }
                });

            }

            @Override
            public int getItemCount() {
                return imageList.size();
            }

            public class ImageListHolder extends RecyclerView.ViewHolder {

              public ImageView imageview;


                public ImageListHolder(View view) {
                    super(view);
                    imageview=view.findViewById(R.id.imageview);

                }

            }
}


