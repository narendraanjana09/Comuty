package com.nsa.comuty.home.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.nsa.comuty.R;
import com.nsa.comuty.databinding.FragmentMoreRvItemBinding;
import com.nsa.comuty.home.models.MoreModel;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MoreAdapter extends RecyclerView.Adapter<MoreAdapter.ViewHolder> {
    private Context context;
    private List<MoreModel> list;
    private OnMoreClickListener listener;

    public MoreAdapter(Context context, List<MoreModel> list, OnMoreClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_more_rv_item, parent, false);
        FragmentMoreRvItemBinding binding=FragmentMoreRvItemBinding.bind(view);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {

        return list.size();

    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private FragmentMoreRvItemBinding itemView;

        public ViewHolder(@NonNull FragmentMoreRvItemBinding itemView) {
            super(itemView.getRoot());
            this.itemView=itemView;
            itemView.bottomCard.setRotation(-9f);
            itemView.bottomCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(list.get(getAbsoluteAdapterPosition()));
                }
            });
        }

        public void setData(MoreModel model) {
            itemView.iconIv.setImageDrawable(model.getDrawable());
            itemView.titleTv.setText(model.getTitle());
        }
    }
}
