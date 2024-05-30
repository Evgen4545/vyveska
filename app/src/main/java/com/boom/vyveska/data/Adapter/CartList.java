package com.boom.vyveska.data.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.boom.vyveska.R;
import com.boom.vyveska.data.CartInfo;
import com.boom.vyveska.data.MaskTransformation;
import com.boom.vyveska.data.PostFullInfo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;


public class CartList extends RecyclerView.Adapter<CartList.ViewHolder>{




    public interface OnStateClickListener{
        void onStateClick(CartInfo cartInfo, int position);
    }

    private final OnStateClickListener onClickListener;


    private final LayoutInflater inflater;
    private final List<CartInfo> InfoList;
    private  Context context;

    public CartList(Context context, List<CartInfo> InfoList, OnStateClickListener onClickListener) {
        this.onClickListener = onClickListener;

        this.InfoList = InfoList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.cart_list, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
      CartInfo list = InfoList.get(position);
      holder.textPost.setText(String.valueOf(list.getText()));
      holder.count.setText(String.valueOf(list.getCount())+" шт");

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

        final TextView textPost;
        final TextView count;
        ViewHolder(View view){
            super(view);
            textPost = view.findViewById(R.id.cartText);
            count = view.findViewById(R.id.textView7);

        }
    }


}