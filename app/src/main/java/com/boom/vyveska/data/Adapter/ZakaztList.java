package com.boom.vyveska.data.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.boom.vyveska.R;
import com.boom.vyveska.data.CartInfo;
import com.boom.vyveska.data.ZakazInfo;

import java.util.List;


public class ZakaztList extends RecyclerView.Adapter<ZakaztList.ViewHolder>{




    public interface OnStateClickListener{
        void onStateClick(ZakazInfo cartInfo, int position);
    }

    private final OnStateClickListener onClickListener;


    private final LayoutInflater inflater;
    private final List<ZakazInfo> InfoList;
    private  Context context;

    public ZakaztList(Context context, List<ZakazInfo> InfoList, OnStateClickListener onClickListener) {
        this.onClickListener = onClickListener;

        this.InfoList = InfoList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.zakaz_list, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
      ZakazInfo list = InfoList.get(position);
      holder.name.setText(String.valueOf(list.getName()));
      holder.phone.setText(String.valueOf(list.getPhone()));
      holder.order.setText(String.valueOf(list.getOrder()));

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                onClickListener.onStateClick(list, position);
            }
        });







    }

    @Override
    public int getItemCount() {
        return InfoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView name;
        final TextView phone, order;
        ViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.name14);
            phone = view.findViewById(R.id.phone1);
            order = view.findViewById(R.id.orderView16);

        }
    }


}