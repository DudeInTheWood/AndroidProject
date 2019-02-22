package com.example.admin.project_android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CheckInAdapter extends RecyclerView.Adapter<CheckInAdapter.ImageViewHolder>{

    public Context context;
    public List<CheckInData> cData;

    public CheckInAdapter(Context context, List<CheckInData> checkInData){
        this.context = context;
        this.cData = checkInData;

    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.comment_field,viewGroup,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        CheckInData checkInData = cData.get(i);
        imageViewHolder.commentText.setText(checkInData.getComment());
    }

    @Override
    public int getItemCount() {
        return cData.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView commentText;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            commentText = itemView.findViewById(R.id.commentText);
        }
    }


}
