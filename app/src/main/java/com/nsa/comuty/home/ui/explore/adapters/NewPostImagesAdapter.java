package com.nsa.comuty.home.ui.explore.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nsa.comuty.R;
import com.nsa.comuty.databinding.NewPostAddImageItemBinding;

import java.util.List;

public class NewPostImagesAdapter extends RecyclerView.Adapter<NewPostImagesAdapter.ViewHolder> {
    private Context context;
    private List<Uri> imagesList;
    private OnClickListener listener;

    public NewPostImagesAdapter(Context context) {
        this.context = context;
    }

    public void setImagesList(List<Uri> imagesList) {
        this.imagesList = imagesList;
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_post_add_image_item,parent
                        ,false);
        NewPostAddImageItemBinding binding=NewPostAddImageItemBinding.bind(view);
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
        NewPostAddImageItemBinding itemView;
        public ViewHolder(@NonNull NewPostAddImageItemBinding itemView) {
            super(itemView.getRoot());
            this.itemView=itemView;
            itemView.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(getAbsoluteAdapterPosition());
                }

            });
            itemView.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onDelete(getAbsoluteAdapterPosition());
                }
            });
        }
        public void setImage(Uri uri){
            itemView.image1.setImageURI(uri);
        }
    }
    public interface OnClickListener{
        void onClick(int pos);
        void onDelete(int pos);
    }
}
