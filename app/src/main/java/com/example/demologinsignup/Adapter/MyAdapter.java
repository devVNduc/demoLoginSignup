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
import com.example.demologinsignup.Activity.DetailActivity;
import com.example.demologinsignup.Animation.animation;
import com.example.demologinsignup.Data.DataClass;
import com.example.demologinsignup.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private ArrayList<DataClass> dataList, searchList;
    public MyAdapter(Context context, ArrayList<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getDataImage()).into(holder.recImage);
        holder.recTitle.setText(dataList.get(position).getDataTitle());
        holder.recPrice.setText(dataList.get(position).getDataPrice());
        holder.recOrigin.setText(dataList.get(position).getDataOrigin());
        holder.recDesc.setText(dataList.get(position).getDataDesc());
        holder.recQuantity.setText(dataList.get(position).getDataQuantity());
        holder.recStatus.setText(dataList.get(position).getDataStatus());
        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(context, animation.class);
                context.startActivity(i);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getDataImage());
                        intent.putExtra("Title", dataList.get(holder.getAdapterPosition()).getDataTitle());
                        intent.putExtra("Price", dataList.get(holder.getAdapterPosition()).getDataPrice());
                        intent.putExtra("Origin", dataList.get(holder.getAdapterPosition()).getDataOrigin());
                        intent.putExtra("Description", dataList.get(holder.getAdapterPosition()).getDataDesc());
                        intent.putExtra("Key",dataList.get(holder.getAdapterPosition()).getKey());
                        intent.putExtra("Quantity",dataList.get(holder.getAdapterPosition()).getDataQuantity());
                        intent.putExtra("Status", dataList.get(holder.getAdapterPosition()).getDataStatus());
                        context.startActivity(intent);
                    }
                }, 3000);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }



    public void searchData(ArrayList<DataClass> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}



class MyViewHolder extends  RecyclerView.ViewHolder{
    ImageView recImage;
    TextView recTitle, recPrice, recOrigin, recDesc, recQuantity, recStatus;
    CardView recCard;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recImage = itemView.findViewById(R.id.recImage);
        recTitle = itemView.findViewById(R.id.recTitle);
        recPrice = itemView.findViewById(R.id.recPrice);
        recOrigin = itemView.findViewById(R.id.recOrigin);
        recDesc = itemView.findViewById(R.id.recDesc);
        recCard = itemView.findViewById(R.id.recCard);
        recQuantity = itemView.findViewById(R.id.recQuantity);
        recStatus = itemView.findViewById(R.id.recStatus);

    }
}