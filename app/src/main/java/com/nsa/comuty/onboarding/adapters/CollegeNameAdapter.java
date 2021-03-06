package com.nsa.comuty.onboarding.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nsa.comuty.R;
import com.nsa.comuty.databinding.CountryCodeItemBinding;
import com.nsa.comuty.onboarding.interfaces.CollegeClickListener;
import com.nsa.comuty.onboarding.models.CollegeModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CollegeNameAdapter extends RecyclerView.Adapter<CollegeNameAdapter.ViewHolder> {

    private Context context;
    private List<CollegeModel> list,searchList;
    private CollegeClickListener listener;

    public CollegeNameAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
        searchList = new ArrayList<>();
        setData();
    }

    public List<CollegeModel> getList() { return list; }

    public void setList(List<CollegeModel> list) { this.list = list; }

    public List<CollegeModel> getSearchList() { return searchList; }

    public void setSearchList(List<CollegeModel> searchList) { this.searchList = searchList; }

    public void setListener(CollegeClickListener listener) { this.listener = listener; }



    private void setData() {
        list.clear();
        for(String s:context.getResources().getStringArray(R.array.colleges)){
            s=s.replace("/","_");
            s=s.replace("-","_");
            s=s.replace(" ","_");
            s=s.replace(".","_");
            s=s.replace("#","_");
            s=s.replace("[","_");
            s=s.replace("]","_");
            s=s.replace("$","_");

        list.add(new CollegeModel(s.toUpperCase()));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Collections.sort(list, Comparator.comparing(CollegeModel::getCollegeName));
        }else{
            Collections.sort(list,(f1, f2)->{return f1.getCollegeName().compareTo(f2.getCollegeName());});
        }
        searchList.addAll(list);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CountryCodeItemBinding binding=CountryCodeItemBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(list.get(position));
    }


    @Override
    public int getItemCount() { return list.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CountryCodeItemBinding binding;
        public ViewHolder(@NonNull CountryCodeItemBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(list.get(getAbsoluteAdapterPosition()));
                }
            });
        }

        public void setData(CollegeModel collegeModel) {
            binding.getRoot().setText(collegeModel.getCollegeName());
        }
    }

}
