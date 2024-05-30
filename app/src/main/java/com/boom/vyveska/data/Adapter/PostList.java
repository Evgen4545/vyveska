package com.boom.vyveska.data.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.boom.vyveska.R;
import com.boom.vyveska.data.CartInfo;
import com.boom.vyveska.data.ListInfo;
import com.boom.vyveska.data.MaskTransformation;
import com.boom.vyveska.data.PostInfo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;


public class PostList extends RecyclerView.Adapter<PostList.ViewHolder>{




    private final LayoutInflater inflater;
    private final List<PostInfo> InfoList;
    private  Context context;

    private final OnStateClickListener onClickListener;
    public interface OnStateClickListener{
        void onStateClick(PostInfo cartInfo, int position);
    }

    public PostList(Context context, List<PostInfo> InfoList,OnStateClickListener onClickListener) {

        this.onClickListener = onClickListener;
        this.InfoList = InfoList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.post_list, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
      PostInfo list = InfoList.get(position);
      holder.textPost.setText(String.valueOf(list.getText()));

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                onClickListener.onStateClick(list, position);
            }
        });



        final Transformation transformation = new MaskTransformation(context, R.drawable.convers_transformation);
        Picasso.get()
                .load(list.getPhoto())
                .resize(550, 350) // resizes the image to these dimensions (in pixel)
                .centerCrop()
                .transform(transformation)
                .into(holder.photo);



    }

    @Override
    public int getItemCount() {
        return InfoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView textPost;
        final ImageView photo;
        ViewHolder(View view){
            super(view);
            textPost = view.findViewById(R.id.textList);
            photo = view.findViewById(R.id.imagePost);

        }
    }
}