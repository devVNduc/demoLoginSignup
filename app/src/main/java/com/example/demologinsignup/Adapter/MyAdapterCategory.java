package com.example.demologinsignup.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demologinsignup.Activity.Product_Category_Activity;
import com.example.demologinsignup.Animation.animation;
import com.example.demologinsignup.Data.DataclassCategory;
import com.example.demologinsignup.R;

import java.util.ArrayList;

public class MyAdapterCategory extends RecyclerView.Adapter<MyViewHolderCategory> {
    private Context context;
    private ArrayList<DataclassCategory> dataclassCategories, searchList;
    public MyAdapterCategory(Context context, ArrayList<DataclassCategory> dataclassCategories) {
        this.context = context;
        this.dataclassCategories = dataclassCategories;
    }

    @NonNull
    @Override
    public MyViewHolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_category_item,parent,false);
        return new MyViewHolderCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderCategory holder, int position) {
        Glide.with(context).load(dataclassCategories.get(position).getCategoryImage()).into(holder.recImageCategory);
        holder.recTitleCategory.setText(dataclassCategories.get(position).getCategoryName());
        holder.recId.setText(dataclassCategories.get(position).getCategoryId());
        holder.recCardCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(context, animation.class);
                context.startActivity(i);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(context, Product_Category_Activity.class);
                        intent.putExtra("CategoryId", dataclassCategories.get(holder.getAdapterPosition()).getCategoryId());
                        context.startActivity(intent);
                    }
                }, 1000);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataclassCategories.size();
    }



    public void searchData(ArrayList<DataclassCategory> searchList){
        dataclassCategories = searchList;
        notifyDataSetChanged();
    }
}



class MyViewHolderCategory extends  RecyclerView.ViewHolder{
    ImageView recImageCategory;
    TextView recTitleCategory, recId;
    CardView recCardCategory;
    public MyViewHolderCategory(@NonNull View itemView) {
        super(itemView);
        recImageCategory = itemView.findViewById(R.id.recImageCategory);
        recTitleCategory = itemView.findViewById(R.id.recTitleCategory);
        recId = itemView.findViewById(R.id.recId);
        recCardCategory = itemView.findViewById(R.id.recCardCategory);

    }
}