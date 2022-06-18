package com.nsa.comuty.home.ui.posts.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.nsa.comuty.R;
import com.nsa.comuty.databinding.PostStackImageItemBinding;
import com.nsa.comuty.extra.Image;
import com.nsa.comuty.extra.Zoom_Image_Dialog;
import com.nsa.comuty.onboarding.models.ImageModel;

import java.util.List;

public class PostImagesAdapter extends RecyclerView.Adapter<PostImagesAdapter.ViewHolder> {
    private Context context;
    private List<String> imagesList;
    private OnImageClickListener onImageClickListener;


    public PostImagesAdapter(Context context, List<String> imagesList, OnImageClickListener onImageClickListener) {
        this.context = context;
        this.imagesList = imagesList;
        this.onImageClickListener = onImageClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_stack_image_item,parent
                        ,false);
        PostStackImageItemBinding binding=PostStackImageItemBinding.bind(view);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setImage(imagesList.get(position));
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        PostStackImageItemBinding itemView;
        public ViewHolder(@NonNull PostStackImageItemBinding itemView) {
            super(itemView.getRoot());
            this.itemView=itemView;
            itemView.image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   onImageClickListener.OnClick(imagesList.get(getAbsoluteAdapterPosition()));
                }
            });

        }
        public void setImage(String link){
            new Image().loadImage(context,link, itemView.image1);
        }
    }
    public interface OnImageClickListener{
        void OnClick(String link);
    }

}
