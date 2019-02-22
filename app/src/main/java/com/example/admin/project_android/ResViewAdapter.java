package com.example.admin.project_android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ResViewAdapter extends RecyclerView.Adapter<ResViewAdapter.ImageViewHolder> {
    private Context context;
    private List<Restaurant> resItems;
    private OnItemClickListener mListener;

    public ResViewAdapter(Context context, List<Restaurant> resItems) {
        this.context = context;
        this.resItems = resItems;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        public TextView textView, loveTextView;
        public ImageView imageView;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.card_text_view);
            imageView = itemView.findViewById(R.id.card_image_view);
            loveTextView = itemView.findViewById(R.id.loveText);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.OnwhatEverClick(position);
                }
            }
            return false;
        }
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.image_item,viewGroup,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        Restaurant current = resItems.get(i);
        imageViewHolder.textView.setText(current.getName());
        imageViewHolder.loveTextView.setText(current.getLove()+" ♥");
        Picasso.get().load(current.getImageUri()).placeholder(R.drawable.ic_launcher_foreground)
                .fit()
                .centerCrop()
                .into(imageViewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return resItems.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);

        void OnwhatEverClick(int pos);

        void OnDeleteClick(int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
