package com.nsa.comuty.home.ui.posts.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nsa.comuty.R;
import com.nsa.comuty.databinding.PostStackImageItemBinding;

import java.util.List;

public class PostStackImagesAdapter extends RecyclerView.Adapter<PostStackImagesAdapter.ViewHolder> {
    private Context context;
    private List<Uri> imagesList;

    public PostStackImagesAdapter(Context context) {
        this.context = context;
    }

    public void setImagesList(List<Uri> imagesList) {
        this.imagesList = imagesList;
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

        }
        public void setImage(Uri uri){
            itemView.image1.setImageURI(uri);
        }
    }

}
