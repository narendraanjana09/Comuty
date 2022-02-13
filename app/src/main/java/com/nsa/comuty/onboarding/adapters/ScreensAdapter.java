package com.nsa.comuty.onboarding.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nsa.comuty.R;
import com.nsa.comuty.databinding.ScreensLayoutBinding;
import com.nsa.comuty.onboarding.models.ScreensModel;

import java.util.ArrayList;
import java.util.List;

public class ScreensAdapter extends RecyclerView.Adapter<ScreensAdapter.ViewHolder> {

    private Context context;
    private List<ScreensModel> list;

    public ScreensAdapter(Context context) {
        this.context = context;
        list=new ArrayList<>();
     setData();

    }

    private void setData() {
        list.add(new ScreensModel("Engage With\n               Your College"
                ,context.getDrawable(R.drawable.screen1),
                "Connect with you friends\nat college",
                "friends-seniors-juniors"));

        list.add(new ScreensModel("Upcoming\n           events"
                ,context.getDrawable(R.drawable.screen2),
                "see or add any\nupcoming event\nin the college",
                "⦿ ◦ ◦ ◦"));

        list.add(new ScreensModel("It’s all\n    about\n       sharing"
                ,context.getDrawable(R.drawable.screen3),
                "Share your thoughts,\nideas, achievements\nwith your friends",
                "◦ ⦿ ◦ ◦"));

        list.add(new ScreensModel("Send\n     use-full\n         messages"
                ,context.getDrawable(R.drawable.screen4),
                "Chat privately to someone\nor create\nclub to chat with all",
                "◦ ◦ ⦿ ◦"));

        list.add(new ScreensModel("friends\n      and\n         colleagues"
                ,context.getDrawable(R.drawable.screen5),
                "See who else is here\nand search for them\nwho have the same skills",
                "◦ ◦ ◦ ⦿"));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ScreensLayoutBinding binding=ScreensLayoutBinding.inflate(LayoutInflater.from(context),parent,false);
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
        private ScreensLayoutBinding binding;
        public ViewHolder(@NonNull ScreensLayoutBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void setData(ScreensModel model) {
            binding.txt1.setText(model.getTxt1());
            binding.txt2.setText(model.getTxt2());
            binding.txt3.setText(model.getTxt3());
            binding.img1.setImageDrawable(model.getImg1());
        }
    }
}
